/*
 * Foyzul Hassan
 * Utility functions to datatype range checking
 */

package com.utils;

public class DataTypeRangeChecker {
	public static boolean isValidIntRange(StringBuffer strbuff) {
		boolean ret = false;

		long val;

		val = Long.parseLong(strbuff.toString());
		

		if (val >= 0 && val <= 2147483647)
			ret = true;

		return ret;
	}

}
