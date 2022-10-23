package com.khubla.pdxreader.listener.sql;

import java.util.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.db.*;

/**
 * SQL Table
 *
 * @author tom
 */
public class SQLTableDesc {
	/**
	 * generate SQLTableDesc from DBTableHeader
	 *
	 * @throws PDXReaderException
	 */
	public static SQLTableDesc generateSQLTableDesc(String filename, DBTableHeader pdxTableHeader) throws PDXReaderException {
		final SQLTableDesc ret = new SQLTableDesc(generateTableName(filename));
		/*
		 * build the table fields
		 */
		for (final DBTableField pdxTableField : pdxTableHeader.getFields()) {
			ret.add(SQLRowDesc.generateSQLRowDesc(pdxTableField));
		}
		return ret;
	}

	/**
	 * create table name from file name
	 */
	private static String generateTableName(String filename) {
		return SQLSanitize.sanitize(filename.substring(0, filename.indexOf('.')));
	}

	/**
	 * create template
	 */
	private final String CREATE_TEMPLATE = "CREATE TABLE %s (%s);";
	/**
	 * insert template
	 */
	private final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s);";
	/**
	 * SQL table name
	 */
	private final String sqlTableName;
	/**
	 * the rows
	 */
	private final List<SQLRowDesc> rows = new ArrayList<SQLRowDesc>();

	public SQLTableDesc(String tableName) {
		super();
		sqlTableName = tableName;
	}

	public void add(SQLRowDesc sqlRowDesc) {
		rows.add(sqlRowDesc);
	}

	/**
	 * generate column name list
	 */
	public String generateColumnNameList() {
		String columnList = "";
		for (final SQLRowDesc sqlRowDesc : rows) {
			if (columnList.length() != 0) {
				columnList += ",";
			}
			columnList += sqlRowDesc.getName();
		}
		return columnList;
	}

	public List<SQLRowDesc> getRows() {
		return rows;
	}

	public String getTableName() {
		return sqlTableName;
	}

	/**
	 * generate the SQL CREATE
	 */
	public String renderSQLCreate() {
		String columnList = "";
		for (final SQLRowDesc sqlRowDesc : rows) {
			if (columnList.length() != 0) {
				columnList += ",";
			}
			columnList += sqlRowDesc.getName() + " " + sqlRowDesc.getType();
		}
		return String.format(CREATE_TEMPLATE, sqlTableName, columnList);
	}

	/**
	 * generate the SQL INSERT
	 */
	public String renderSQLInsert(List<DBTableValue> values) {
		/*
		 * values
		 */
		String fields = "";
		int i = 0;
		for (final DBTableValue pdxTableValue : values) {
			if (fields.length() != 0) {
				fields += ",";
			}
			/*
			 * get value
			 */
			final String value = SQLRowDesc.getSQLValue(rows.get(i++).getFieldType(), pdxTableValue.getValue());
			/*
			 * append
			 */
			fields += "\"" + value + "\"";
		}
		/*
		 * insert
		 */
		return String.format(INSERT_TEMPLATE, sqlTableName, generateColumnNameList(), fields);
	}
}
