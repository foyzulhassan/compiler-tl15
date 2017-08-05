package com.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.entities.TokenInfo;
import com.scanner.Token;
import com.visitors.Visitor;

public class DummyNode implements Node {

	private List<Node> mASTChildNodes = new ArrayList<Node>();
	private Node mASTFather = null;
	private Node mParserParent = null;

	@Override
	public TokenInfo getTokenValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Node> getChildNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node buildAST(Node father) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setColor(String pColor) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasError() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setError(boolean pError) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCount(int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Attribute> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String key, Attribute pAttribute) {
		// TODO Auto-generated method stub

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
		return null;
	}

	@Override
	public void setLastChildType(Token token) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addASTChildNode(Node child) {
		mASTChildNodes.add(child);

	}

	@Override
	public List<Node> getASTChildNodes() {
		return mASTChildNodes;
	}

	@Override
	public void setASTParentNode(Node parent) {
		mASTFather = parent;

	}

	@Override
	public Node getASTParentNode() {
		return mASTFather;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void setPasrserParent(Node parent) {
		mParserParent = parent;

	}

	@Override
	public Node getParserParent() {
		// TODO Auto-generated method stub
		return mParserParent;
	}

}
