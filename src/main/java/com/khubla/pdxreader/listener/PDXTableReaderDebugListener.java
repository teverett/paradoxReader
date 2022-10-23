package com.khubla.pdxreader.listener;

import java.util.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.db.*;

/**
 * @author tom
 */
public class PDXTableReaderDebugListener implements PDXTableListener {
	@Override
	public void finish() {
	}

	@Override
	public void header(DBTableHeader pdxTableHeader) {
		/*
		 * show the header data
		 */
		System.out.println("recordBufferSize: " + pdxTableHeader.getRecordBufferSize());
		System.out.println("headerBlockSize: " + pdxTableHeader.getHeaderBlockSize());
		System.out.println("tableType: " + pdxTableHeader.getTableType());
		System.out.println("dataBlockSizeCode: " + pdxTableHeader.getDataBlockSizeCode());
		System.out.println("numberRecords: " + pdxTableHeader.getNumberRecords());
		System.out.println("blocksInUse: " + pdxTableHeader.getBlocksInUse());
		System.out.println("totalBlocksInFile: " + pdxTableHeader.getTotalBlocksInFile());
		System.out.println("firstDataBlock: " + pdxTableHeader.getFirstDataBlock());
		System.out.println("lastDataBlock: " + pdxTableHeader.getLastDataBlock());
		System.out.println("modified1: " + pdxTableHeader.getModified1());
		System.out.println("indexFieldNumber: " + pdxTableHeader.getIndexFieldNumber());
		System.out.println("primaryIndexWorkspace: " + pdxTableHeader.getPrimaryIndexWorkspace());
		System.out.println("indexRoot: " + pdxTableHeader.getIndexRoot());
		System.out.println("numIndexLevels: " + pdxTableHeader.getNumIndexLevels());
		System.out.println("numberFields: " + pdxTableHeader.getNumberFields());
		System.out.println("numberKeyFields: " + pdxTableHeader.getNumberKeyFields());
		System.out.println("sortOrder: " + pdxTableHeader.getSortOrder());
		System.out.println("modified2: " + pdxTableHeader.getModified2());
		System.out.println("change1: " + pdxTableHeader.getChange1());
		System.out.println("change2: " + pdxTableHeader.getChange2());
		System.out.println("writeProtected: " + pdxTableHeader.getWriteProtected());
		System.out.println("fileVersionID: " + pdxTableHeader.getFileVersionID());
		System.out.println("maxBlocks: " + pdxTableHeader.getMaxBlocks());
		System.out.println("auxPasswords: " + pdxTableHeader.getAuxPasswords());
		System.out.println("autoInc: " + pdxTableHeader.getAutoInc());
		System.out.println("indexUpdateRequired: " + pdxTableHeader.getIndexUpdateRequired());
		System.out.println("refIntegrity: " + pdxTableHeader.getRefIntegrity());
		System.out.println("embeddedFilename: " + pdxTableHeader.getEmbeddedFilename());
		System.out.println("unk1: " + pdxTableHeader.getUnk1());
		System.out.println("unk2: " + pdxTableHeader.getUnk2());
		System.out.println("unk3: " + pdxTableHeader.getUnk3());
		System.out.println("unk4: " + pdxTableHeader.getUnk4());
		System.out.println("unk5: " + pdxTableHeader.getUnk5());
		System.out.println("unk6: " + pdxTableHeader.getUnk6());
		System.out.println("unk7: " + pdxTableHeader.getUnk7());
		System.out.println("unk8: " + pdxTableHeader.getUnk8());
		System.out.println("unk9: " + pdxTableHeader.getUnk9());
		System.out.println("unk10: " + pdxTableHeader.getUnk10());
		System.out.println("unk11: " + pdxTableHeader.getUnk11());
		System.out.println("unk12: " + pdxTableHeader.getUnk12());
		System.out.println("unk13: " + pdxTableHeader.getUnk13());
	}

	@Override
	public void record(List<DBTableValue> values) {
	}

	@Override
	public void start(String filename) {
	}
}
