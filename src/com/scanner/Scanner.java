package com.scanner;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.entities.TokenInfo;
import com.utils.CharTypeIdentifier;
import com.utils.DataTypeRangeChecker;
import com.utils.InputFileReader;

public class Scanner {

	// current read character
	private char currentChar;
	// line number of current character
	private int lineNumber;
	// to indicate whether there is error
	private boolean isError;
	// Hashtable to hold TL15 Keywords
	private Hashtable<String, Token> keyWordsTable;
	// InputFileReader to read characters from TL15 source file
	private InputFileReader inputFileReader;
	// File name of the source file
	private String fileName;
	// End of character
	final char EOFCH = (char) -1;
	
	private List<String> errorList;

	// CTOR
	public Scanner(String fileName) throws FileNotFoundException {

		this.fileName = fileName;
		this.inputFileReader = new InputFileReader(fileName);

		keyWordsTable = new Hashtable<String, Token>();
		errorList=new ArrayList<String>();
		keyWordsTable = KeyWords.getKeyWords();

		this.isError = false;
		this.currentChar = ' ';
	}

	// Determine if any scanner error is generated
	public boolean hasError() {
		return isError;
	}

	// Get next character and line number from source file
	private void readNextCharacter() {
		currentChar = inputFileReader.getNextChar();
		lineNumber = inputFileReader.getLineNumber();
	}
	
	
	@SuppressWarnings("static-access")
	private void updateErrorList(String message, Object... args)
	{
		String strmsg="[SCANNER ERROR]: "+message;
		String msg="";
		
		msg=msg.format(strmsg,args);
		
		errorList.add(msg);
	}
	
	public List<String> getErrorList()
	{
		return errorList;
	}

	// Function to process token based on current token
	public TokenInfo getNextToken() {
		readNextCharacter();
		StringBuffer buffer;
		while (CharTypeIdentifier.isWhiteSpace(currentChar)) {
			if(currentChar==EOFCH)
				break;
			readNextCharacter();
		}

		switch (currentChar) {

		case '+':
			readNextCharacter();
			if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
				return new TokenInfo(Token.ADDITIVE, "+", lineNumber);
			} else {
				isError = true;
				updateErrorList("[%s line:%d] After '+' token it is not allowed to write %s", fileName,
						lineNumber, "" + currentChar);
				return getNextToken();
			}

		case '-':
			readNextCharacter();
			if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
				return new TokenInfo(Token.ADDITIVE, "-", lineNumber);
			} else {
				isError = true;
				updateErrorList("[%s line:%d] After '-' token it is not allowed to write %s", fileName,
						lineNumber, "" + currentChar);
				return getNextToken();
			}

