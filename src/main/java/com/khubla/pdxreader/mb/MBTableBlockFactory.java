package com.khubla.pdxreader.mb;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.mb.block.*;

public class MBTableBlockFactory {
	public static MBTableBlock getMBTableBlock(MBTableBlock.RecordType recordType) throws PDXReaderException {
		switch (recordType) {
			case header:
				return new MBTableHeaderBlock();
			case single:
				return new MBSingleBlock();
			case suballocated:
				return new MBSuballocatedBlock();
			case free:
				return new MBFreeBlock();
			default:
				throw new PDXReaderException("Unknown record type '" + recordType + "'");
		}
	}
}
