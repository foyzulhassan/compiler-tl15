package com.nodes;

public class Counter {
	static int count = 0;
	int prevCount = 0;
	private static Counter Instance;

	private Counter() {
		count = 0;

	}

	public static Counter getInstance() {
		if (Instance == null) {
			Instance = new Counter();
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