		case '*':
			readNextCharacter();
			if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
				return new TokenInfo(Token.MULTIPLICATIVE, "*", lineNumber);
			} else {
				isError = true;
				updateErrorList("[%s line:%d] After '*' token it is not allowed to write %s", fileName,
						lineNumber, "" + currentChar);
				return getNextToken();
			}

		case '(':
			readNextCharacter();
			if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
				return new TokenInfo(Token.LP, lineNumber);
			} else {
				isError = true;
				updateErrorList("[%s line:%d] After '(' token it is not allowed to write %s", fileName,
						lineNumber, "" + currentChar);
				return getNextToken();
			}

		case ')':
			readNextCharacter();
			if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
				return new TokenInfo(Token.RP, lineNumber);
			} else {
				isError = true;
				updateErrorList("[%s line:%d] After ')' token it is not allowed to write %s", fileName,
						lineNumber, "" + currentChar);
				return getNextToken();
			}

		case '=':
			readNextCharacter();
			if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
				return new TokenInfo(Token.COMPARE, "=", lineNumber);
			} else {
				isError = true;
				updateErrorList("[%s line:%d] After '=' token it is not allowed to write %s", fileName,
						lineNumber, "" + currentChar);
				return getNextToken();
			}

		case ';':
			readNextCharacter();
			if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
				return new TokenInfo(Token.SC, lineNumber);
			} else {
				isError = true;
				updateErrorList("[%s line:%d] After ';' token it is not allowed to write %s", fileName,
						lineNumber, "" + currentChar);
				return getNextToken();
			}

		case '[':
			readNextCharacter();
			if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
				return new TokenInfo(Token.ARRAYSTART, lineNumber);
			} else {
				isError = true;
				updateErrorList("[%s line:%d] After '[' token it is not allowed to write %s", fileName,
						lineNumber, "" + currentChar);
				return getNextToken();
			}

		case ']':
			readNextCharacter();
			if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
				return new TokenInfo(Token.ARRAYEND, lineNumber);
			} else {
				isError = true;
				updateErrorList("[%s line:%d] After ']' token it is not allowed to write %s", fileName,
						lineNumber, "" + currentChar);
				return getNextToken();
			}

		case ':':
			readNextCharacter();
			if (currentChar == '=') {
				readNextCharacter();
				if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
					return new TokenInfo(Token.ASGN, lineNumber);
				} else {
					isError = true;
					updateErrorList("[%s line:%d] After ':=' token it is not allowed to write %s", fileName,
							lineNumber, "" + currentChar);
					return getNextToken();
				}

			} else {
				isError = true;
				updateErrorList("[%s line:%d] '%s' does not does not support after \":\" character", fileName,
						lineNumber, "" + currentChar);

				return getNextToken();
			}
			
		case '>':
			readNextCharacter();
			if (currentChar == ' ') {

				return new TokenInfo(Token.COMPARE, ">", lineNumber);

			} else if (currentChar == '=') {
				readNextCharacter();
				if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
					return new TokenInfo(Token.COMPARE, ">=", lineNumber);
				} else {
					isError = true;
					updateErrorList("[%s line:%d] After '>=' token it is not allowed to write %s", fileName,
							lineNumber, "" + currentChar);
					return getNextToken();
				}

			} else {
				isError = true;
				updateErrorList("[%s line:%d] %s does not does not support after '>' Character %s", fileName,
						lineNumber, "" + currentChar);

				return getNextToken();
			}
		case '<':
			readNextCharacter();
			if (currentChar == ' ') {

				return new TokenInfo(Token.COMPARE, "<", lineNumber);

			} else if (currentChar == '=') {
				readNextCharacter();
				if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
					return new TokenInfo(Token.COMPARE, "<=", lineNumber);
				} else {
					isError = true;
					updateErrorList("[%s line:%d] After '<=' token it is not allowed to write %s", fileName,
							lineNumber, "" + currentChar);
					return getNextToken();
				}

			} else {
				isError = true;
				updateErrorList("[%s line:%d] %s does not does not support after < Character", fileName,
						lineNumber, "" + currentChar);

				return getNextToken();
			}

		case '!':
			readNextCharacter();
			if (currentChar == '=') {
				readNextCharacter();
				if (CharTypeIdentifier.isWhiteSpace(currentChar)) {
					return new TokenInfo(Token.COMPARE, "!=", lineNumber);
				} else {
					isError = true;
					updateErrorList("[%s line:%d] After '!=' token it is not allowed to write %s", fileName,
							lineNumber, "" + currentChar);
					return getNextToken();
				}

			} else {
				isError = true;
				updateErrorList("[%s line:%d] %s does not support after ! Character", fileName, lineNumber,
						"" + currentChar);

				return getNextToken();
			}
		case '%':
			readNextCharacter();
			while (currentChar != '\n') {
				readNextCharacter();
			}
			return getNextToken();

		case EOFCH:
			return new TokenInfo(Token.EOF, lineNumber);

		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			buffer = new StringBuffer();
			boolean beginzerochek = false;
			boolean leadingzeroerror = false;

			if (currentChar == '0')
				beginzerochek = true;

			while (Character.isDigit(currentChar)) {
				buffer.append(currentChar);
				readNextCharacter();

				if (beginzerochek == true && Character.isDigit(currentChar)) {
					leadingzeroerror = true;
				}
			}

			if (!CharTypeIdentifier.isWhiteSpace(currentChar)) {

				isError = true;
				updateErrorList("[%s line:%d] After number it is not allowed to write %s", fileName,
						lineNumber, "" + currentChar);
				return getNextToken();
			}

			if (leadingzeroerror == true) {
				isError = true;
				updateErrorList("[%s line:%d] Does not support leading zero for int", fileName, lineNumber);

				return getNextToken();
			} else {
				if (DataTypeRangeChecker.isValidIntRange(buffer)) {
					return new TokenInfo(Token.num, buffer.toString(), lineNumber);
				} else {
					isError = true;
					updateErrorList("[%s line:%d] Interger value out of range: %s", fileName, lineNumber,
							buffer.toString());

					return getNextToken();
				}
			}

		default:
			if (CharTypeIdentifier.isStartOfIdentifier(currentChar)) {
				buffer = new StringBuffer();
				while (CharTypeIdentifier.isPartOfIdentifier(currentChar)) {
					buffer.append(currentChar);
					readNextCharacter();
				}
				String identifier = buffer.toString();

				if (!CharTypeIdentifier.isWhiteSpace(currentChar)) {
					isError = true;
					
					buffer.append(currentChar);

					readNextCharacter();
					while (!CharTypeIdentifier.isWhiteSpace(currentChar)) {
						buffer.append(currentChar);
						readNextCharacter();
					}
					
					updateErrorList("[%s line:%d] Invalid Identifier %s", fileName, lineNumber,buffer.toString());

					return getNextToken();
					
				} else {
					return new TokenInfo(Token.ident, identifier, lineNumber);
				}
			}

			else if (CharTypeIdentifier.isStartOfKeyword(currentChar)) {
				buffer = new StringBuffer();
				while (CharTypeIdentifier.isPartOfKeyword(currentChar)) {
					buffer.append(currentChar);
					readNextCharacter();
				}

				if (!CharTypeIdentifier.isWhiteSpace(currentChar)) {
					isError = true;
					buffer.append(currentChar);

					readNextCharacter();
					while (!CharTypeIdentifier.isWhiteSpace(currentChar)) {			
						
						buffer.append(currentChar);
						readNextCharacter();
					}				
					
					updateErrorList("[%s line:%d] Kayword %s is not supported", fileName, lineNumber,
							buffer.toString());

					return getNextToken();
				}

				String identifier = buffer.toString();
				if (identifier.equalsIgnoreCase("true") || identifier.equalsIgnoreCase("false")) {
					return new TokenInfo(Token.boollit, identifier, lineNumber);
				} else if (identifier.equalsIgnoreCase("div") || identifier.equalsIgnoreCase("mod")) {
					return new TokenInfo(Token.MULTIPLICATIVE, identifier, lineNumber);
				} else if (keyWordsTable.containsKey(identifier)) {
					return new TokenInfo(keyWordsTable.get(identifier), lineNumber);
				} else {
					isError = true;
					updateErrorList("[%s line:%d] Invalid keyword %s", fileName, lineNumber,
							identifier);

					return getNextToken();
				}
			} else {
				isError = true;
				updateErrorList("[%s line:%d] Unidentified input token: '%c'", fileName, lineNumber,
						currentChar);
				readNextCharacter();
				return getNextToken();
			}
		}
	}
}
