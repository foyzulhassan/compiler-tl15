package com.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entities.TokenInfo;
import com.scanner.Token;
import com.visitors.Visitor;


public class Assignment extends DummyNode {

	private TokenInfo mIdent;
	private TokenInfo mAsgn;
	private AssignmentExtended mParseAssignment1;
	int mCount =0;
	boolean isError = false;
	Map<String,Attribute> mAttributes = new HashMap<String,Attribute>();
	private boolean mError;
	private String mColor;
	private Node mNode =null;
	private List<Node> mNodes = null;
	private Operator mFather;	
	private Token mLastToken;
	
		
	public Assignment(TokenInfo pIdent, TokenInfo pAsgn, AssignmentExtended pParseAssignment1) {
		mIdent = pIdent;
		mAsgn = pAsgn;
		mParseAssignment1 = pParseAssignment1;
	}

	public TokenInfo getAsgn() {
		return mAsgn;
	}

	public TokenInfo getIdent() {
		return mIdent;
	}

	public AssignmentExtended getParseAssignment1() {
		return mParseAssignment1;
	}

	
	@Override
	public TokenInfo getTokenValue() {
		
		return null;
	}

	@Override
	public Node buildAST(Node father) {
        if(mNode==null){
		Operator node = new Operator(mAsgn);
		this.mFather = node;
		node.setChilds(getChildNodes());
		mNode = node.buildAST(father);
	   }

		return mNode;
	}


	@Override
	public List<Node> getChildNodes() {
		if(mNodes==null){
		List<Node> nodes = new ArrayList<Node>();
		Operator iden = new Operator(mIdent);
		nodes.add(iden.buildAST(mFather));
		if (mParseAssignment1 != null) {
			Node buildAST = mParseAssignment1.buildAST(mFather);
			 nodes.add(buildAST);
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
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
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
}
