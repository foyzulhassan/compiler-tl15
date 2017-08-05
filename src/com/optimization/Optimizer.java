package com.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.block.Block;
import com.codegen.GlobalName;
import com.codegen.Instruction;
import com.codegen.MipsCodeGenerator;

public class Optimizer {
	Map<String, Block> mBlockMap = null;
	Map<String, GlobalName> mGlobalName = new HashMap<String, GlobalName>();
	Map<String, String> valueNumber = new HashMap<String, String>();
	Map<String, Hashtable<String, String>> markerScope = new HashMap<String, Hashtable<String, String>>();
	private StringBuffer mOptimizedCfg = new StringBuffer();
	private StringBuffer mOptimizedMips = new StringBuffer();

	int counter = 0;

	public StringBuffer getOptimizedCfg() {
		return mOptimizedCfg;
	}

	public StringBuffer getOptimizedMips() {
		return mOptimizedMips;
	}

	public Optimizer(Map<String, Block> pBlockMap) {
		mBlockMap = pBlockMap;
	}

	public void performOptimization() {

		// clear block visit status
		clearBlockVisitations();

		/* Remove unreachable blocks */
		upDateSuccessor();

		mBlockMap.get("B0").isEntryBlock = true;

		detectLoops(mBlockMap.get("B0"), null);

		upDateBranchCount();

		removeUnreachableBlocks();
		/* Ena of Remove unreachable blocks */

		/* Perform SSH Optimization */
		computeDominators(mBlockMap.get("B0"));

		buildDomTree();

		clearBlockVisitations();

		frontierDominance(mBlockMap.get("B0"));

		comupteGlobalNames();

		insertPhiFunction();

		clearBlockVisitations();

		upDatePhiparameter(mBlockMap.get("B0"));

		removeRedundentPhiparam();

		clearBlockVisitations();

		rename(mBlockMap.get("B0"));

		clearBlockVisitations();

		dominatorValueNumbering(mBlockMap.get("B0"));

		clearBlockVisitations();

		traversePhiGraph(mBlockMap.get("B0"));

		clearBlockVisitations();

		mOptimizedCfg.append(printPhiGraph());

		clearBlockVisitations();

		replacePhiFunction(mBlockMap.get("B0"));

		clearBlockVisitations();
		/* End of SSH Optimization */

		/* Generate MIPS code */
		MipsCodeGenerator mips = new MipsCodeGenerator();

		String code = mips.generateCode(mBlockMap, false);

		mOptimizedMips.append(code);
	}

	private void replacePhiFunction(Block pBlock) {
		List<Instruction> delete = new ArrayList<Instruction>();
		for (Instruction ins : pBlock.getInstructions()) {
			if (ins.isPhiFunction) {
				for (String key : ins.getParams().keySet()) {

					Block block = mBlockMap.get(key);
					Instruction instruction = new Instruction();
					instruction.setOpCode("i2i");
					instruction.setSource1(ins.getParams().get(key));
					instruction.setSource2(ins.getDestication());
					addbeforeJump(block, instruction);
					delete.add(ins);

				}

			}

		}

		for (Instruction instruction : delete) {
			pBlock.getInstructions().remove(instruction);

		}

		pBlock.setVisited(true);
		for (String dom : pBlock.getSucecessor()) {
			Block block = mBlockMap.get(dom);

			if (!block.isVisited()) {
				replacePhiFunction(block);
			}
		}

	}

	private void addbeforeJump(Block block, Instruction instruction) {
		int i = 0;
		boolean jump = false;
		for (Instruction ins : block.getInstructions()) {
			if (ins.getOpCode().equalsIgnoreCase("jumpI")) {
				i = block.getInstructions().indexOf(ins);
				jump = true;
				break;

			}

			if (ins.getOpCode().equalsIgnoreCase("cbr")) {
				i = block.getInstructions().indexOf(ins);
				jump = true;
				break;

			}

		}
		if (jump) {
			block.getInstructions().add(i, instruction);
		} else {
			block.getInstructions().add(instruction);
		}
	}

	private String printPhiGraph() {
		clearBlockVisitations();
		StringBuffer buffer = new StringBuffer();
		buffer.append("digraph graphviz { " + "\n" + " node [shape = none]; " + "\n" + "  edge [tailport = s]; " + "\n"
				+ " entry  " + "\n" + "subgraph cluster { " + "\n" + " color=\"/x11/white\" " + "\n");

		traverseBlock(mBlockMap.get("B0"), buffer);
		buffer.append("}");
		buffer.append("entry -> n0" + "\n");
		buffer.append("n" + counter + " -> " + "exit" + "\n");
		buffer.append("}");
		return buffer.toString();
	}

