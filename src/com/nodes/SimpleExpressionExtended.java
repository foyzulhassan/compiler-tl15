package com.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entities.TokenInfo;
import com.scanner.Token;
import com.visitors.Visitor;

public class SimpleExpressionExtended extends DummyNode{

	private TokenInfo mToken;
	private Term mParseTerm;
	private int mCount;
	Map<String,Attribute> mAttributes = new HashMap<String,Attribute>();
	private boolean mError;
	private String mColor;
	private Node mNode =null;
	private List<Node> mNodes = null;
	private Node mFather;
	private Token mLastToken;

	public SimpleExpressionExtended(TokenInfo pWord, Term pParseTerm) {
		mToken = pWord;
		mParseTerm = pParseTerm;
		
		
	}
	
	public Term getParseTerm() {
		return mParseTerm;
	}
	
	public TokenInfo getToken() {
		return mToken;
	}
	
		
	@Override
	public Node buildAST(Node father) {

		if (mToken != null && mNode == null) {

			Operator node = new Operator(mToken);
			this.mFather = node;
			node.setChilds(getChildNodes());
			mNode = node.buildAST(father);
		}

		return mNode;
	}
	
	@Override
	public TokenInfo getTokenValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public List<Node> getChildNodes() {
		if (mNodes == null) {
			List<Node> nodes = new ArrayList<Node>();
			if (mParseTerm != null) {
				nodes.add(mParseTerm.buildAST(this.mFather));
			}
			mNodes = nodes;
		}
		return mNodes;
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
		// TODO Auto-generated method stub
		return mAttributes;
	}

	@Override
	public void setAttribute(String key, Attribute pAttribute) {
		mAttributes.put(key,pAttribute);
		
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Token getLastChildType() {
		// TODO Auto-generated method stub
		return mLastToken;
	}

	@Override
	public void setLastChildType(Token token) {
		mLastToken=token;
		
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
	}

}
