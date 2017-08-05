/*
 * Foyzul Hassan
 * Input file reader class to get character and linenumber
 * 
 */

package com.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class InputFileReader
{
	private LineNumberReader lineNumberReader;

	public InputFileReader(String filename) throws FileNotFoundException 
	{
		lineNumberReader = new LineNumberReader(new FileReader(new File(filename)));
	}
	
	public String getNextLine()
	{
		String line = null;
        try {
			line = lineNumberReader.readLine();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        
        return line;        
	}

	public char getNextChar() {
		char ch = (char) -1;
		try {
			ch = (char) lineNumberReader.read();
				
		} catch (IOException e) {			
			e.printStackTrace();
		}

		return ch;
	}

	public int getLineNumber() {

		return lineNumberReader.getLineNumber()+1;
	}
	
	public void setLineNumber(int linenumber)
	{
		lineNumberReader.setLineNumber(linenumber);
	}

	public void close() {
		try {
			lineNumberReader.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

}
