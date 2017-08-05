package com.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entities.TokenInfo;
import com.scanner.Token;
import com.visitors.Visitor;

public class SimpleExpression extends DummyNode {

	private Term mParseTerm;
	private SimpleExpressionExtended mParseSimpleExpression1;
	private int mCount;
	Map<String,Attribute> mAttributes = new HashMap<String,Attribute>();
	private boolean mError;
	private String mColor;
	private List<Node> mNodes = null;
	private Node mFather;
	private Token mLastToken;
	
	public SimpleExpression(Term pParseTerm, SimpleExpressionExtended pParseSimpleExpression1) {
		mParseTerm = pParseTerm;
		mParseSimpleExpression1 = pParseSimpleExpression1;

	}

	public SimpleExpressionExtended getParseSimpleExpression1() {
		return mParseSimpleExpression1;
	}

	public Term getParseTerm() {
		return mParseTerm;
	}

	

	@Override
	public TokenInfo getTokenValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node buildAST(Node father) {
		mFather = father;
		return this;
	}

	

	@Override
	public List<Node> getChildNodes() {
		
		if (mNodes == null) {
			List<Node> nodes = new ArrayList<Node>();
			List<Node> nodeOP = new ArrayList<Node>();
			if (mParseTerm != null) {
				nodes.add(mParseTerm.buildAST(this));
			}
			if (mParseSimpleExpression1 != null) {
				Operator buildAST = (Operator) mParseSimpleExpression1.buildAST(this);
				List<Node> childNodes = mParseSimpleExpression1.getChildNodes();
				nodes.addAll(childNodes);
				if (buildAST != null) {
					buildAST.setChilds(nodes);
					nodeOP.add(buildAST);
				} else {
					mNodes = nodes;
					return mNodes;
				}
			}
			mNodes = nodeOP;
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
		return mFather;
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
