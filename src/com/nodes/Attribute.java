package com.nodes;

import com.scanner.Token;

public class Attribute {
	String name;
	Token value;

	public String getName() {
		return name;
	}

	public void setName(String pName) {
		name = pName;
	}

	public Token getValue() {
		return value;
	}

	public void setValue(Token pValue) {
		value = pValue;
	}

}
