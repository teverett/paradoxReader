package com.khubla.pdxreader.listener;

import java.util.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.px.*;
import com.khubla.pdxreader.px.block.*;

/**
 * @author tom
 */
public class PDXIndexReaderConsoleListenerImpl implements PDXIndexListener {
	/**
	 * total records
	 */
	private int totalRecords = 0;

	@Override
	public void finish() {
		System.out.println("# total records " + totalRecords);
	}

	@Override
	public void header(PXFileHeader pxFileHeader) {
	}

	@Override
	public void record(PXIndexBlockRecord pxIndexBlockRecord) {
		/*
		 * count the record
		 */
		totalRecords++;
		System.out.println("blockNumberForKey: " + pxIndexBlockRecord.getBlockNumberForKey() + " statistics:" + pxIndexBlockRecord.getStatistics() + " unknown:" + pxIndexBlockRecord.getUnknown());
	}

	@Override
	public void start() {
		System.out.println("# generated " + new Date().toString());
	}
}
