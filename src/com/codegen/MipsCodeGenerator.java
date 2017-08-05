package com.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.block.Block;

public class MipsCodeGenerator {
	Map<String, Block> mBlockMap = null;
	Map<String, Integer> varStackP = new HashMap<String, Integer>();
	int fp = 0;
	int labelCounter = 0;
	boolean mFlag = false;

	public String generateCode(Map<String, Block> pBlockMap, boolean flag) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(".data" + "\n");
		buffer.append("newline:	.asciiz \"\\n\"" + " \n");
		buffer.append(".text " + "\n");
		buffer.append(".globl main " + "\n");
		buffer.append("main: " + "\n" + " li $fp, 0x7ffffffc " + "\n");

		mBlockMap = pBlockMap;
		mFlag = flag;
		traverseBlock(mBlockMap.get("B0"), buffer, flag);
		return buffer.toString();
	}

	private void traverseBlock(Block pBlock, StringBuffer buffer, boolean flag) {
		if (pBlock == null) {
			return;
		}
		List<Instruction> insList = null;
		if (flag) {
			insList = getInstructionList(pBlock);
		} else {
			insList = pBlock.getInstructions();
		}

		StringBuffer code = new StringBuffer();
		if(flag){
		code.append(pBlock.getAssemblyLabel() + " :");
		}else{
			code.append(pBlock.getLabel() + " :");	
		}
		code.append("\n");

		for (Instruction instruction : insList) {

			code.append(produceAssembly(instruction));

		}

		buffer.append(code.toString());
		buffer.append("\n");
		buffer.append("\n");
		pBlock.setVisited(true);
		List<String> children = pBlock.getChildren();
		for (String string : children) {
			Block block = mBlockMap.get(string);

			if (block != null && !block.isVisited())
				traverseBlock(block, buffer, flag);

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

	private String produceAssembly(Instruction instruction) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n");
		switch (instruction.getOpCode()) {
		case "loadI":
			int local = 0;
			String value = instruction.getSource1();
			if (instruction.getSource1().equalsIgnoreCase("true")) {
				value = "1";
			}

			if (instruction.getSource1().equalsIgnoreCase("false")) {
				value = "0";
			}
			buffer.append("li $t0, " + value);
			local = getOffsetValue(instruction, 2);
			buffer.append("\n");
			buffer.append("sw $t0, " + local + "($fp)");
			break;
		case "add":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			local = getOffsetValue(instruction, 2);
			buffer.append("lw $t2, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("addu $t0, $t1, $t2");
			buffer.append("\n");
			local = getOffsetValue(instruction, 3);
			buffer.append("sw $t0, " + local + "($fp)");

			break;

		case "sub":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			local = getOffsetValue(instruction, 2);
			buffer.append("lw $t2, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("subu $t0, $t1, $t2");
			buffer.append("\n");
			local = getOffsetValue(instruction, 3);
			buffer.append("sw $t0, " + local + "($fp)");

			break;

		case "div":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			local = getOffsetValue(instruction, 2);
			buffer.append("lw $t2, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("div $t0, $t1, $t2");
			buffer.append("\n");
			local = getOffsetValue(instruction, 3);
			buffer.append("sw $t0, " + local + "($fp)");
			break;

		case "mult":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			local = getOffsetValue(instruction, 2);
			buffer.append("lw $t2, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("mul $t0, $t1, $t2");
			buffer.append("\n");
			local = getOffsetValue(instruction, 3);
			buffer.append("sw $t0, " + local + "($fp)");

			break;

		case "cmp_GT":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			local = getOffsetValue(instruction, 2);
			buffer.append("lw $t2, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("sgt $t0, $t1, $t2");
			buffer.append("\n");
			local = getOffsetValue(instruction, 3);
			buffer.append("sw $t0, " + local + "($fp)");

			break;

		case "cmp_LT":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			local = getOffsetValue(instruction, 2);
			buffer.append("lw $t2, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("slt $t0, $t1, $t2");
			buffer.append("\n");
			local = getOffsetValue(instruction, 3);
			buffer.append("sw $t0, " + local + "($fp)");

			break;

		case "cmp_GE":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			local = getOffsetValue(instruction, 2);
			buffer.append("lw $t2, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("sge $t0, $t1, $t2");
			buffer.append("\n");
			local = getOffsetValue(instruction, 3);
			buffer.append("sw $t0, " + local + "($fp)");

			break;

		case "cmp_LE":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			local = getOffsetValue(instruction, 2);
			buffer.append("lw $t2, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("sle $t0, $t1, $t2");
			buffer.append("\n");
			local = getOffsetValue(instruction, 3);
			buffer.append("sw $t0, " + local + "($fp)");

			break;
		case "i2i":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("add $t0, $t1, $zero");
			buffer.append("\n");
			Integer temp = getfpValue(instruction, 2);
			
			buffer.append("sw $t0, " + temp + "($fp)");
			break;
		case "cbr":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t0, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("bne $t0, $zero," + instruction.getSource2());
			buffer.append("\n");
			buffer.append("L" + labelCounter + ":");
			labelCounter += 1;
			buffer.append("\n");
			buffer.append("j " + instruction.getDestication());

			break;

		case "cmp_EQ":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			local = getOffsetValue(instruction, 2);
			buffer.append("lw $t2, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("seq $t0, $t1, $t2");
			buffer.append("\n");
			local = getOffsetValue(instruction, 3);
			buffer.append("sw $t0, " + local + "($fp)");

			break;

		case "cmp_NE":
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			local = getOffsetValue(instruction, 2);
			buffer.append("lw $t2, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("sne $t0, $t1, $t2");
			buffer.append("\n");
			local = getOffsetValue(instruction, 3);
			buffer.append("sw $t0, " + local + "($fp)");

			break;

		case "jumpI":

			buffer.append("j " + instruction.getSource1());
			break;
		case "readint":
			buffer.append("li $v0, 5");
			buffer.append("\n");
			buffer.append("syscall");
			buffer.append("\n");
			buffer.append("add $t0, $v0, $zero");
			buffer.append("\n");

			buffer.append("sw $t0, " + getfpValue(instruction,1) + "($fp)");

			break;
		case "writeint":
			buffer.append("li $v0, 1");
			buffer.append("\n");
			local = getOffsetValue(instruction, 1);
			buffer.append("lw $t1, " + local + "($fp)");
			buffer.append("\n");
			buffer.append("add $a0, $t1, $zero");
			buffer.append("\n");
			buffer.append("syscall");
			buffer.append("\n");
			buffer.append("li $v0, 4");
			buffer.append("\n");
			buffer.append("la $a0, newline");
			buffer.append("\n");
			buffer.append("syscall");

			break;

		case "exit":
			buffer.append("li $v0, 10");
			buffer.append("\n");
			buffer.append("syscall");
			break;
		default:
			break;
		}

		return buffer.toString();
	}

	private Integer getfpValue(Instruction instruction, int num) {
		if (mFlag) {
			if (num == 1) {
				return varStackP.get(instruction.getSource1());
			} else if (num == 2) {
				return varStackP.get(instruction.getSource2());
			}
		} else {
			if (num == 1) {
				return getNumber(instruction.getSource1());
			} else if (num == 2) {
				return getNumber(instruction.getSource2());
			}
		}
		return 0;
	}
	
	

	private int getOffsetValue(Instruction instruction, int num) {
		int local = 0;
		if (mFlag) {
			if (num == 1) {
				if (isVariable(instruction.getSource1())) {
					local = varStackP.get(instruction.getSource1());
				} else {
					String[] res = instruction.getSource1().split("_");
					local = fp - Integer.valueOf(res[1]) * 4;
				}
			} else if (num == 2) {
				if (instruction.getOpCode().equalsIgnoreCase("loadI")) {
					if (isVariable(instruction.getSource2())) {
						Integer temp = varStackP.get(instruction.getSource2());
						if (temp == null) {

							local = fp;
							fp = fp - 4;
							varStackP.put(instruction.getSource2(), local);
						}
					} else {
						String[] res = instruction.getSource2().split("_");
						local = fp - Integer.valueOf(res[1]) * 4;
					}
				} else {
					if (isVariable(instruction.getSource2())) {
						local = varStackP.get(instruction.getSource2());
					} else {
						String[] res = instruction.getSource2().split("_");
						local = fp - Integer.valueOf(res[1]) * 4;
					}
				}
			} else if (num == 3) {
				if (isVariable(instruction.getDestication())) {
					local = varStackP.get(instruction.getDestication());
				} else {
					String[] res = instruction.getDestication().split("_");
					local = fp - Integer.valueOf(res[1]) * 4;
				}
			}
		}else{
			if(num==1){
				local = getNumber(instruction.getSource1());
			}else if(num==2){
				local = getNumber(instruction.getSource2());
			}else if(num==3){
				local = getNumber(instruction.getDestication());
			}
		}
		return local;
	}

	private boolean isVariable(String pSource2) {
		boolean flag = false;
		String[] spString = pSource2.split("_");
		if (spString[0].equalsIgnoreCase("r") && spString.length > 1) {

			if (Character.isAlphabetic(spString[1].codePointAt(0))) {
				flag = true;
			}
		}

		return flag;

	}
	
	private int getNumber(String pSource) {
		
		Integer integer = varStackP.get(pSource);
		if(integer==null || integer==0){
			fp = fp -4;
			varStackP.put(pSource,fp);
			integer = fp;
		}
		
		return integer;
	}

}
