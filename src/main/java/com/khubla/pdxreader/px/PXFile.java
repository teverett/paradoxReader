package com.khubla.pdxreader.px;

import java.io.*;
import java.util.*;

import com.google.common.io.*;
import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.px.block.*;

/**
 * @author tom
 */
public class PXFile {
	/**
	 * max header size
	 */
	private final static int MAX_HEADER_SIZE = 10240;
	/**
	 * max block size
	 */
	private final static int MAX_BLOCK_SIZE = 10240;
	/**
	 * header
	 */
	private PXFileHeader pxFileHeader;
	/**
	 * blocks
	 */
	private List<PXIndexBlock> blocks;

	public List<PXIndexBlock> getBlocks() {
		return blocks;
	}

	public PXFileHeader getPxFileHeader() {
		return pxFileHeader;
	}

	/**
	 * read
	 *
	 * @throws FileNotFoundException
	 */
	public void read(File file, PDXIndexListener pdxIndexListener) throws PDXReaderException, FileNotFoundException {
		/*
		 * check if the file exists
		 */
		if (file.exists()) {
			try {
				/*
				 * start
				 */
				pdxIndexListener.start();
				/*
				 * set up streams
				 */
				final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
				final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(bufferedInputStream);
				try {
					/*
					 * mark and read the headers
					 */
					bufferedInputStream.mark(MAX_HEADER_SIZE);
					readHeaders(littleEndianDataInputStream);
					/*
					 * call the api
					 */
					pdxIndexListener.header(pxFileHeader);
					/*
					 * read the block data
					 */
					bufferedInputStream.reset();
					readBlocks(bufferedInputStream, pdxIndexListener);
					/*
					 * done
					 */
					pdxIndexListener.finish();
				} finally {
					littleEndianDataInputStream.close();
					bufferedInputStream.close();
				}
			} catch (final Exception e) {
				throw new PDXReaderException("Exception in read", e);
			}
		} else {
			throw new FileNotFoundException();
		}
	}

	/**
	 * read block data
	 */
	private void readBlocks(BufferedInputStream bufferedInputStream, PDXIndexListener pdxIndexListener) throws PDXReaderException {
		try {
			/*
			 * init the array
			 */
			blocks = new ArrayList<PXIndexBlock>();
			/*
			 * skip to the first index block
			 */
			final int nSkip = pxFileHeader.getHeaderBlockSize();
			if (nSkip == bufferedInputStream.skip(nSkip)) {
				/*
				 * walk index blocks
				 */
				final int blocksInUse = pxFileHeader.getBlocksInUse();
				for (int i = 0; i < blocksInUse; i++) {
					/*
					 * block
					 */
					final PXIndexBlock pxIndexBlock = new PXIndexBlock();
					/*
					 * mark at the start of the block
					 */
					bufferedInputStream.mark(MAX_BLOCK_SIZE);
					/*
					 * read the block data
					 */
					pxIndexBlock.read(pdxIndexListener, bufferedInputStream);
					/*
					 * store it. blocks are numbered from 1, not from 0.
					 */
					blocks.add(pxIndexBlock);
					/*
					 * reset to the start of the block
					 */
					bufferedInputStream.reset();
					/*
					 * skip ahead to next block
					 */
					bufferedInputStream.skip(pxFileHeader.getBlockSize().getValue() * 1024);
				}
			} else {
				throw new PDXReaderException("File format exception");
			}
		} catch (final Exception e) {
			throw new PDXReaderException("Exception in readBlocks", e);
		}
	}

	/**
	 * read
	 */
	private void readHeaders(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
		try {
			/*
			 * read header
			 */
			pxFileHeader = new PXFileHeader();
			pxFileHeader.read(littleEndianDataInputStream);
		} catch (final Exception e) {
			throw new PDXReaderException("Exception in readHeaders", e);
		}
	}

	public void setBlocks(List<PXIndexBlock> blocks) {
		this.blocks = blocks;
	}

	public void setPxFileHeader(PXFileHeader pxFileHeader) {
		this.pxFileHeader = pxFileHeader;
	}
}
