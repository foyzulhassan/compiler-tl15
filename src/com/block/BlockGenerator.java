package com.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.entities.TokenInfo;
import com.nodes.*;
import com.register.RegisterCounter;
import com.scanner.Token;


public class BlockGenerator {
	
	Stack<String> mStack = new Stack<String>();
	List<String> mCodes = new ArrayList<String>();
	RegisterCounter registerCounter = RegisterCounter.getInstance();
	BlockCounter blockCounter = BlockCounter.getInstance();
	static StringBuffer mBuffer = new StringBuffer();
	Map<String, Block> mBlockMap = new HashMap<String, Block>();
	
	public Map<String, Block> getBlockMap() {
		return mBlockMap;
	}

	String currentBlock = null;
	String lastUntrackedBlock = null;
	int counter = 0;
	private Program mbuildAst=null;
	
	
	public BlockGenerator(Program buildAST )
	{
		mbuildAst=new Program(null,null,null,null,null);
		mbuildAst=buildAST;		
	}
	
	
	public void generateBlocks(Program buildAST)
	{
		Block block = new Block(blockCounter.getCount());
		mBlockMap.put(block.getLabel(), block);
		currentBlock = block.getLabel();
		mStack.push(block.getLabel());
		
		generateILOc(buildAST, null);
		
		if (!mStack.isEmpty()) {
			Block block1 = mBlockMap.get(mStack.pop());
			block1.setBlock(block1.getBlock().concat(mBuffer.toString()));
		}

		if (!mStack.isEmpty()) {
			String t = mStack.pop();
		}

		findBlock();
		
		mCodes.add(mBuffer.toString());
	}
	
	private Block findBlock() {
		// TODO Auto-generated method stub
		Set<String> keySet = mBlockMap.keySet();
		for (String string : keySet) {
			Block block = mBlockMap.get(string);
			List<String> children = block.getChildren();
			if (children.isEmpty()) {

				block.setBlock(block.getBlock() + "\n" + "exit");
				return block;
			}
		}
		return null;
	}
	
	private void generateILOc(Node pNode, Node father) {
		if (pNode == null)
			return;
		List<Node> childrenNodes = pNode.getChildNodes();
		String localBlock = "";

		if (pNode.getTokenValue() != null && pNode.getTokenValue().getWord() == Token.WHILE) {

			String target = mStack.pop();
			Block block = mBlockMap.get(target);

			Block block2 = new Block(blockCounter.getCount());
			block.setChildren(block2.getLabel());
			mBuffer.append("jumpI ->" + block2.getLabel());
			mCodes.add(mBuffer.toString());
			if (!block.isProcess()) {
				block.setBlock(block.getBlock().concat(mBuffer.toString()));
				block.setProcess(true);
			}
			mBuffer = new StringBuffer();
			mStack.push(block2.getLabel());
			mBlockMap.put(block.getLabel(), block);
			mBlockMap.put(block2.getLabel(), block2);

		} else if (pNode.getTokenValue() != null && pNode.getTokenValue().getWord() == Token.IF) {

			String target = mStack.pop();
			Block block = mBlockMap.get(target);

			mBlockMap.put(block.getLabel(), block);

			updateBlockinfo(block, pNode);
			currentBlock = block.getLabel();
			localBlock = currentBlock;
			if (block.isWithElse()) {
				mStack.push(currentBlock);
			}

		}

		String sElse = "";
		for (Node node : childrenNodes) {

			if (node != null && node.getTokenValue() != null && pNode.getTokenValue() != null
					&& pNode.getTokenValue().getWord() == Token.IF) {

				Block block = mBlockMap.get(localBlock);
				if (node.getTokenValue().getWord() == Token.THEN) {

					String target = mStack.pop();
					Block b1 = new Block(blockCounter.getCount());
					Block b2 = new Block(blockCounter.getCount());
					mBuffer.append("cbr " + target + "-> " + b1.getLabel() + " , " + b2.getLabel() + "\n");
					block.setBlock(mBuffer.toString());
					block.setProcess(true);
					mBuffer = new StringBuffer();

					mBlockMap.put(b1.getLabel(), b1);
					mBlockMap.put(b2.getLabel(), b2);
					block.setChildren(b1.getLabel());
					block.setChildren(b2.getLabel());
					if (!block.isWithElse()) {
						mStack.push(b2.getLabel());
						mStack.push(currentBlock);
					}
					mStack.push(b1.getLabel());
					sElse = b2.getLabel();

				} else if (node.getTokenValue().getWord() == Token.ELSE) {
					mStack.push(sElse);
					block.setWithElse(true);
				}
			}

			if (node != null && pNode.getTokenValue() != null && pNode.getTokenValue().getWord() == Token.WHILE) {
				// TokenWord whileChild = node.getTokenValue();
				Node father1 = node.getFather();
				if (father1 != null && father1.getTokenValue() != null && father1.getTokenValue().getWord() == Token.DO) {
					String target = mStack.pop();
					Block b1 = new Block(blockCounter.getCount());
					Block b2 = new Block(blockCounter.getCount());
					mBuffer.append("cbr " + target + "-> " + b1.getLabel() + " , " + b2.getLabel() + "\n");
					mCodes.add(mBuffer.toString());
					String pop = mStack.pop();
					Block b = mBlockMap.get(pop);
					b.setBlock(b.getBlock().concat(mBuffer.toString()));
					b.setChildren(b1.getLabel());
					b.setChildren(b2.getLabel());
					mBlockMap.put(b1.getLabel(), b1);
					mBlockMap.put(b2.getLabel(), b2);
					mBuffer = new StringBuffer();

					if (father1.getTokenValue().getWord() == Token.DO) {

						// target = mStack.pop();
						mStack.push(b2.getLabel());
						mStack.push(b.getLabel());
						mStack.push(b1.getLabel());
					}

				}

			}

			generateILOc(node, pNode);
		}

		if (pNode != null) {
			generateCode(pNode);
		}

	}
	