	private void upDatePhiparameter(Block pBlock) {
		for (Instruction ins : pBlock.getInstructions()) {
			String destination = getDestination(ins);
			if (destination != null && ins.isPhiFunction()) {

				pBlock.getLocals().put(destination, ins.getDestication());
			}

			if (destination != null && !ins.isPhiFunction()) {

				if (!ins.getOpCode().equalsIgnoreCase("writeint")) {

					pBlock.getLocals().put(destination, destination);
				}
			}
		}
		// TODO Auto-generated method stub
		pBlock.setVisited(true);
		for (String dom : pBlock.getSucecessor()) {
			Block block = mBlockMap.get(dom);
			updateLiveValues(pBlock, block);
			for (Instruction ins : block.getInstructions()) {

				String destination = getDestination(ins);
				if (ins.isPhiFunction()) {
					String parameter = ins.getParams().get(pBlock.getLabel());
					if (parameter == null) {
						if (pBlock.getLocals().containsKey(ins.destication)) {

							ins.getParams().put(pBlock.getLabel(), ins.getDestication());
						}
					}

				}

			}
			if (!block.isVisited()) {

				upDatePhiparameter(block);
			}
		}
	}

	private void removeRedundentPhiparam() {

		for (String name : mBlockMap.keySet()) {
			Block block = mBlockMap.get(name);
			List<Instruction> instructionList = block.getInstructions();

			for (Instruction ins : instructionList) {
				if (ins.isPhiFunction) {
					List<String> temp = new ArrayList<String>();
					for (String key : ins.getParams().keySet()) {
						temp.add(key);
					}

					for (String string : temp) {

						Block block2 = mBlockMap.get(string);
						if (block2.getDominance().size() > 0) {
							String idom = block2.getDominance().get(block2.getDominance().size() - 1);
							if (ins.getParams().containsKey(idom) && !idom.equalsIgnoreCase(block.getLabel())
									&& isEqualFdom(block2, idom)) {
								ins.getParams().remove(idom);

							}
						}
					}

				}

			}
		}

	}

	private boolean isEqualFdom(Block pBlock2, String pIdom) {
		Block block = mBlockMap.get(pIdom);
		for (String dom : block.getDominanceFrontier()) {
			if (pBlock2.getDominanceFrontier().contains(dom)) {
				return true;

			}
		}
		return false;
	}

	private void buildDomTree() {
		for (String name : mBlockMap.keySet()) {
			Block block = mBlockMap.get(name);
			List<String> dominance = block.getDominance();
			if (!dominance.isEmpty()) {
				String idom = dominance.get(dominance.size() - 1);
				Block block2 = mBlockMap.get(idom);
				if (!block2.getDomChild().contains(block.getLabel())) {
					block2.addDomChild(block.getLabel());
				}
			} else if (dominance.isEmpty() && block.getPredecessor().isEmpty()) {

			}
		}

	}

	private String getPhiparameter(Instruction pIns) {
		String params = "";
		for (String key : pIns.getParams().keySet()) {
			params += pIns.getParams().get(key) + ", ";
		}
		return params;
	}

