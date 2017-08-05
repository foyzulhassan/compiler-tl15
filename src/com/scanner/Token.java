/*
 * Foyzul Hassan
 * Enum class with associated values
 */

package com.scanner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;





public enum Token {
	BOOL("bool"), INT("int"), IF("if"), THEN("then"), ELSE("else"), BEGIN("begin"), END("end"), WHILE("while"), DO(
			"do"), PROGRAM("program"), VAR("var"), AS("as"), LP("("), RP(")"), ASGN(":="), SC(";"), WRITEINT("writeint"), READINT("readint"), num("num"), boollit(
									"boollit"), ident("ident"), MULTIPLICATIVE("multiplicative"), ADDITIVE(
											"additive"), COMPARE("compare"), EOF("EOF"), ARRAYSTART(
													"arraystart"), ARRAYEND("arrayend"),VOID("void");

	private String tokenname;

	Token(String name) {
		this.tokenname = name;
	}

	public String getValue() {
		return this.tokenname;
	}
	
	/*public static Set<Token> getOP2() {
		Set<Token> set = new HashSet<Token>(Arrays.asList(MUL, MOD, DIV));
		return set;
	}*/
	
	public static Set<Token> getFactor() {
		Set<Token> set = new HashSet<Token>(Arrays.asList(ident, num, boollit,LP));
		return set;
	}

	/*public static Set<Token> getOP3() {
		Set<Token> set = new HashSet<Token>(Arrays.asList(PLUS, MINUS));
		return set;
	}*/

	/*public static Set<Token> getOP4() {
		Set<Token> set = new HashSet<Token>(Arrays.asList(EQUAL, NOT_EQUAL, LT, LT_EQUAL, GT, GT_EQUAL));
		return set;
	}*/
	
	public static Set<Token> getINTOP() {
		Set<Token> set = new HashSet<Token>(Arrays.asList(ADDITIVE,MULTIPLICATIVE));
		return set;
	}
	
	public static Set<Token> getArithOP() {
		Set<Token> set = new HashSet<Token>(Arrays.asList(ADDITIVE,MULTIPLICATIVE));
		return set;
	}

	public static Set<Token> getStatement() {
		Set<Token> set = new HashSet<Token>(Arrays.asList(ident, IF, WHILE, WRITEINT));
		return set;
	}
	
	public static Set<Token> getValidASTTokens() {
		Set<Token> set = new HashSet<Token>(Arrays.asList(IF,WHILE, PROGRAM, ASGN, WRITEINT, READINT, num,
				boollit, ident, MULTIPLICATIVE, ADDITIVE, COMPARE));
		return set;
	}
	
	public static Set<Token> getBinaryOperation() {
		Set<Token> set = new HashSet<Token>(Arrays.asList(MULTIPLICATIVE, ADDITIVE, COMPARE,WRITEINT));
		return set;
	}
	
	public static Set<Token> getIntOPeration() {
		Set<Token> set = new HashSet<Token>(Arrays.asList(MULTIPLICATIVE, ADDITIVE, COMPARE));
		return set;
	}

}
