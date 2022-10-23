package com.khubla.pdxreader.mb.block;

import com.google.common.io.*;
import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.mb.*;

public class MBFreeBlock extends MBTableBlock {
	public MBFreeBlock() {
		super(MBTableBlock.RecordType.free);
	}

	@Override
	public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
		// sizeofBlock = littleEndianDataInputStream.readUnsignedShort();
		// modificationCount = littleEndianDataInputStream.readUnsignedShort();
	}
}