	private void rename(Block pBlock) {
		pBlock.setVisited(true);

		for (Instruction ins : pBlock.getInstructions()) {

			String destination = getDestination(ins);
			if (destination != null && ins.isPhiFunction()) {

				/*
				 * if
				 * (pBlock.getDominanceFrontier().contains(pBlock.getLabel())) {
				 * upDatePhiFuction(pBlock, pBlock); }
				 */

				GlobalName globalName = mGlobalName.get(destination);
				String newName = destination + "_" + globalName.getCounter();
				globalName.incrementCount();

				if (ins.indexDest == 2) {
					ins.setSource2(newName);
				} else if (ins.indexDest == 3) {
					ins.setDestication(newName);
				}

				globalName.getStack().push(newName);
				pBlock.getLocals().put(destination, newName);
			}

			if (ins.getOpCode().equalsIgnoreCase("cbr")) {
				GlobalName globalName = mGlobalName.get(ins.getSource1());
				String newName = globalName.getStack().peek();
				ins.setSource1(newName);
			}

			if (destination != null && !ins.isPhiFunction()) {

				GlobalName globalName = mGlobalName.get(ins.getSource1());

				if (globalName != null && !ins.getOpCode().equalsIgnoreCase("readint")) {
					String newName = globalName.getStack().peek();
					ins.setSource1(newName);
					if (ins.indexDest == 3) {
						globalName = mGlobalName.get(ins.getSource2());
						newName = globalName.getStack().peek();
						ins.setSource2(newName);
					}
				}

				if (!ins.getOpCode().equalsIgnoreCase("writeint")) {
					globalName = mGlobalName.get(destination);
					String newName = destination + "_" + globalName.getCounter();
					globalName.incrementCount();

					if (ins.indexDest == 2) {
						ins.setSource2(newName);
					} else if (ins.indexDest == 3) {
						ins.setDestication(newName);
					} else if (ins.indexDest == 1) {
						ins.setSource1(newName);
					}

					globalName.getStack().push(newName);
					pBlock.getLocals().put(destination, newName);
				}
			}

		}

		for (String succ : pBlock.getSucecessor()) {

			Block block = mBlockMap.get(succ);

			updateLiveValues(pBlock, block);

			upDatePhiFuction(block, pBlock);

		}

		for (String dom : pBlock.getSucecessor()) {
			Block block = mBlockMap.get(dom);
			if (!block.isVisited()) {
				rename(block);
			} else if (block.isVisited()) {
				for (Instruction ins : block.getInstructions()) {
					if (ins.isPhiFunction) {
						String destination = ins.getSource1();
						GlobalName globalName = mGlobalName.get(destination);
						if (!globalName.getStack().isEmpty()) {
							String value = globalName.getStack().peek();
							if (getNewDestination(ins.getParams(), value)) {
								globalName.getStack().clear();
								globalName.getStack().push(ins.getDestication());

							}
						}
					}

				}
			}
		}

		for (Instruction ins : pBlock.getInstructions()) {

			String destination = getDestination(ins);
			if (destination != null && !ins.isPhiFunction()) {
				GlobalName globalName = mGlobalName.get(destination);
				if (globalName != null && (ins.indexDest == 2 || ins.indexDest == 3)
						&& !globalName.getStack().isEmpty())
					globalName.getStack().pop();
			}
		}

	}

	private void updateLiveValues(Block pBlock, Block pBlock2) {
		for (String key : pBlock.getLocals().keySet()) {
			if (!pBlock2.getLocals().containsKey(key)) {

				pBlock2.getLocals().put(key, pBlock.getLocals().get(key));

			}
		}

	}

	private boolean getNewDestination(Map<String, String> pParams, String value) {
		for (String key : pParams.keySet()) {
			String string = pParams.get(key);
			Block block = mBlockMap.get(key);
			if (block.getLocals().get(string) != null) {
				pParams.put(key, block.getLocals().get(string));
			}
			if (string.equalsIgnoreCase(value)) {
				return true;
			}
		}

		return false;
	}

	private void dominatorValueNumbering(Block pBlock) {
		markerScope.put(pBlock.getLabel(), new Hashtable<String, String>());
		List<Instruction> delete = new ArrayList<Instruction>();

		for (Instruction ins : pBlock.getInstructions()) {

			if (ins.isPhiFunction) {
				if (isNotValidPhi(ins)) {
					valueNumber.put(ins.getDestication(), ins.getSource1());
					delete.add(ins);
				} else {
					updatePhi(pBlock, ins);
					valueNumber.put(ins.getDestication(), ins.getDestication());
					Hashtable<String, String> hashtable = markerScope.get(pBlock.getLabel());
					hashtable.put(ins.getDestication(), getPhiparameter(ins));
				}

			}

			if (!ins.isPhiFunction) {

				String destination = getDestination(ins);

				if (ins.getOpCode().equalsIgnoreCase("i2i")) {

					valueNumber.put(ins.getSource2(), valueNumber.get(ins.getSource1()));

					delete.add(ins);

				} else if (destination != null && ins.indexDest > 1) {
					boolean flag = false;
					String y = "";
					if (ins.getOpCode().equalsIgnoreCase("loadI")) {
						y = ins.getSource1();

					} else {
						y = valueNumber.get(ins.getSource1());
					}
					String expr = y + ins.getOpCode();
					if (ins.indexDest > 2) {
						String z = valueNumber.get(ins.getSource2());
						expr += z;
						flag = true;
					}

					String value = looupHastable(expr);
					if (value != null) {
						if (flag) {
							valueNumber.put(ins.getDestication(), value);
						} else {
							valueNumber.put(ins.getSource2(), value);
						}
						delete.add(ins);

					} else {
						if (flag) {
							valueNumber.put(ins.getDestication(), ins.getDestication());
							Hashtable<String, String> hashtable = markerScope.get(pBlock.getLabel());
							hashtable.put(expr, ins.getDestication());
						} else {
							valueNumber.put(ins.getSource2(), ins.getSource2());
							Hashtable<String, String> hashtable = markerScope.get(pBlock.getLabel());
							hashtable.put(expr, ins.getSource2());
						}
					}

				}

				if (ins.getOpCode().equalsIgnoreCase("readint")) {
					valueNumber.put(ins.getSource1(), ins.getSource1());

				} else if (ins.getOpCode().equalsIgnoreCase("writeint")) {

				}
			}
		}

		for (Instruction instruction : delete) {

			pBlock.getInstructions().remove(instruction);
		}

		for (String succ : pBlock.getSucecessor()) {

			Block block = mBlockMap.get(succ);

			upDatePhiFuction2(block, pBlock);
			redorderIns(block);

		}

		for (String dom : pBlock.getDomChild()) {
			Block block = mBlockMap.get(dom);
			dominatorValueNumbering(block);
		}

		markerScope.remove(pBlock.getLabel());

	}