	private void generateCode(Node pNode) {
		if (pNode.getTokenValue() != null) {
			Token word = pNode.getTokenValue().getWord();
			TokenInfo tword = pNode.getTokenValue();
			if (word == Token.num) {
				String target = "r_" + registerCounter.getCount();
				mBuffer.append("loadI " + tword.getWordValue() + " => " + target + " \n");
				mStack.push(target);

			} else if (word == Token.INT) {

				mStack.push("int");

			} else if (word == Token.BOOL) {

				mStack.push("bool");

			} else if (word == Token.READINT) {
				mStack.push("readint");

			} else if (word == Token.WRITEINT) {
				String target = mStack.pop();
				mBuffer.append("writeint " + target + "\n");

			} else if (word == Token.THEN) {
				String target = mStack.pop();
				Block block = mBlockMap.get(target);
				block.setBlock(block.getBlock().concat(mBuffer.toString()));
				block.setToken(word);
				mBlockMap.put(block.getLabel(), block);

				mBuffer = new StringBuffer();

			} else if (word == Token.ELSE) {

				String target = mStack.pop();
				Block block = mBlockMap.get(target);
				block.setBlock(block.getBlock().concat(mBuffer.toString()));
				block.setToken(word);
				mBlockMap.put(block.getLabel(), block);
				mBuffer = new StringBuffer();

			} else if (word == Token.boollit) {
				String target = "r_" + registerCounter.getCount();
				mBuffer.append("loadI " + tword.getWord() + " => " + target + " \n");
				mStack.push(target);

			} else if (word == Token.ident && pNode.isDeclaration()) {
				String target = "r_" + tword.getWordValue();
				String type = mStack.pop();
				String source = "0";
				if (type.equalsIgnoreCase("bool")) {
					source = "false";
				}
				mBuffer.append("loadI " + source + " => " + target + " \n");

			} else if (word == Token.ident) {
				String target = "r_" + tword.getWordValue();

				mStack.push(target);

			} else if (word == Token.IF) {
				String target = "";
				String temp1 = mStack.pop();
				Block block1 = mBlockMap.get(temp1);

				if (block1.isWithElse()) {
					Block block = new Block(blockCounter.getCount());
					mBlockMap.put(block.getLabel(), block);
					target = block.getLabel();
					mStack.push(target);

				} else {
					target = mStack.pop();
					mStack.push(target);

				}

				// System.err.println(block1.getLabel()+"IF"+block1.getBlock());
				List<String> children = block1.getChildren();

				for (String string : children) {
					Block temp = mBlockMap.get(string);
					if (!temp.isProcess() && !temp.getLabel().equalsIgnoreCase(target)) {
						temp.setBlock(temp.getBlock().concat("jumpI ->" + target));
						temp.setChildren(target);
						temp.setProcess(true);
					}

				}

				if (lastUntrackedBlock != null) {
					Block temp = mBlockMap.get(lastUntrackedBlock);
					if (!temp.isProcess()) {
						temp.setBlock(temp.getBlock().concat("jumpI ->" + target));
						temp.setChildren(target);
						temp.setProcess(true);
						mBlockMap.put(temp.getLabel(), temp);
					}
				}

				lastUntrackedBlock = target;

			} else if (word == Token.WHILE) {

				String temp = mStack.pop();
				mStack.push(temp);
				lastUntrackedBlock = temp;

			} else if (Token.getIntOPeration().contains(word)) {
				String target = "r_" + registerCounter.getCount();
				String popRight = mStack.pop();
				String popLeft = mStack.pop();
				if (word == Token.MULTIPLICATIVE && tword.getWordValue().equals("mod")) {
					mBuffer.append("div " + popLeft + " , " + popRight + " => " + target + " \n");
					String divResult = target;
					target = "r_" + registerCounter.getCount();
					mBuffer.append("mult " + divResult + " , " + popRight + " => " + target + " \n");
					String mulResult = target;
					target = "r_" + registerCounter.getCount();
					mBuffer.append("sub " + popLeft + " , " + mulResult + " => " + target + " \n");
				} else {
					mBuffer.append(getOPcode(tword) + popLeft + " , " + popRight + " => " + target + " \n");
				}
				mStack.push(target);

			} else if (word == Token.ASGN) {
				String popRight = mStack.pop();
				String popLeft = mStack.pop();
				if (popRight.equalsIgnoreCase("readint")) {
					mBuffer.append(popRight + " => " + popLeft + " \n");
				} else
					mBuffer.append("i2i " + popRight + " => " + popLeft + " \n");

			}

		} else {
			Node father = pNode.getFather();
			if (father != null && father.getTokenValue() != null) {

				Token token = father.getTokenValue().getWord();
				if (token == Token.DO) {
					String target = mStack.pop();
					Block block = mBlockMap.get(target);

					target = mStack.pop();
					mBuffer.append("jumpI -> " + target);
					if (!block.isProcess()) {
						block.setBlock(block.getBlock().concat(mBuffer.toString()));
						block.setChildren(target);
						block.setProcess(true);
					}
					mCodes.add(mBuffer.toString());
					mBuffer = new StringBuffer();

				}

			}
		}

	}
	
