/*
 * Foyzul Hassan
 * Class to load TL15 Keywords
 */

package com.scanner;

import java.util.Hashtable;

import com.scanner.Token;

public class KeyWords {
	public static Hashtable<String, Token> getKeyWords() {
		Hashtable<String, Token> hashtable = new Hashtable<String, Token>();

		hashtable.put(Token.PROGRAM.getValue(), Token.PROGRAM);
		hashtable.put(Token.BEGIN.getValue(), Token.BEGIN);
		hashtable.put(Token.VAR.getValue(), Token.VAR);
		hashtable.put(Token.AS.getValue(), Token.AS);
		hashtable.put(Token.IF.getValue(), Token.IF);
		hashtable.put(Token.ELSE.getValue(), Token.ELSE);
		hashtable.put(Token.THEN.getValue(), Token.THEN);
		hashtable.put(Token.INT.getValue(), Token.INT);
		hashtable.put(Token.BOOL.getValue(), Token.BOOL);
		hashtable.put(Token.END.getValue(), Token.END);
		hashtable.put(Token.WHILE.getValue(), Token.WHILE);
		hashtable.put(Token.DO.getValue(), Token.DO);
		hashtable.put(Token.WRITEINT.getValue(), Token.WRITEINT);
		hashtable.put(Token.READINT.getValue(), Token.READINT);
		hashtable.put("div", Token.MULTIPLICATIVE);
		hashtable.put("mod",Token.MULTIPLICATIVE);

		return hashtable;

	}

}
