/*
 * Foyzul Hassan
 * Utility functions to identify character type
 */

package com.utils;


public class CharTypeIdentifier {
	final static char EOFCH = (char) -1;
	public static boolean isWhiteSpace(char ch) {
		boolean ret = false;

		if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\n' || ch == '\f' || ch==EOFCH) {
			ret = true;
		}

		return ret;
	}

	public static boolean isStartOfIdentifier(char ch) {
		boolean ret = false;

		if (ch >= 'A' && ch <= 'Z') {
			ret = true;
		}

		return ret;
	}

	public static boolean isPartOfIdentifier(char ch) {
		boolean ret = false;

		if (isStartOfIdentifier(ch) || Character.isDigit(ch)) {
			ret = true;
		}

		return ret;
	}

	public static boolean isStartOfKeyword(char ch) {
		boolean ret = false;

		if (ch >= 'a' && ch <= 'z') {
			ret = true;
		}

		return ret;
	}

	public static boolean isPartOfKeyword(char ch) {
		boolean ret = false;

		if (isStartOfKeyword(ch)) {
			ret = true;
		}

		return ret;
	}

}
