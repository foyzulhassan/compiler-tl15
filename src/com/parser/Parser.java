package com.parser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.entities.TokenInfo;
import com.nodes.*;
import com.scanner.Token;
import com.scanner.TokenMngr;
import com.utils.ConsoleLogger;

public class Parser {

	TokenMngr mTokenManager;

	private boolean isError = false;

	private boolean isRecovered = true;

	// ctor
	public Parser(String pFileName) throws FileNotFoundException {
		mTokenManager = new TokenMngr(pFileName);
		mTokenManager.next();

	}

	// <program> ::= program <declarations> begin <statementSequence> end
	public Program startParse() {
		TokenInfo tokenprogram = mTokenManager.token();
		// Expecting Token Program at this point
		tokenShoulBeAs(Token.PROGRAM);

		Declaration declaration = null;
		StatementSequence parseStatementSequences = null;

		if (isCurrentTokenAs(Token.VAR)) {

			declaration = parseDeclaration();

		}

		TokenInfo tokenbegin = mTokenManager.token();

		tokenShoulBeAs(Token.BEGIN);

		if (Token.getStatement().contains(mTokenManager.token().getWord())) {

			parseStatementSequences = parseStatementSequences();

		}

		TokenInfo tokenend = mTokenManager.token();
		tokenShoulBeAs(Token.END);

		return new Program(tokenprogram, declaration, tokenbegin, parseStatementSequences, tokenend);
	}

	/**
	 * <pre>
	 * <statementSequence> ::= <statement> SC <statementSequence> | epsilon
	 * </pre>
	 * 
	 *
	 * @return the statement sequence
	 */
	private StatementSequence parseStatementSequences() {

		Statement statement = parseStatement();
		TokenInfo token = mTokenManager.token();
		tokenShoulBeAs(Token.SC);

		StatementSequence parseStatementSequences = new StatementSequence(null, null, null);

		
		if (Token.getStatement().contains(mTokenManager.token().getWord())) {
			parseStatementSequences = parseStatementSequences();

		}

		return new StatementSequence(statement, token, parseStatementSequences);

	}

	/**
	 * <pre>
	 * <statement> ::= <assignment> | <ifStatement>  | <whileStatement>  | <writeInt>
	 * </pre>
	 * 
	 *
	 * @return the statement
	 */
	private Statement parseStatement() {
		Assignment parseAssignment = null;
		IfStatement parseIfStatement = null;
		WhileStatement parseWhileStatement = null;
		Writeint parseWriteExpression = null;
		if (isCurrentTokenAs(Token.ident)) {
			parseAssignment = parseAssignment();
		} else if (isCurrentTokenAs(Token.IF)) {
			parseIfStatement = parseIfStatement();
		} else if (isCurrentTokenAs(Token.WHILE)) {
			parseWhileStatement = parseWhileStatement();
		} else if (isCurrentTokenAs(Token.WRITEINT)) {
			parseWriteExpression = parseWriteExpression();
		}

		return new Statement(parseAssignment, parseIfStatement, parseWhileStatement, parseWriteExpression);

	}
	

	/**
	 * <pre>
	 * <assignment> ::= ident ASGN <assignment1>
	 * </pre>
	* @return the assignment
	 */
	private Assignment parseAssignment() {
		TokenInfo ident = mTokenManager.token();
		tokenShoulBeAs(Token.ident);
		TokenInfo asgn = mTokenManager.token();
		tokenShoulBeAs(Token.ASGN);
		AssignmentExtended parseAssignment1 = parseAssignment1();
		return new Assignment(ident, asgn, parseAssignment1);
	}

	/**
	 * <pre>
	 * <assignment1> ::= <expression> | READINT
	 * </pre>	 
	 * @return the assignment
	 */
	private AssignmentExtended parseAssignment1() {

		TokenInfo word = mTokenManager.token();
		Expression parseExpression = null;
		if (haveExpectedToekn(Token.READINT)) {

		} else {
			parseExpression = parseExpression();
		}

		return new AssignmentExtended(word, parseExpression);
	}

