package com.scanner;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import com.entities.TokenInfo;

public class TokenMngr {

	private Scanner mScanner;

	private TokenInfo previousToken;

	private TokenInfo token;
	
	private List<TokenInfo> tokenlist = new ArrayList<TokenInfo>();
	
	private int tokenindex;

	public TokenMngr(String fileName) throws FileNotFoundException {
		mScanner = new Scanner(fileName);
		
		while (true) {
			TokenInfo tokeninfo = mScanner.getNextToken();

			if (tokeninfo.getWord() == Token.EOF)
			{
				tokenlist.add(tokeninfo);
				break;

			}
			
			else {
				tokenlist.add(tokeninfo);

			}
		}
		
		if(mScanner.hasError())
		{
			List<String> errors=new ArrayList<String>();
			errors=mScanner.getErrorList();			
			
			for(int i=0;i<errors.size();i++)
				System.out.println(errors.get(i));
			
			System.out.println("Please fix the SCANNER ERROR(s) to continue");
			
			System.exit(0);
		}
		
		tokenindex=0;

	}

	public TokenInfo previousToken() {
		return previousToken;
	}

	public boolean hasError() {
		return mScanner.hasError();
	}

	public TokenInfo token() {
		return token;
	}

	/*
	 * public void recordPosition() { isLookingAhead = true;
	 * mQueueStack.push(mNextQueue); mNextQueue = new Vector<TokenWord>();
	 * mNextQueue.add(previousToken); mNextQueue.add(token); }
	 */

	public void next() {
		previousToken = token;		
		//token = mScanner.getNextToken();
		token=tokenlist.get(tokenindex);
		tokenindex++;	
		
	}
}
