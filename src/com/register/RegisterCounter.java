package com.register;

public class RegisterCounter {
	static int count = 0;
	int prevCount=0;
	private static RegisterCounter Instance;

	private RegisterCounter() {
		count = 0;

	}

	public static RegisterCounter getInstance() {
		if (Instance == null) {
			Instance = new RegisterCounter();
		}
		return Instance;
	}

	public int getCount() {

		int temp = count;
        prevCount = count;
		count++;

		return temp;

	}
	
	
	public int getPrevCount() {
		return prevCount;
	}

}