	/**
	 * <writeInt> ::= WRITEINT <expression>
	 */
	private Writeint parseWriteExpression() {
		TokenInfo token = mTokenManager.token();
		tokenShoulBeAs(Token.WRITEINT);
		Expression parseExpression = parseExpression();

		return new Writeint(token, parseExpression);

	}

	/*
	 * <elseClause> ::= ELSE <statementSequence>| epsilon
	 * 
	 */
	private ElseClause parseElseExpression() {
		TokenInfo token = mTokenManager.token();
		if (haveExpectedToekn(Token.ELSE)) {
			StatementSequence parseStatementSequences = parseStatementSequences();

			return new ElseClause(token, parseStatementSequences);
		} else
			return new ElseClause(null, null);

	}

	/*
	 * <ifStatement> ::= IF <expression> THEN <statementSequence> <elseClause>
	 * END
	 */
	private IfStatement parseIfStatement() {
		TokenInfo ifT = mTokenManager.token();
		tokenShoulBeAs(Token.IF);
		Expression parseExpression = parseExpression();
		TokenInfo thenT = mTokenManager.token();
		tokenShoulBeAs(Token.THEN);
		StatementSequence parseStatementSequences = parseStatementSequences();
		ElseClause parseElseExpression = parseElseExpression();
		TokenInfo endT = mTokenManager.token();
		tokenShoulBeAs(Token.END);
		return new IfStatement(ifT, parseExpression, thenT, parseStatementSequences, parseElseExpression, endT);
	}

	/**
	 * Parses the while statement. <whileStatement> ::= WHILE <expression> DO
	 * <statementSequence> END
	 * 
	 * @return the while statement
	 */
	private WhileStatement parseWhileStatement() {
		TokenInfo whileT = mTokenManager.token();
		tokenShoulBeAs(Token.WHILE);
		Expression parseExpression = parseExpression();
		TokenInfo dOT = mTokenManager.token();
		tokenShoulBeAs(Token.DO);
		StatementSequence parseStatementSequences = parseStatementSequences();
		TokenInfo endT = mTokenManager.token();
		tokenShoulBeAs(Token.END);
		return new WhileStatement(whileT, parseExpression, dOT, parseStatementSequences, endT);
	}

	/*
	 * <expression> ::= <simpleExpression> <expression1>
	 * 
	 * 
	 */
	private Expression parseExpression() {

		SimpleExpression parseSimpleExpression = parseSimpleExpression();
		ExpressionExtended expression1 = parseExpression1();

		return new Expression(parseSimpleExpression, expression1);

	}

	/*
	 * <expression1> ::= OP4 <simpleExpression> |e
	 */
	private ExpressionExtended parseExpression1() {
		TokenInfo word = mTokenManager.token();

		if (haveExpectedToekn(Token.COMPARE)) {
			SimpleExpression parseSimpleExpression = parseSimpleExpression();
			return new ExpressionExtended(word, parseSimpleExpression);
		}

		else
			return new ExpressionExtended(null, null);
	}

	/*
	 * <simpleExpression> ::= <term> <simpleExpression1>
	 * 
	 * 
	 */

	private SimpleExpression parseSimpleExpression() {
		Term parseTerm = parseTerm();
		SimpleExpressionExtended parseSimpleExpression1 = parseSimpleExpression1();

		return new SimpleExpression(parseTerm, parseSimpleExpression1);

	}

	/*
	 * <simpleExpression1> ::= OP3 <term> |e
	 */
	private SimpleExpressionExtended parseSimpleExpression1() {
		TokenInfo word = mTokenManager.token();
		if (haveExpectedToekn(Token.ADDITIVE)) {
			Term parseTerm = parseTerm();
			return new SimpleExpressionExtended(word, parseTerm);
		} else
			return new SimpleExpressionExtended(null, null);
	}

	/**
	 * <pre>
	 * <term> ::= <factor><term1>
	 * 
	 * </pre>
	 * @return the term
	 */
	private Term parseTerm() {
		Factor parseFactor = parseFactor();
		TermExtended parseTerm1 = parseTerm1();

		return new Term(parseFactor, parseTerm1);
	}

