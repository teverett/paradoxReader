package com.khubla.pdxreader.mb.block;

import com.google.common.io.*;
import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.mb.*;

public class MBSingleBlock extends MBTableBlock {
	public MBSingleBlock() {
		super(MBTableBlock.RecordType.single);
	}

	@Override
	public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
		// sizeofBlock = littleEndianDataInputStream.readUnsignedShort();
		// modificationCount = littleEndianDataInputStream.readUnsignedShort();
	}
}
