package com.khubla.pdxreader.listener.sql;

import java.util.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.db.*;

/**
 * @author tom
 */
public class PDXTableReaderSQLListenerImpl implements PDXTableListener {
	/**
	 * total records
	 */
	private int totalRecords = 0;
	/**
	 * filename
	 */
	private String filename;
	/**
	 * SQL schema
	 */
	private SQLTableDesc sqlTableDesc = null;

	@Override
	public void finish() {
		System.out.println("# total records " + totalRecords);
	}

	@Override
	public void header(DBTableHeader pdxTableHeader) {
		try {
			/*
			 * build SQL schema
			 */
			sqlTableDesc = SQLTableDesc.generateSQLTableDesc(filename, pdxTableHeader);
			/*
			 * get the CREATE
			 */
			final String sqlCreate = sqlTableDesc.renderSQLCreate();
			System.out.println(sqlCreate);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void record(List<DBTableValue> values) {
		/*
		 * count the record
		 */
		totalRecords++;
		/*
		 * get the INSERT
		 */
		final String sqlInsert = sqlTableDesc.renderSQLInsert(values);
		System.out.println(sqlInsert);
	}

	@Override
	public void start(String filename) {
		this.filename = filename;
		System.out.println("# '" + filename + "' generated " + new Date().toString());
	}
}