	/**
	 * <pre>
	 * <term1> ::= OP2 <factor>|e
	 * </pre>
	 *
	 * @return the term1
	 */
	private TermExtended parseTerm1() {
		TokenInfo token = mTokenManager.token();
		if (haveExpectedToekn(Token.MULTIPLICATIVE)) {

			Factor parseFactor = parseFactor();

			return new TermExtended(token, parseFactor);
		}

		else
			return new TermExtended(null, null);

	}

	/**
	 * Parses the factor.
	 * 
	 * <pre>
	 * <factor> ::= ident | num | boollit | LP <expression> RP
	 * </pre>
	 * 
	 * @return the factor
	 */
	private Factor parseFactor() {

		TokenInfo token = mTokenManager.token();
		if (haveExpectedToekn(Token.ident) || haveExpectedToekn(Token.num) || haveExpectedToekn(Token.boollit)) {
			return new Factor(token, null, null);
		} else {
			tokenShoulBeAs(Token.LP);

			Expression parseExpression = parseExpression();
			TokenInfo token2 = mTokenManager.token();
			tokenShoulBeAs(Token.RP);

			return new Factor(token, parseExpression, token2);

		}

	}

	/**
	 * <pre>
	 * <declarations> ::= var ident as <type> SC <declarations> | ε
	 * </pre>
	 * 	
	 * @return the declaration
	 */

	public Declaration parseDeclaration() {
		TokenInfo tokenVar = mTokenManager.token();

		tokenShoulBeAs(Token.VAR);
		TokenInfo tokenID = mTokenManager.token();
		tokenShoulBeAs(Token.ident);
		TokenInfo tokenAs = mTokenManager.token();
		tokenShoulBeAs(Token.AS);
		Type parseType = parseType();
		TokenInfo tokenSc = mTokenManager.token();
		tokenShoulBeAs(Token.SC);

		Declaration declaration = null;
		if (isCurrentTokenAs(Token.VAR)) {
			declaration = parseDeclaration();
		}

		return new Declaration(tokenVar, tokenID, tokenAs, parseType, tokenSc, declaration);
	}

	/**
	 * <pre>
	 * <type>  ::=  BOOL | INT
	 * </pre>
	 *	
	 * @return the type
	 */
	public Type parseType() {
		TokenInfo token = mTokenManager.token();
		if (haveExpectedToekn(Token.BOOL) || haveExpectedToekn(Token.INT)) {
			return new Type(token);
		} else {
			isError = true;
			return new Type(null);
		}

	}

	/**
	 * Must be.
	 *
	 * @param pToken
	 *            the token
	 */
	private void tokenShoulBeAs(Token pToken) {
		if (mTokenManager.token().getWord() == pToken) {
			isRecovered = true;
			mTokenManager.next();
		} else if (isRecovered) {
			isRecovered = false;

			ConsoleLogger.reportParserError("PARSER ERROR %s found where %s sought in line no %d ",
					mTokenManager.token().getWord(), pToken, mTokenManager.token().getLineNumber());
		} else {
			// Do not report the (possibly spurious) error,
			// but rather attempt to recover by forcing a match.
			while (!isCurrentTokenAs(pToken) && !isCurrentTokenAs(Token.EOF)) {
				mTokenManager.next();
			}
			if (isCurrentTokenAs(pToken)) {
				isRecovered = true;
				mTokenManager.next();
			}
		}
	}

	/**
	 * Have expected toekn.
	 *
	 * @param pToken
	 *            the token
	 * @return true, if successful
	 */
	private boolean haveExpectedToekn(Token pToken) {
		if (isCurrentTokenAs(pToken)) {
			mTokenManager.next();
			return true;

		} else {
			return false;
		}
	}

	/**
	 * Checks if is match.
	 *
	 * @param pToken
	 *            the token
	 * @return true, if is match
	 */
	private boolean isCurrentTokenAs(Token pToken) {
		return pToken.getValue().equalsIgnoreCase(mTokenManager.token().getWord().getValue());
	}

}
