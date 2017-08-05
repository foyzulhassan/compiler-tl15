package com.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entities.TokenInfo;
import com.scanner.Token;
import com.visitors.Visitor;

public class Declaration extends DummyNode {
	private TokenInfo mTokenVar;
	private TokenInfo mTokenIdent;
	private TokenInfo mTokenAs;
	private Type mType;
	private TokenInfo mTokenSc;
	private Declaration mDeclaration;
	private int mCount;
	Map<String, Attribute> mAttributes = new HashMap<String, Attribute>();
	private boolean mError;
	private String mColor;
	private List<Node> mNodes = null;
	private Node father = null;
	private Token mLastToken;

	public Declaration(TokenInfo pTokenVar, TokenInfo pTokenID, TokenInfo pTokenAs, Type pParseType, TokenInfo pTokenSc,
			Declaration pDeclaration) {
		mTokenVar = pTokenVar;
		mTokenIdent = pTokenID;
		mTokenAs = pTokenAs;
		mType = pParseType;
		mTokenSc = pTokenSc;
		mDeclaration = pDeclaration;

	}

	public TokenInfo getTokenVar() {
		return mTokenVar;
	}

	public TokenInfo getTokenIdent() {
		return mTokenIdent;
	}

	public TokenInfo getTokenAs() {
		return mTokenAs;
	}

	public Type getType() {
		return mType;
	}

	public TokenInfo getTokenSc() {
		return mTokenSc;
	}

	public Declaration getDeclaration() {
		return mDeclaration;
	}

	@Override
	public TokenInfo getTokenValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node buildAST(Node father) {
		this.father = father;
		return this;
	}

	@Override
	public List<Node> getChildNodes() {
		if (mNodes == null) {
			List<Node> nodes = new ArrayList<Node>();
			List<Node> nodeType = new ArrayList<Node>();

			if (mTokenIdent != null) {
				Operator nodeVar = new Operator(mTokenIdent) {
					@Override
					public boolean isDeclaration() {
						// TODO Auto-generated method stub
						return true;
					}
				};
				nodeType.add(mType.buildAST(nodeVar));
				nodeVar.setChilds(nodeType);
				nodes.add(nodeVar.buildAST(this));
			}

			if (mDeclaration != null) {
				nodes.add(mDeclaration.buildAST(this));
			}
			mNodes = nodes;
		}
		return mNodes;// TODO Auto-generated method stub
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
		return true;
	}

	@Override
	public Node getFather() {
		// TODO Auto-generated method stub
		return father;
	}

	@Override
	public Token getLastChildType() {
		// TODO Auto-generated method stub
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
