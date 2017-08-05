package com.block;

public class BlockCounter {
	static int count = 0;
	int prevCount=0;
	private static BlockCounter Instance;

	private BlockCounter() {
		count = 0;

	}

	public static BlockCounter getInstance() {
		if (Instance == null) {
			Instance = new BlockCounter();
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
