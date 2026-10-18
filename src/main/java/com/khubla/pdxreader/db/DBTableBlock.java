package com.khubla.pdxreader.db;

import java.io.*;
import java.util.*;

import com.google.common.io.*;
import com.khubla.pdxreader.api.*;

/**
 * @author tom
 */
public class DBTableBlock {
	/**
	 * header
	 */
	private DBTableBlockHeader dbTableBlockHeader;
	/**
	 * block number
	 */
	private final int blockNumber;
	/**
	 * fields
	 */
	private final List<DBTableField> fields;
	/**
	 * records
	 */
	private List<DBTableRecord> records;
	/**
	 * records per block
	 */
	private final int recordsPerBlock;

	public DBTableBlock(int blockNumber, int recordsPerBlock, List<DBTableField> fields) {
		this.blockNumber = blockNumber;
		this.fields = fields;
		this.recordsPerBlock = recordsPerBlock;
	}

	public int getBlockNumber() {
		return blockNumber;
	}

	public DBTableBlockHeader getDbTableBlockHeader() {
		return dbTableBlockHeader;
	}

	public List<DBTableField> getFields() {
		return fields;
	}

	public DBTableBlockHeader getPdxFileBlockHeader() {
		return dbTableBlockHeader;
	}

	public List<DBTableRecord> getRecords() {
		return records;
	}

	public int getRecordsPerBlock() {
		return recordsPerBlock;
	}

	/**
	 * read data. This assumes that the inputStream is on byte 0 from the start of the block.
	 * <p>
	 * Read the block's records. A block need not be full, and blocks before the last are often not: Paradox leaves room after deletes and splits. The block header says how many records it holds, as the
	 * offset of the last one; anything after that is free space, which may still contain the bytes of a deleted record.
	 */
	public void read(PDXTableListener pdxReaderListener, InputStream inputStream, int recordSize) throws PDXReaderException {
		try {
			records = new ArrayList<DBTableRecord>();
			/*
			 * read the header
			 */
			final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(inputStream);
			readHeader(littleEndianDataInputStream);
			/*
			 * read the records the block holds
			 */
			final int numrecords = recordsInBlock(dbTableBlockHeader.getOffsetLastRecord(), recordSize);
			for (int i = 0; i < numrecords; i++) {
				final DBTableRecord pdxTableRecord = new DBTableRecord();
				pdxTableRecord.read(pdxReaderListener, fields, inputStream);
				records.add(pdxTableRecord);
			}
		} catch (final Exception e) {
			throw new PDXReaderException("Exception in read", e);
		}
	}

	/**
	 * The offset of the last record is a signed short: an empty block holds minus the record size.
	 */
	public static int recordsInBlock(int offsetLastRecord, int recordSize) {
		final int offset = (short) offsetLastRecord;
		return offset < 0 ? 0 : (offset / recordSize) + 1;
	}

	/**
	 * read header
	 */
	private void readHeader(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
		try {
			dbTableBlockHeader = new DBTableBlockHeader();
			dbTableBlockHeader.read(littleEndianDataInputStream);
		} catch (final Exception e) {
			throw new PDXReaderException("Exception in readHeader", e);
		}
	}

	public void setDbTableBlockHeader(DBTableBlockHeader dbTableBlockHeader) {
		this.dbTableBlockHeader = dbTableBlockHeader;
	}

	public void setPdxFileBlockHeader(DBTableBlockHeader pdxFileBlockHeader) {
		dbTableBlockHeader = pdxFileBlockHeader;
	}

	public void setRecords(List<DBTableRecord> records) {
		this.records = records;
	}
}
