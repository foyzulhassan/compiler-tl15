package com.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GlobalName {

	List<String> blocks = new ArrayList<String>();
	int mCounter =0;
	Stack<String> mStack = new Stack<String>();
	public List<String> getBlocks() {
		return blocks;
	}
	public void setBlocks(List<String> pBlocks) {
		blocks = pBlocks;
	}
	public int getCounter() {
		return mCounter;
	}
	public void setCounter(int pCounter) {
		mCounter = pCounter;
	}
	public Stack<String> getStack() {
		return mStack;
	}
	public void setStack(Stack<String> pStack) {
		mStack = pStack;
	}
	
	public void incrementCount(){
		mCounter++;
	}
	
	
	
}
