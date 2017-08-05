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

public class ASTStaticCheckVisitor implements Visitor{
	
	private HashMap<String, String> typeMap = new HashMap<String,String>();
	private HashMap<String, Boolean> initmap = new HashMap<String,Boolean>();
	
	public ASTStaticCheckVisitor()
	{
		typeMap = new HashMap<String,String>();
		
	}
	
	public void checkStaticErrors(Program parsetree)
	{
		visit(parsetree);
	}
	
	@Override
	public void visit(Node node) {
		// TODO Auto-generated method stub
		printCommonNodes(node);
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
		// TODO Auto-generated method stub	
		
		if(Token.getBinaryOperation().contains(node.getTokenValue().getWord()))
		{
			Stack<Node> mStack = new Stack<Node>();
			List<Node> childlist=node.getASTChildNodes();
			
			for(int i=0;i<childlist.size();i++)
			{
				mStack.push(childlist.get(i));
			}
			
			Node rightnode=null;
			Node leftnode=null;
			
			if(!mStack.isEmpty())
				rightnode=mStack.pop();
			if(!mStack.isEmpty())
				leftnode=mStack.pop();
			
			if(rightnode!=null && rightnode.getTokenValue()!=null && rightnode.getTokenValue().getWord()==Token.ident)
			{
				if(initmap.get(rightnode.getTokenValue().getWordValue())==null)
				{
					///initmap.put(leftnode.getTokenValue().getWordValue(), true);
					String msg=" ";
					msg=msg.format("[Semantic Error] at Line %d[Variable %s not initialized]",node.getTokenValue().getLineNumber(),rightnode.getTokenValue().getWordValue());
					System.out.println(msg);
					node.setError(true);
				}
			}
			
			if(leftnode!=null && leftnode.getTokenValue()!=null && leftnode.getTokenValue().getWord()==Token.ident)
			{
				if(initmap.get(leftnode.getTokenValue().getWordValue())==null)
				{
					String msg= " ";			
					msg=msg.format("[Semantic Error] at Line %d [Variable %s not initialized]",node.getTokenValue().getLineNumber(),leftnode.getTokenValue().getWordValue());
					System.out.println(msg);
					node.setError(true);
				}
			}
		}
		
		printCommonNodes(node);
		
		if(node.getTokenValue()!=null)
		{			
			if(node.getTokenValue().getWord()==Token.ASGN)
			{
				Stack<Node> mStack = new Stack<Node>();
				List<Node> childlist=node.getASTChildNodes();
				
				for(int i=0;i<childlist.size();i++)
				{
					mStack.push(childlist.get(i));
				}
				
				Node rightnode=mStack.pop();
				Node leftnode=mStack.pop();
				
				if(leftnode.getTokenValue()!=null && leftnode.getTokenValue().getWord()==Token.ident)
				{
					if(initmap.get(leftnode.getTokenValue().getWordValue())==null)
					{
						initmap.put(leftnode.getTokenValue().getWordValue(), true);
					}
				}
			}
		}
	}

	@Override
	public void visit(Program pNode) {
		
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
		List<Node> childrenNodes = node.getASTChildNodes();		
		for (Node anode : childrenNodes) {
			if(anode!=null)
				anode.accept(this);
		}
	}

}
