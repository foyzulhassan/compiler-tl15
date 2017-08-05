package com.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.codegen.Instruction;
import com.scanner.Token;

public class Block {
	String mBlock = "";
	String mLabel = "B";
	List<String> mChildren = new ArrayList<String>();
	List<String> mPredecessor = new ArrayList<String>();
	List<String> mSucecessor = new ArrayList<String>();
	List<String> mdominance = new ArrayList<String>();
	List<String> mDominanceFrontier =new ArrayList<String>();
	List<String> mDomChild =new ArrayList<String>();
	List<Instruction> mInstructions = new ArrayList<Instruction>();
	Map<String, Instruction> insertedPhi = new HashMap<String, Instruction>();
	Map<String, String> locals = new HashMap<String, String>();
	int mNumber = 0;
	boolean visited = false;
	Token mToken = null;
	boolean isProcess = false;
	boolean isWithElse = false;
	private String assemblyLabel="";
	public boolean isActive = false;
	public boolean isLoopHead = false;
	public boolean isLoopTail = false;
	public boolean isEntryBlock = false;
	public int ref =0;
	public int fwdBranch =0;
	public int bwdBranch =0;
	public Block(int number){
	 mNumber = number;	
	}
	public String getBlock() {
		return mBlock;
	}

	public void setBlock(String pBlock) {
		mBlock = pBlock;
	}

	public String getLabel() {
		return mLabel+mNumber;
	}

	public void setLabel(String pLabel) {
		mLabel = pLabel;
	}
	
	public int getNumber() {
		return mNumber;
	}
	
	public void setNumber(int pNumber) {
		mNumber = pNumber;
	}
	public void setChildren(String pChildren) {
		mChildren.add(pChildren);
	}

	public List<String> getChildren() {
		return mChildren;
	}
	
	public void setVisited(boolean pVisited) {
		visited = pVisited;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setToken(Token pToken) {
		mToken = pToken;
	}
	
	public Token getToken() {
		return mToken;
	}
	
	public void setProcess(boolean pIsProcess) {
		isProcess = pIsProcess;
	}
	
	public boolean isProcess() {
		return isProcess;
	}
	
	public void setWithElse(boolean pIsWithoutElse) {
		isWithElse = pIsWithoutElse;
	}
	
	public boolean isWithElse() {
		return isWithElse;
	}

	public String getAssemblyLabel() {
		return mLabel;
	}
	
	public List<String> getPredecessor() {
		return mPredecessor;
	}
	
	public List<String> getSucecessor() {
		return mSucecessor;
	}
	
	public void addPredecessor(String pPredecessor) {
		mPredecessor.add(pPredecessor);
	}
	
	public void addSucecessor(String pSuccessor) {
		mSucecessor.add(pSuccessor);
	}
	
	public List<String> getDominance() {
		return mdominance;
	}
	
	public void addDominance(String pDominance){
		mdominance.add(pDominance);
	}
	public List<String> getDominanceFrontier() {
		return mDominanceFrontier;
	}
	
	public void addDominanceFrontier(String pDominanceFrontier) {
		mDominanceFrontier.add(pDominanceFrontier);
	}
	
	public List<Instruction> getInstructions() {
		return mInstructions;
	}
	
	public void setInstructions(List<Instruction> pInstructions) {
		mInstructions = pInstructions;
	}
	
	public List<String> getDomChild() {
		return mDomChild;
	}
	
	public void addDomChild(String pDomChild) {
		mDomChild.add(pDomChild);
	}
	
	public Map<String, Instruction> getInsertedPhi() {
		return insertedPhi;
	}
	
	public Map<String, String> getLocals() {
		return locals;
	}
}
