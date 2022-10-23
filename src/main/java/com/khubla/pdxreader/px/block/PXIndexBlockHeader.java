package com.khubla.pdxreader.px.block;

import com.google.common.io.*;
import com.khubla.pdxreader.api.*;

/**
 * @author tom
 */
public class PXIndexBlockHeader {
	private int nextBlockNumber;
	private int previousBlockNumber;
	private int offsetLastRecord;

	public int getNextBlockNumber() {
		return nextBlockNumber;
	}

	public int getOffsetLastRecord() {
		return offsetLastRecord;
	}

	public int getPreviousBlockNumber() {
		return previousBlockNumber;
	}

	/**
	 * block header, 6 bytes
	 */
	public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
		try {
			nextBlockNumber = littleEndianDataInputStream.readUnsignedShort();
			previousBlockNumber = littleEndianDataInputStream.readUnsignedShort();
			offsetLastRecord = littleEndianDataInputStream.readUnsignedShort();
		} catch (final Exception e) {
			throw new PDXReaderException("Exception in read", e);
		}
	}

	public void setNextBlockNumber(int nextBlockNumber) {
		this.nextBlockNumber = nextBlockNumber;
	}

	public void setOffsetLastRecord(int offsetLastRecord) {
		this.offsetLastRecord = offsetLastRecord;
	}

	public void setPreviousBlockNumber(int previousBlockNumber) {
		this.previousBlockNumber = previousBlockNumber;
	}
}
