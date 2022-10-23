package com.khubla.pdxreader.listener.sql;

import java.nio.charset.*;
import java.text.*;
import java.util.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.db.*;
import com.khubla.pdxreader.db.DBTableField.*;
import com.khubla.pdxreader.util.*;

/**
 * SQL Row
 *
 * @author tom
 */
public class SQLRowDesc {
	/**
	 * time format for SQL
	 */
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * generate SQLRowDesc from DBTableField
	 *
	 * @throws PDXReaderException
	 */
	public static SQLRowDesc generateSQLRowDesc(DBTableField pdxTableField) throws PDXReaderException {
		return new SQLRowDesc(SQLSanitize.sanitize(pdxTableField.getName()), mapTypes(pdxTableField.getFieldType()), pdxTableField.getFieldType());
	}

	/**
	 * get the SQL value for a given Paradox value
	 */
	public static String getSQLValue(FieldType fieldType, String paradoxValue) {
		if (null != paradoxValue) {
			if (fieldType == FieldType.D) {
				final Date date = ParadoxDate.getDateFromParadoxDate(Integer.parseInt(paradoxValue));
				return SIMPLE_DATE_FORMAT.format(date);
			} else if (fieldType == FieldType.T) {
				final Date date = new Date(Integer.parseInt(ParadoxTime.getTimeFromParadoxTime(paradoxValue.getBytes(Charset.forName("UTF-8")))));
				return SIMPLE_DATE_FORMAT.format(date);
			} else {
				return paradoxValue;
			}
		} else {
			return null;
		}
	}

	/**
	 * map Paradox types to SQL types
	 *
	 * @throws PDXReaderException
	 */
	private static String mapTypes(FieldType fieldType) throws PDXReaderException {
		try {
			String ret = "";
			switch (fieldType) {
				case A:
					ret = "VARCHAR(255)";
					break;
				case D:
					ret = "DATE";
					break;
				case S:
					ret = "INTEGER";
					break;
				case I:
					ret = "INTEGER";
					break;
				case C:
					ret = "INTEGER";
					break;
				case N:
					ret = "DOUBLE";
					break;
				case L:
					ret = "INTEGER";
					break;
				case M:
					ret = "BLOB";
					break;
				case B:
					ret = "BLOB";
					break;
				case E:
					ret = "BLOB";
					break;
				case O:
					ret = "BLOB";
					break;
				case G:
					ret = "BLOB";
					break;
				case T:
					ret = "DATETIME";
					break;
				case TS:
					ret = "INTEGER";
					break;
				case Auto:
					ret = "INTEGER";
					break;
				case BCD:
					ret = "INTEGER";
					break;
				case Bytes:
					ret = "BLOB";
					break;
				default:
					throw new PDXReaderException("Unable to map type '" + fieldType + "'");
			}
			return ret;
		} catch (final Exception e) {
			throw new PDXReaderException("Exception in mapTypes", e);
		}
	}

	private final String name;
	private final String type;
	private final FieldType fieldType;

	public SQLRowDesc(String name, String type, FieldType fieldType) {
		super();
		this.name = name;
		this.type = type;
		this.fieldType = fieldType;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
}