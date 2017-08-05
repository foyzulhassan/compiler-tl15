/*
 * Foyzul Hassan
 * Entity class to hold Token Information 
 */

package com.entities;

import com.scanner.Token;

public class TokenInfo {
	//Token Word
	private Token word;
	//Token Line Number
	private int lineNumber;
	//Associate Value with the token
	private String wordValue;

	public Token getWord() {
		return word;
	}

	public void setWord(Token word) {
		this.word = word;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getWordValue() {
		return wordValue;
	}

	public void setWordValue(String wordValue) {
		this.wordValue = wordValue;
	}

	public TokenInfo() {
		this.word = null;
		this.lineNumber = -1;
		this.wordValue = "";

	}

	public TokenInfo(Token token, int linenumber) {
		this.word = token;
		this.lineNumber = linenumber;
		this.wordValue = "";
	}

	public TokenInfo(Token token, String value, int lineNumber) {
		this.word = token;
		this.lineNumber = lineNumber;
		this.wordValue = value;
	}
}
