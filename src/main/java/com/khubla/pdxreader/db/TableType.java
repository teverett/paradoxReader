package com.khubla.pdxreader.db;

/**
 * table type
 */
public enum TableType {
	indexedDB(0), indexPX(1), nonindexedDB(2), noincrementingsecondaryindexXnn(3), secondaryindexYnn(4), incrementingsecondaryindexXnn(5), nonincrementingsecondaryindexXGn(6), secondaryindexYGn(
			7), incrementingsecondaryindexXGn(8);

	public int value;

	private TableType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}