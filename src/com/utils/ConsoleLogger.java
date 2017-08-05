/*
 * Foyzul Hassan
 * Utility functions print error logs
 */

package com.utils;

public class ConsoleLogger {

	public static void reportScannerError(String message, Object... args) {
		System.err.printf("[SCANNER ERROR]: ");
		System.err.printf(message, args);
		System.err.println();
	}
	
	public static void reportParserError(String message, Object... args) {
		System.err.printf("[PARSER ERROR]: ");
		System.err.printf(message, args);
		System.err.println();
	}
}
