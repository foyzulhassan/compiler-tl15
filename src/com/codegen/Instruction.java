package com.codegen;

import java.util.Map;
import java.util.TreeMap;

public class Instruction{
	public String opCode;
	String source1;
	String source2;
	public String destication;
	public boolean isPhiFunction = false;
	boolean source1Updated = false;
	boolean source2Updated = false;
	public int indexDest=0;
	String source1Block;
	String source2Block;
	Map<String, String> params = new TreeMap<String, String>();
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String pOpCode) {
		opCode = pOpCode;
	}
	public String getSource1() {
		return source1;
	}
	public void setSource1(String pSource1) {
		source1 = pSource1;
	}
	public String getSource2() {
		return source2;
	}
	public void setSource2(String pSource2) {
		source2 = pSource2;
	}
	public String getDestication() {
		return destication;
	}
	public void setDestication(String pDestication) {
		destication = pDestication;
	}
	
	public boolean isPhiFunction() {
		return isPhiFunction;
	}
	
	public void setPhiFunction(boolean pIsPhiFunction) {
		isPhiFunction = pIsPhiFunction;
	}
	
	public void setSource1Updated(boolean pSource1Updated) {
		source1Updated = pSource1Updated;
	}
	
	public boolean isSource1Updated() {
		return source1Updated;
	}
	
	public void setSource2Updated(boolean pSource2Updated) {
		source2Updated = pSource2Updated;
	}
	
	public boolean isSource2Updated() {
		return source2Updated;
	}
	
	public String getSource1Block() {
		return source1Block;
	}
	
	public String getSource2Block() {
		return source2Block;
	}
	
	public void setSource1Block(String pSource1Block) {
		source1Block = pSource1Block;
	}
	
	public void setSource2Block(String pSource2Block) {
		source2Block = pSource2Block;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
}