	private void redorderIns(Block pBlock) {
		int i = 0;
		Instruction save = null;
		for (Instruction ins1 : pBlock.getInstructions()) {
			if (ins1.isPhiFunction) {
				for (Instruction ins2 : pBlock.getInstructions()) {
					if (ins2.isPhiFunction) {
						for (String key : ins2.getParams().keySet()) {
							String value = ins2.getParams().get(key);

							if (value.equalsIgnoreCase(ins1.getDestication())) {
								i = pBlock.getInstructions().indexOf(ins1);
								save = ins2;
								break;
							}
						}
					}
				}
				if (save != null) {
					break;
				}
			}
		}

		if (save != null) {
			pBlock.getInstructions().remove(save);
			pBlock.getInstructions().add(i, save);
		}

	}

	private void updatePhi(Block pBlock, Instruction pIns) {
		List<String> pred = new ArrayList<String>();
		List<String> nonpred = new ArrayList<String>();
		for (String key : pIns.getParams().keySet()) {

			if (pBlock.getPredecessor().contains(key)) {

				pred.add(key);
			} else {
				nonpred.add(key);
			}

		}
		if (nonpred.size() > 0) {
			String value = pIns.getParams().get(nonpred.get(0));
			for (String key : pred) {
				String newValue = pIns.getParams().get(key);
				if (value.equalsIgnoreCase(newValue)) {
					pIns.getParams().remove(nonpred.get(0));
				}
			}
		}

	}

