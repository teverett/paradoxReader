package com.khubla.pdxreader.db;

/**
 * block size
 */
public enum BlockSize {
	oneK(1), twoK(2), threeK(3), fourK(4), eightK(8), sixteenK(16), thirtytwoK(32);

	public int value;

	private BlockSize(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}