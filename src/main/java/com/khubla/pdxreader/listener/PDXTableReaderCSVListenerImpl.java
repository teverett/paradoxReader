package com.khubla.pdxreader.listener;

import java.util.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.db.*;

/**
 * @author tom
 */
public class PDXTableReaderCSVListenerImpl implements PDXTableListener {
	/**
	 * total records
	 */
	private int totalRecords = 0;

	@Override
	public void finish() {
		System.out.println("# total records " + totalRecords);
	}

	@Override
	public void header(DBTableHeader pdxTableHeader) {
		/*
		 * show the table fields
		 */
		boolean first = true;
		for (final DBTableField pdxTableField : pdxTableHeader.getFields()) {
			if (first) {
				first = false;
			} else {
				System.out.print(",");
			}
			System.out.print(pdxTableField.getName());
		}
		System.out.println();
	}

	@Override
	public void record(List<DBTableValue> values) {
		/*
		 * count the record
		 */
		totalRecords++;
		/*
		 * dump the record
		 */
		boolean first = true;
		for (final DBTableValue pdxTableValue : values) {
			if (first) {
				first = false;
			} else {
				System.out.print(",");
			}
			System.out.print(pdxTableValue.getValue());
		}
		System.out.println();
	}

	@Override
	public void start(String filename) {
		System.out.println("# '" + filename + "' generated " + new Date().toString());
	}
}
