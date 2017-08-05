package com.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entities.TokenInfo;
import com.scanner.Token;
import com.visitors.Visitor;

public class StatementSequence extends DummyNode {

	private Statement mStatement;
	private TokenInfo mSc;
	private StatementSequence mParseStatementSequences;
	private int mCount;
	Map<String, Attribute> mAttributes = new HashMap<String, Attribute>();
	private boolean mError;
	private String mColor;
	private List<Node> mNodes = null;
	private Node mFather;
	private Token mLastToken;

	public StatementSequence(Statement pStatement, TokenInfo pSc, StatementSequence pParseStatementSequences) {
		mStatement = pStatement;
		mSc = pSc;
		mParseStatementSequences = pParseStatementSequences;

	}

	public StatementSequence getParseStatementSequences() {
		return mParseStatementSequences;
	}

	public TokenInfo getSc() {
		return mSc;
	}

	public Statement getStatement() {
		return mStatement;
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
			if (mStatement != null) {
				nodes.add(mStatement.buildAST(this));
			}
			if (mParseStatementSequences != null) {
				nodes.add(mParseStatementSequences.buildAST(this));
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
