package com.khubla.pdxreader.mb.block;

import java.io.*;

import com.google.common.io.*;
import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.mb.*;

public class MBTableHeaderBlock extends MBTableBlock {
	/**
	 * modification count
	 */
	private int modificationCount;

	public MBTableHeaderBlock() {
		super(MBTableBlock.RecordType.header);
	}

	public int getModificationCount() {
		return modificationCount;
	}

	@Override
	public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
		try {
			final int blockType = littleEndianDataInputStream.readByte();
			if (blockType != super.recordType.getValue()) {
				throw new PDXReaderException("Block type mismatch");
			}
			final int sizeofBlock = littleEndianDataInputStream.readUnsignedShort();
			if ((sizeofBlock * BLOCK_SIZE_MULTIPLIER) != super.sizeofBlock) {
				throw new PDXReaderException("Block type mismatch");
			}
			modificationCount = littleEndianDataInputStream.readUnsignedShort();
		} catch (final IOException e) {
			throw new PDXReaderException("Exception reading inputStream", e);
		}
	}

	public void setModificationCount(int modificationCount) {
		this.modificationCount = modificationCount;
	}
}