	private void updateBlockinfo(Block pBlock, Node pNode) {
		List<Node> childrenNodes = pNode.getChildNodes();

		for (Node node : childrenNodes) {
			if (node != null && node.getTokenValue() != null && node.getTokenValue() != null
					&& node.getTokenValue().getWord() == Token.ELSE) {
				pBlock.setWithElse(true);
			}
		}

	}
	
	private String getOPcode(TokenInfo tokeninfo) {
		if (tokeninfo.getWord() == Token.ADDITIVE && tokeninfo.getWordValue().equals("+")) {
			return "add ";
		} else if (tokeninfo.getWord() == Token.ADDITIVE && tokeninfo.getWordValue().equals("-")) {
			return "sub ";
		} else if (tokeninfo.getWord() == Token.MULTIPLICATIVE && tokeninfo.getWordValue().equals("*")) {
			return "mult ";
		} else if (tokeninfo.getWord() == Token.MULTIPLICATIVE && tokeninfo.getWordValue().equals("div")) {
			return "div ";
		} else if (tokeninfo.getWord() == Token.COMPARE && tokeninfo.getWordValue().equals(">")) {
			return "cmp_GT ";
		} else if (tokeninfo.getWord() == Token.COMPARE && tokeninfo.getWordValue().equals(">=")) {
			return "cmp_GE ";
		} else if (tokeninfo.getWord() == Token.COMPARE && tokeninfo.getWordValue().equals("<")) {
			return "cmp_LT ";
		} else if (tokeninfo.getWord() == Token.COMPARE && tokeninfo.getWordValue().equals("<=")) {
			return "cmp_LE ";
		} else if (tokeninfo.getWord() == Token.COMPARE && tokeninfo.getWordValue().equals("=")) {
			return "cmp_EQ ";
		} else if (tokeninfo.getWord() == Token.COMPARE && tokeninfo.getWordValue().equals("!=")) {
			return "cmp_NE ";
		}

		return null;
	}

}
