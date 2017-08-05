package com.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entities.TokenInfo;
import com.scanner.Token;
import com.visitors.Visitor;

public class Operator extends DummyNode {

	TokenInfo mTokenWord;
	List<Node> mChilds = new ArrayList<Node>();
	private int mCount;
	Map<String, Attribute> mAttributes = new HashMap<String, Attribute>();
	private String mColor;
	private boolean mError;
	private Node mFather = null;
	private Token mLastToken;

	public Operator(TokenInfo pTokenWord) {
		
		mTokenWord = pTokenWord;
	}

	@Override
	public TokenInfo getTokenValue() {
		
		return mTokenWord;
	}

	@Override
	public Node buildAST(Node fahter) {
		
		mFather = fahter;
		return this;
	}

	@Override
	public List<Node> getChildNodes() {
		
		return mChilds;
	}

	public void setChilds(List<Node> pChilds) {
		mChilds = pChilds;
	}

	@Override
	public int getCount() {

		return mCount;
	}

	@Override
	public void setCount(int pCount) {
		mCount = pCount;

	}

	@Override
	public Map<String, Attribute> getAttributes() {
		
		return mAttributes;
	}

	@Override
	public void setAttribute(String key, Attribute pAttribute) {
		mAttributes.put(key, pAttribute);

	}

	@Override
	public String getColor() {
		return mColor;
	}

	@Override
	public void setColor(String pColor) {
		mColor = pColor;

	}

	@Override
	public void setError(boolean pError) {
		mError = pError;

	}

	@Override
	public boolean hasError() {
		return mError;
	}

	@Override
	public boolean isDeclaration() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Node getFather() {
		
		return mFather;
	}

	@Override
	public Token getLastChildType() {
		
		return mLastToken;
	}

	@Override
	public void setLastChildType(Token token) {
		mLastToken = token;

	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);

	}
}
