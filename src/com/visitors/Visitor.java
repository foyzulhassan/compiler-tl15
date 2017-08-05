package com.visitors;

import com.nodes.*;

public interface Visitor {
	public void visit(Assignment node);
	public void visit(AssignmentExtended node);
	public void visit(Attribute node);
	public void visit(Declaration node);
	public void visit(ElseClause node);
	public void visit(Expression node);
	public void visit(ExpressionExtended node);
	public void visit(Factor node);
	public void visit(IfStatement node);
	public void visit(Operator node);	
	public void visit(Program node);
	public void visit(SimpleExpression node);
	public void visit(SimpleExpressionExtended node);
	public void visit(Statement node);
	public void visit(StatementSequence node);	
	public void visit(Term node);
	public void visit(TermExtended node);
	public void visit(Type node);
	public void visit(Writeint node);
	public void visit(DummyNode node);
	public void visit(Node node);

}
