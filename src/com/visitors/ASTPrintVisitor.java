package com.visitors;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.nodes.Assignment;
import com.nodes.AssignmentExtended;
import com.nodes.Attribute;
import com.nodes.Declaration;
import com.nodes.DummyNode;
import com.nodes.ElseClause;
import com.nodes.Expression;
import com.nodes.ExpressionExtended;
import com.nodes.Factor;
import com.nodes.IfStatement;
import com.nodes.Node;
import com.nodes.NodeLabelHelper;
import com.nodes.Operator;
import com.nodes.Program;
import com.nodes.SimpleExpression;
import com.nodes.SimpleExpressionExtended;
import com.nodes.Statement;
import com.nodes.StatementSequence;
import com.nodes.Term;
import com.nodes.TermExtended;
import com.nodes.Type;
import com.nodes.Writeint;
import com.scanner.Token;
import com.utils.NodeInfoUtil;

public class ASTPrintVisitor implements Visitor{
	private StringBuffer pBuffer;
	
	
	public ASTPrintVisitor()
	{
		pBuffer = new StringBuffer();
		pBuffer.append("digraph parseTree { \n" + " ordering=out; ");
	}
	
	@Override
	public void visit(Node node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}
	
	public StringBuffer generateAST(Program parsetree)
	{
		visit(parsetree);
		pBuffer.append("\n" + "}");
		
		return pBuffer;
	}

	@Override
	public void visit(Assignment node) {
		printCommonNodes(node);
	}

	@Override
	public void visit(AssignmentExtended node) {
		
		printCommonNodes(node);
	}

	@Override
	public void visit(Attribute node) {		
		//printCommonNodes(node);
		
	}

	@Override
	public void visit(Declaration node) {
		String program = "decl list";
		if (node.getChildNodes().size() > 0)
		{
			if(node instanceof Declaration && node.getASTParentNode() instanceof Declaration)
			{
				//node.setCount(father.getCount());
			}
			else
			{
				NodeInfoUtil.buildASTview(node.getASTParentNode().getCount(), node.getCount(), pBuffer, program, "white");
			}
		}
		
		List<Node> childrenNodes = node.getASTChildNodes();
		
		for (Node anode : childrenNodes) {
			if(anode!=null)
				anode.accept(this);
		}
	}

	@Override
	public void visit(ElseClause node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(Expression node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(ExpressionExtended node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(Factor node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(IfStatement node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(Operator node) {		
		
		printCommonNodes(node);		
	
	}

	@Override
	public void visit(Program pNode) {
		
		String program = "program";
		String color = "/x11/lightgrey";
		if (pNode.hasError()) {
			color = "/pastel13/1";
		}
		NodeInfoUtil.buildASTview(0, pNode.getCount(), pBuffer, program, color);
		
		List<Node> childrenNodes = pNode.getASTChildNodes();
		
		for (Node node : childrenNodes) {
			if(node!=null)
				node.accept(this);
		}
		
	}

	@Override
	public void visit(SimpleExpression node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(SimpleExpressionExtended node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(Statement node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(StatementSequence node) {
		String program = "stmt list";
		if (node.getChildNodes().size() > 0)
		{
			
			if(node instanceof StatementSequence && node.getASTParentNode() instanceof StatementSequence)
			{
				//node.setCount(father.getCount());
			}
			else
			{
				NodeInfoUtil.buildASTview(node.getASTParentNode().getCount(), node.getCount(), pBuffer, program, "white");
			}
		}
		
		List<Node> childrenNodes = node.getASTChildNodes();
		
		for (Node anode : childrenNodes) {
			if(anode!=null)
				anode.accept(this);
		}
		
	}

	@Override
	public void visit(Term node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(TermExtended node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(Type node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}

	@Override
	public void visit(Writeint node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}
	
	

	@Override
	public void visit(DummyNode node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
	}
	
	private void printCommonNodes(Node node)
	{
		String label = NodeLabelHelper.Label.replace("#", "" + node.getCount());

		if (node.hasError()) {
			label = label.replace("$", "/pastel13/1");

		} else {

			label = label.replace("$", node.getColor());

		}
		
		if(node.getASTParentNode() instanceof Declaration){
			
			Attribute temp1 = node.getAttributes().get("type");
			
			if(temp1!=null && !node.hasError())
			{
				label = label.replace("@", "decl:"+NodeInfoUtil.getTokenValue(node)+":"+temp1.getValue().getValue());
				
			}
			else
			{
				label = label.replace("@", "decl:"+NodeInfoUtil.getTokenValue(node));
			}				
			
		}
		else {
			Attribute temp1 = node.getAttributes().get("type");
			//Token temp2=node.getLastChildType();
			Token tokentype=node.getTokenValue().getWord();
			if (temp1 != null && !node.hasError()) {
				
				if(tokentype!=null && tokentype!=Token.COMPARE && tokentype!=Token.ASGN && tokentype!=Token.ADDITIVE && tokentype!=Token.MULTIPLICATIVE)
					label = label.replace("@", NodeInfoUtil.getTokenValue(node) + ":" +temp1.getValue().getValue());
				else
					label = label.replace("@", NodeInfoUtil.getTokenValue(node));

			} else {
				label = label.replace("@", NodeInfoUtil.getTokenValue(node));
			}
		}
		//label = label.replace("@", getTokenValue(node));
		Token currentToekn=node.getTokenValue().getWord() ;
		if (node.getTokenValue().getWord() != Token.INT && currentToekn != Token.BOOL && currentToekn != Token.LP
				&& currentToekn != Token.RP) {
			pBuffer.append(label);
			pBuffer.append("\n");

			pBuffer.append("n" + node.getASTParentNode().getCount() + " -> " + "n" + node.getCount());

			pBuffer.append("\n");
		}
		
		List<Node> childrenNodes = node.getASTChildNodes();		
		for (Node anode : childrenNodes) {
			if(anode!=null)
				anode.accept(this);
		}
	}	
	
	public StringBuffer getASTPrintText()
	{
		return pBuffer;
	}	
}
