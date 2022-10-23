package com.khubla.pdxreader.mb;

import java.io.*;
import java.util.*;

import com.google.common.io.*;
import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.mb.MBTableBlock.*;

/**
 * @author tom
 */
public class MBTableFile {
	/**
	 * blocks
	 */
	private final List<MBTableBlock> blocks = new ArrayList<MBTableBlock>();

	/**
	 * This method finds all the blocks with their types and offsets
	 */
	private void preReadBlocks(File file) throws PDXReaderException {
		try {
			/*
			 * set up streams
			 */
			final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(bufferedInputStream);
			try {
				/*
				 * offset tracker
				 */
				int fileOffset = 0;
				/*
				 * loop
				 */
				while (littleEndianDataInputStream.available() > 1) {
					/*
					 * read type
					 */
					final int type = littleEndianDataInputStream.readByte();
					/*
					 * get an appropriate Block type
					 */
					final RecordType recordType = MBTableBlock.getRecordType(type);
					/*
					 * ok?
					 */
					if (null != recordType) {
						final MBTableBlock mbTableBlock = MBTableBlockFactory.getMBTableBlock(recordType);
						/*
						 * set the offset
						 */
						mbTableBlock.setFileOffset(fileOffset);
						/*
						 * pre-read
						 */
						final int bytesPreRead = mbTableBlock.preRead(littleEndianDataInputStream);
						/*
						 * add to list
						 */
						blocks.add(mbTableBlock);
						/*
						 * skip forward to next one
						 */
						final int bytesToSkip = mbTableBlock.getSizeofBlock() - (1 + bytesPreRead);
						final long skipped = littleEndianDataInputStream.skip(bytesToSkip);
						if (0 != skipped) {
							/*
							 * update the offset
							 */
							fileOffset += mbTableBlock.getSizeofBlock();
						}
					}
				}
			} finally {
				littleEndianDataInputStream.close();
				bufferedInputStream.close();
			}
		} catch (final Exception e) {
			throw new PDXReaderException("Exception in read", e);
		}
	}

	/**
	 * read
	 *
	 * @throws FileNotFoundException
	 */
	public void read(File file) throws PDXReaderException, FileNotFoundException {
		/*
		 * check if the file exists
		 */
		if (file.exists()) {
			try {
				/*
				 * pre-read
				 */
				preReadBlocks(file);
				/*
				 * read
				 */
				readBlocks(file);
			} catch (final Exception e) {
				throw new PDXReaderException("Exception in read", e);
			}
		} else {
			throw new FileNotFoundException();
		}
	}

	/**
	 * Read all blocks
	 */
	private void readBlocks(File file) throws PDXReaderException {
		try {
			for (final MBTableBlock mbTableBlock : blocks) {
				/*
				 * set up streams
				 */
				final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
				final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(bufferedInputStream);
				try {
					/*
					 * ffd
					 */
					final long skipped = littleEndianDataInputStream.skip(mbTableBlock.getFileOffset());
					if (0 != skipped) {
						/*
						 * read
						 */
						mbTableBlock.read(littleEndianDataInputStream);
					}
				} finally {
					littleEndianDataInputStream.close();
					bufferedInputStream.close();
				}
			}
		} catch (final Exception e) {
			throw new PDXReaderException("Exception in read", e);
		}
	}
}
