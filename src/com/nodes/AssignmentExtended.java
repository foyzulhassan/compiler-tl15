package com.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entities.TokenInfo;
import com.scanner.Token;
import com.visitors.Visitor;

public class AssignmentExtended extends DummyNode {

	private TokenInfo mTokenWord;
	private Expression mParseExpression;
	private int mCount;
	Map<String, Attribute> mAttributes = new HashMap<String, Attribute>();
	private boolean mError;
	private String mColor;
	private List<Node> mNodes = null;
	private Node mFather;
	private Token mLastToken;

	public AssignmentExtended(TokenInfo pWord, Expression pParseExpression) {
		mTokenWord = pWord;
		mParseExpression = pParseExpression;
	}

	public TokenInfo getTokenWord() {
		return mTokenWord;
	}

	public Expression getParseExpression() {
		return mParseExpression;
	}

	@Override
	public Node buildAST(Node father) {
		this.mFather = father;
		return this;
	}

	@Override
	public TokenInfo getTokenValue() {
		// TODO Auto-generated method stub
		if (mTokenWord.getWord() == Token.READINT) {
			return mTokenWord;
		} else
			return null;
	}

	@Override
	public List<Node> getChildNodes() {
		if (mNodes == null) {
			List<Node> nodes = new ArrayList<Node>();
			if (mParseExpression != null) {
				nodes.add(mParseExpression.buildAST(this));
			} else if (mTokenWord != null && mTokenWord.getWord() != Token.READINT) {
				nodes.add(new Operator(mTokenWord));
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
