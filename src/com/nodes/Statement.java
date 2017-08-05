package com.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entities.TokenInfo;
import com.scanner.Token;
import com.visitors.Visitor;

public class Statement extends DummyNode {

	private Assignment mParseAssignment;
	private IfStatement mParseIfStatement;
	private WhileStatement mParseWhileStatement;
	private Writeint mParseWriteExpression;
	private int mCount;
	Map<String, Attribute> mAttributes = new HashMap<String, Attribute>();
	private boolean mError;
	private String mColor;
	private List<Node> mNodes = null;
	private Node mFather;
	private Token mLastToken;

	public Statement(Assignment pParseAssignment, IfStatement pParseIfStatement, WhileStatement pParseWhileStatement,
			Writeint pParseWriteExpression) {
		mParseAssignment = pParseAssignment;
		mParseIfStatement = pParseIfStatement;
		mParseWhileStatement = pParseWhileStatement;
		mParseWriteExpression = pParseWriteExpression;

	}

	public Assignment getParseAssignment() {
		return mParseAssignment;
	}

	public IfStatement getParseIfStatement() {
		return mParseIfStatement;
	}

	public WhileStatement getParseWhileStatement() {
		return mParseWhileStatement;
	}

	public Writeint getParseWriteExpression() {
		return mParseWriteExpression;
	}

	@Override
	public Node buildAST(Node father) {
		mFather = father;
		return this;
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
			if (mParseAssignment != null) {
				nodes.add(mParseAssignment.buildAST(this));
			} else if (mParseIfStatement != null) {
				nodes.add(mParseIfStatement.buildAST(this));
			} else if (mParseWhileStatement != null) {
				nodes.add(mParseWhileStatement.buildAST(this));
			} else if (mParseWriteExpression != null) {
				nodes.add(mParseWriteExpression.buildAST(this));
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