	private boolean isNotValidPhi(Instruction pIns) {
		List<String> list = new ArrayList<String>();

		if (pIns.getParams().size() == 1) {
			return true;
		}

		for (String key : pIns.getParams().keySet()) {
			String string = pIns.getParams().get(key);
			if (string == null) {
				return false;
			}

			list.add(string);
			if (string.equalsIgnoreCase(pIns.getDestication())) {
				return true;
			}

		}

		int i = 0;
		for (String string : list) {

			if (string.equalsIgnoreCase(list.get(0))) {
				i++;
				continue;
			} else if (i == pIns.getParams().size()) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	private void upDatePhiFuction2(Block pBlock, Block pBlock2) {
		for (Instruction ins : pBlock.getInstructions()) {

			if (ins.isPhiFunction()) {

				String parameter = ins.getParams().get(pBlock2.getLabel());

				String value = valueNumber.get(parameter);
				ins.setSource1(value);
				ins.getParams().put(pBlock2.getLabel(), value);

			}

		}

	}

	private String looupHastable(String pExpr) {

		for (String key : markerScope.keySet()) {
			Hashtable<String, String> hashtable = markerScope.get(key);
			if (hashtable.containsKey(pExpr)) {

				return hashtable.get(pExpr);

			}
		}
		return null;
	}

	private void upDatePhiFuction(Block pBlock, Block pBlock2) {

		for (Instruction ins : pBlock.getInstructions()) {

			String destination = getDestination(ins);
			if (ins.isPhiFunction()) {
				String parameter = ins.getParams().get(pBlock2.getLabel());
				if (parameter == null) {
					if (pBlock2.getLocals().containsKey(ins.destication)) {
						GlobalName globalName = mGlobalName.get(ins.destication);
						String newName = globalName.getStack().peek();
						ins.getParams().put(pBlock2.getLabel(), newName);

					}
				}

				GlobalName globalName = mGlobalName.get(parameter);

				if (globalName != null && !globalName.getStack().isEmpty()) {
					String newName = globalName.getStack().peek();
					ins.getParams().put(pBlock2.getLabel(), newName);
				}

			}

		}

	}

	private void insertPhiFunction() {

		for (String name : mGlobalName.keySet()) {
			GlobalName gName = mGlobalName.get(name);

			List<String> addTo = new ArrayList<String>();

			for (String string : gName.getBlocks()) {

				if (gName.getBlocks().size() > 1)
					updateGname(name, addTo, string);

			}

			for (String s : addTo) {
				gName.getBlocks().add(s);
			}

		}

	}

	private void updateGname(String name, List<String> addTo, String string) {
		Block block = mBlockMap.get(string);
		for (String fdom : block.getDominanceFrontier()) {

			if (!block.getLabel().equalsIgnoreCase(fdom)) {
				Block block2 = mBlockMap.get(fdom);
				if (!block2.getInsertedPhi().containsKey(name)) {
					Instruction ins = new Instruction();
					ins.setPhiFunction(true);
					ins.setOpCode("phi");
					ins.setSource1(name);
					ins.setSource2(name);
					ins.setDestication(name);
					ins.getParams().put(block.getLabel(), name);

					block2.getInstructions().add(0, ins);
					// gName.getBlocks().add(block2.getLabel());
					addTo.add(block2.getLabel());
					block2.getInsertedPhi().put(ins.getDestication(), ins);
					updateGname(name, addTo, block2.getLabel());
				} else if (block2.getInsertedPhi().containsKey(name)) {
					Instruction instruction = block2.getInsertedPhi().get(name);
					instruction.getParams().put(block.getLabel(), name);
				}

			}

		}
	}

	private void comupteGlobalNames() {
		for (String name : mBlockMap.keySet()) {
			Block block = mBlockMap.get(name);
			List<Instruction> instructionList = getInstructionList(block);
			block.setInstructions(instructionList);
			findGlobalNames(block);
		}

	}

	public void removeUnreachableBlocks() {
		// Create a list of blocks that cannot be reached.
		ArrayList<Block> toRemove = new ArrayList<Block>();
		for (String name : mBlockMap.keySet()) {
			Block block = mBlockMap.get(name);
			if (!block.isVisited()) {
				toRemove.add(block);
			}
		}

		// From the predecessor list for each blocks, remove
		// the ones that are in toRemove list.
		for (String name : mBlockMap.keySet()) {
			Block block = mBlockMap.get(name);
			for (Block pred : toRemove) {
				block.getPredecessor().remove(pred.getLabel());
			}
		}

		// From the list of all blocks, remove the ones that
		// are in toRemove list.
		for (Block block : toRemove) {
			mBlockMap.remove(block.getLabel());
		}
	}

	private void upDateBranchCount() {
		// TODO Auto-generated method stub
		for (String name : mBlockMap.keySet()) {
			Block block = mBlockMap.get(name);

			block.ref = block.getPredecessor().size();
			block.fwdBranch = block.getPredecessor().size() - block.bwdBranch;
		}
	}

	private void upDateSuccessor() {
		for (String name : mBlockMap.keySet()) {
			Block block = mBlockMap.get(name);

			for (String key : block.getChildren()) {

				block.addSucecessor(key);
				Block block2 = mBlockMap.get(key);
				block2.addPredecessor(block.getLabel());
			}
		}

	}

	public void computeDominators(Block block) {

		if (block.ref > 0) {
			block.ref--;
		}

		if (!block.getPredecessor().isEmpty()) {

			if (block.getPredecessor().size() == 1) {
				Block block2 = mBlockMap.get(block.getPredecessor().get(0));
				for (String d : block2.getDominance()) {
					if (!block.getDominance().contains(d))
						block.addDominance(d);
				}
				if (!block.getDominance().contains(block2.getLabel()))
					block.addDominance(block2.getLabel());

			} else {
				commonDom(block);

			}
		}

		if (block.ref == block.bwdBranch) {
			for (String s : block.getSucecessor()) {
				Block block2 = mBlockMap.get(s);

				computeDominators(block2);

			}
		}

	}

	private List<String> commonDom(Block pBlock) {

		/*
		 * Block dom = pBlock; clearBlockVisitations(); while
		 * (!dom.getDominance().isEmpty()) { dom.setVisited(true);
		 * 
		 * dom = mBlockMap.get(dom.getDominance().get(0));
		 * 
		 * }
		 * 
		 * System.err.println(">>>>>>>" + pBlock.getLabel() + " " + pPred);
		 * 
		 * dom = mBlockMap.get(pPred); while (!dom.isVisited()) {
		 * 
		 * dom = mBlockMap.get(dom.getDominance().get(0));
		 * 
		 * }
		 */

		List<String> predecessor = pBlock.getPredecessor();

		List<Block> blocks = new ArrayList<Block>();

		/*
		 * while( !predecessor.get(0).equalsIgnoreCase(predecessor.get(1))){
		 * 
		 * Block block = mBlockMap.get(pred);
		 * 
		 * }
		 */
		for (String pred : predecessor) {
			Block block = mBlockMap.get(pred);

			blocks.add(block);

		}

		if (!blocks.isEmpty()) {

			// oneCanReachanother(blocks.get(0),blocks.get(1));
			if (blocks.get(0).isEntryBlock || blocks.get(1).isEntryBlock) {
				if (blocks.get(0).isEntryBlock && !pBlock.getDominance().contains(blocks.get(0).getLabel())) {
					pBlock.addDominance(blocks.get(0).getLabel());
				} else if (blocks.get(1).isEntryBlock && !pBlock.getDominance().contains(blocks.get(1).getLabel())) {
					pBlock.addDominance(blocks.get(1).getLabel());
				}
				return null;
			}
			int i = 0;
			for (String dom1 : blocks.get(0).getDominance()) {

				for (String dom2 : blocks.get(1).getDominance()) {

					if (dom2.equalsIgnoreCase(dom1)) {
						i++;
						if (!pBlock.getDominance().contains(dom2)) {
							pBlock.addDominance(dom2);
						}

					}

				}

			}
			if (i > 0 && i == blocks.get(0).getDominance().size()) {
				if (!pBlock.getDominance().contains(blocks.get(0).getLabel())) {
					pBlock.addDominance(blocks.get(0).getLabel());
				}
			} else if (i > 0 && i == blocks.get(1).getDominance().size()) {
				if (!pBlock.getDominance().contains(blocks.get(1).getLabel())) {
					pBlock.addDominance(blocks.get(1).getLabel());
				}
			}

			if (blocks.get(0).getDominance().isEmpty() || blocks.get(1).getDominance().isEmpty()) {
				for (String dom : blocks.get(0).getDominance()) {
					if (!pBlock.getDominance().contains(dom))
						pBlock.addDominance(dom);
				}
				for (String dom : blocks.get(1).getDominance()) {
					if (!pBlock.getDominance().contains(dom))
						pBlock.addDominance(dom);
				}
			}

			if (pBlock.isLoopHead) {
				if (!blocks.get(1).getDominance().isEmpty()
						&& !blocks.get(1).getDominance().contains(pBlock.getLabel())) {
					for (String dom2 : blocks.get(1).getDominance()) {

						if (!pBlock.getDominance().contains(dom2)) {
							pBlock.addDominance(dom2);
						}

					}
					if (!pBlock.getDominance().contains(blocks.get(1).getLabel())) {
						pBlock.addDominance(blocks.get(1).getLabel());
					}
				} else if (!blocks.get(0).getDominance().isEmpty()
						&& !blocks.get(0).getDominance().contains(pBlock.getLabel())) {

					for (String dom2 : blocks.get(0).getDominance()) {

						if (!pBlock.getDominance().contains(dom2)) {
							pBlock.addDominance(dom2);
						}

					}
					if (!pBlock.getDominance().contains(blocks.get(0).getLabel())) {
						pBlock.addDominance(blocks.get(0).getLabel());
					}
				}

			}
			pBlock.setVisited(true);
		}

		return null;
	}

	private void clearBlockVisitations() {
		for (String name : mBlockMap.keySet()) {
			Block block = mBlockMap.get(name);
			block.setVisited(false);

		}
	}

	private void frontierDominance(Block block) {

		List<String> domChild = block.getDomChild();
		for (String dom : domChild) {
			Block block2 = mBlockMap.get(dom);
			if (!block2.isVisited()) {
				block2.setVisited(true);
				frontierDominance(block2);
			}
		}

		List<String> sucecessor = block.getSucecessor();

		for (String succ : sucecessor) {
			Block block2 = mBlockMap.get(succ);
			List<String> dominance = block2.getDominance();
			if (!dominance.isEmpty()) {
				String idom = dominance.get(dominance.size() - 1);
				if (!idom.equalsIgnoreCase(block.getLabel())) {

					block.addDominanceFrontier(succ);
				}
			}
		}

		domChild = block.getDomChild();

		for (String domc : domChild) {

			Block block3 = mBlockMap.get(domc);

			for (String fdom : block3.getDominanceFrontier()) {
				Block block2 = mBlockMap.get(fdom);
				List<String> dominance = block2.getDominance();
				String idom = dominance.get(dominance.size() - 1);
				if (!idom.equalsIgnoreCase(block.getLabel())) {

					block.addDominanceFrontier(fdom);

				}
			}
		}

	}

	public void detectLoops(Block block, Block pred) {
		if (!block.isVisited()) {
			block.setVisited(true);
			block.isActive = true;
			for (String succ : block.getSucecessor()) {
				Block block2 = mBlockMap.get(succ);
				detectLoops(block2, block);
			}
			block.isActive = false;
		} else if (block.isActive) {
			block.isLoopHead = true;
			pred.isLoopTail = true;
			block.bwdBranch++;

		}
	}

	private List<Instruction> getInstructionList(Block pBlock) {
		String sblock = pBlock.getBlock();
		List<Instruction> insList = new ArrayList<Instruction>();
		String[] split = sblock.trim().split("\n");
		String regexp = "[\\s,;\\n\\t]+";
		for (int i = 0; i < split.length; i++) {
			String ins = split[i];
			ins = ins.replace("=>", "");
			ins = ins.replace("->", "");

			String[] split2 = ins.split(regexp);

			Instruction tempIns = new Instruction();
			if (split2.length > 0) {
				String opcode = split2[0].trim();

				tempIns.setOpCode(opcode);
			}
			if (split2.length > 1) {
				String opcode = split2[1].trim();
				tempIns.setSource1(opcode);
			}
			if (split2.length > 2) {
				String opcode = split2[2].trim();
				tempIns.setSource2(opcode);
			}
			if (split2.length > 3) {
				String opcode = split2[3].trim();
				tempIns.setDestication(opcode);
			}

			if (split2.length > 0) {
				insList.add(tempIns);
			}

		}
		return insList;
	}

	private void findGlobalNames(Block pBlock) {
		for (Instruction instruction : pBlock.getInstructions()) {
			String source2 = getDestination(instruction);

			if (source2 != null) {
				GlobalName gName = mGlobalName.get(source2);
				if (gName == null) {

					gName = new GlobalName();
					gName.getBlocks().add(pBlock.getLabel());
					mGlobalName.put(source2, gName);
				} else {
					gName.getBlocks().add(pBlock.getLabel());
				}
			}

		}
	}

	private String getDestination(Instruction instruction) {
		String source2 = null;
		switch (instruction.getOpCode()) {
		case "loadI":
			source2 = instruction.getSource2();
			instruction.indexDest = 2;
			break;

		case "add":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;
			break;

		case "sub":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;
			break;

		case "div":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;
			break;

		case "mult":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;
			break;

		case "cmp_GT":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;
			break;

		case "cmp_LT":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;

			break;

		case "cmp_GE":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;
			break;

		case "cmp_LE":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;
			break;
		case "i2i":
			source2 = instruction.getSource2();
			instruction.indexDest = 2;
			break;
		case "cbr":
			break;

		case "cmp_EQ":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;
			break;

		case "cmp_NE":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;

			break;

		case "phi":
			source2 = instruction.getDestication();
			instruction.indexDest = 3;

			break;
		case "readint":
			source2 = instruction.getSource1();
			instruction.indexDest = 1;

			break;
		case "writeint":
			source2 = instruction.getSource1();
			instruction.indexDest = 1;

			break;

		default:
			break;
		}
		return source2;
	}

	private void traverseBlock(Block pBlock, StringBuffer buffer) {
		if (pBlock == null) {
			return;
		}
		String blocklavel = "n# [label=@,   fillcolor=\"/x11/white\", shape=box]";
		String label = blocklavel.replace("#", "" + pBlock.getNumber());
		String name = decorateTable(pBlock);// ;pBlock.getLabel()+" \n"+
											// pBlock.getBlock() ;
		label = label.replace("@", name);
		buffer.append(label);
		buffer.append("\n");
		pBlock.setVisited(true);
		List<String> children = pBlock.getChildren();

		for (String string : children) {
			Block block = mBlockMap.get(string);

			if (block != null && !block.isVisited())
				traverseBlock(block, buffer);
			if (block != null) {
				if (block.getChildren().size() == 0) {
					counter = block.getNumber();
				}
				buffer.append("n" + pBlock.getNumber() + " -> " + "n" + block.getNumber() + "\n");
			}
		}
	}

	private String decorateTable(Block pBlock) {

		StringBuffer table = new StringBuffer();
		table.append("<<table border='0'> <tr><td border='1'>" + pBlock.getLabel() + "</td> </tr>");

		for (Instruction ins : pBlock.getInstructions()) {
			String destination = getDestination(ins);
			String row = "";
			if (ins.getOpCode().equalsIgnoreCase("cbr")) {

				row = ins.getOpCode() + " " + ins.getSource1() + " - &gt; " + ins.getSource2() + " ,"
						+ ins.getDestication();

			} else if (ins.getOpCode().equalsIgnoreCase("jumpI")) {

				row = ins.getOpCode() + " - &gt; " + ins.getSource1();

			} else if (ins.getOpCode().equalsIgnoreCase("writeint")) {

				row = ins.getOpCode() + " " + ins.getSource1();

			} else if (ins.getOpCode().equalsIgnoreCase("readint")) {

				row = ins.getSource1() + " &lt;=" + ins.getOpCode();

			} else if (ins.getOpCode().equalsIgnoreCase("loadI")) {

				row = ins.getSource2() + " &lt;=" + ins.getOpCode() + " " + ins.getSource1();

			} else if (ins.getOpCode().equalsIgnoreCase("i2i")) {

				row = ins.getSource2() + " &lt;=" + ins.getOpCode() + " " + ins.getSource1();

			} else if (ins.getOpCode().equalsIgnoreCase("exit")) {

				row = ins.getOpCode();

			} else if (!ins.isPhiFunction() && destination != null) {
				row = destination + " &lt;=" + ins.getOpCode() + " " + ins.getSource1() + " , " + ins.getSource2();
			} else if (ins.isPhiFunction()) {
				row = destination + " &lt;=" + ins.getOpCode() + " ( " + getPhiparameter(ins) + " )";
			}
			table.append("<tr><td>" + row + "</td> </tr>");

		}
		table.append("</table>>");

		return table.toString();
	}

	private void traversePhiGraph(Block pBlock) {
		pBlock.setVisited(true);
		updateBlockInfo(pBlock);
		for (String succ : pBlock.getSucecessor()) {
			Block block = mBlockMap.get(succ);
			if (!block.isVisited()) {
				traversePhiGraph(block);
			}
		}

	}

	private void updateBlockInfo(Block pBlock) {
		for (Instruction ins : pBlock.getInstructions()) {
			String destination = getDestination(ins);
			String row = "";
			if (ins.getOpCode().equalsIgnoreCase("cbr")) {
				String source1 = valueNumber.get(ins.getSource1());

				ins.setSource1(source1);

			} else if (ins.getOpCode().equalsIgnoreCase("jumpI")) {

			} else if (ins.getOpCode().equalsIgnoreCase("writeint")) {
				String source1 = valueNumber.get(ins.getSource1());

				ins.setSource1(source1);

			} else if (ins.getOpCode().equalsIgnoreCase("readint")) {

				String source1 = valueNumber.get(ins.getSource1());

				ins.setSource1(source1);

			} else if (ins.getOpCode().equalsIgnoreCase("loadI")) {

				// String source1 = valueNumber.get(ins.getSource1());
				String source2 = valueNumber.get(ins.getSource2());

				// ins.setSource1(source1);
				ins.setSource2(source2);

			} else if (ins.getOpCode().equalsIgnoreCase("i2i")) {
				String source1 = valueNumber.get(ins.getSource1());
				String source2 = valueNumber.get(ins.getSource2());

				ins.setSource1(source1);
				ins.setSource2(source2);

			} else if (ins.getOpCode().equalsIgnoreCase("exit")) {

			} else if (!ins.isPhiFunction()) {
				String source1 = valueNumber.get(ins.getSource1());
				String source2 = valueNumber.get(ins.getSource2());
				String dest = valueNumber.get(ins.getDestication());
				ins.setSource1(source1);
				ins.setSource2(source2);
				ins.setDestication(dest);
			} else if (ins.isPhiFunction()) {
				String dest = valueNumber.get(ins.getDestication());

				ins.setDestication(dest);
				for (String key : ins.getParams().keySet()) {
					String string = ins.getParams().get(key);
					String source = valueNumber.get(string);
					ins.getParams().put(key, source);
				}

			}

		}

	}

}
