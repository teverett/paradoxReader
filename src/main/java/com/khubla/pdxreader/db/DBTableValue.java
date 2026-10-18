package com.khubla.pdxreader.db;

import java.io.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.db.DBTableField.*;
import com.khubla.pdxreader.util.*;

/**
 * @author tom
 */
public class DBTableValue {
	/**
	 * the value
	 */
	private String value;
	/**
	 * the type
	 */
	private DBTableField.FieldType type;

	public DBTableField.FieldType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	/**
	 * Read a table field
	 */
	public void read(DBTableField pdxTableField, InputStream inputStream) throws PDXReaderException {
		try {
			/*
			 * get the data
			 */
			final byte[] data = new byte[pdxTableField.getLength()];
			final int bytesRead = inputStream.read(data);
			if (bytesRead > 0) {
				/*
				 * convert to type
				 */
				final FieldType fieldType = pdxTableField.getFieldType();
				switch (fieldType) {
					case A:
						value = StringUtil.readString(data);
						break;
					case D:
					case I:
					case S:
					case Auto:
						value = toString(ParadoxNumber.decodeInteger(data));
						break;
					case C:
					case N:
						/*
						 * currency is stored exactly as a number is
						 */
						value = toString(ParadoxNumber.decodeDouble(data));
						break;
					case M:
						/*
						 * Memo
						 */
						value = StringUtil.byteArrayToString(data);
						break;
					case L:
						/*
						 * Logical
						 */
						value = StringUtil.byteArrayToString(data);
						break;
					case B:
						/*
						 * Binary
						 */
						value = StringUtil.byteArrayToString(data);
						break;
					case O:
						/*
						 * OLE
						 */
						value = StringUtil.byteArrayToString(data);
						break;
					case E:
						/*
						 * formatted memo
						 */
						value = StringUtil.byteArrayToString(data);
						break;
					case G:
						/*
						 * Graphic
						 */
						value = StringUtil.byteArrayToString(data);
						break;
					case BCD:
						/*
						 * BCD
						 */
						value = StringUtil.byteArrayToString(data);
						break;
					case Bytes:
						/*
						 * Bytes
						 */
						value = StringUtil.byteArrayToString(data);
						break;
					case T:
						value = ParadoxTime.getTimeFromParadoxTime(data);
						break;
					case TS:
						value = ParadoxTime.getTimeFromParadoxTime(data);
						break;
					default:
						throw new Exception("Unknown field type '" + fieldType.name() + "'");
				}
			}
		} catch (final Exception e) {
			throw new PDXReaderException("Exception in read", e);
		}
	}

	private static String toString(Object number) {
		return null == number ? null : number.toString();
	}

	public void setType(DBTableField.FieldType type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
