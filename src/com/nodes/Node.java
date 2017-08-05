package com.nodes;

import java.util.List;
import java.util.Map;

import com.entities.TokenInfo;
import com.scanner.Token;
import com.visitors.Visitor;


public interface Node {

	public TokenInfo getTokenValue();
	public List<Node> getChildNodes();
	public Node buildAST(Node father);
	public String getColor();
	public void setColor(String pColor);
	public boolean hasError();
	public void setError(boolean pError);
	public int getCount();
	public void setCount(int count);
	public Map<String, Attribute> getAttributes();
	public void setAttribute(String key, Attribute pAttribute);
	public boolean isDeclaration();
	public Node getFather();	
	public Token getLastChildType();
	public void setLastChildType(Token token);
	
	public void setPasrserParent(Node parent);
	public Node getParserParent();
	
	public void addASTChildNode(Node child);
	public List<Node> getASTChildNodes();
	public void setASTParentNode(Node parent);
	public Node getASTParentNode();
	
	public void accept(Visitor visitor);
}
