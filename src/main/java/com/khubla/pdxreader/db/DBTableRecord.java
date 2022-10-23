package com.khubla.pdxreader.db;

import java.io.*;
import java.util.*;

import com.khubla.pdxreader.api.*;

/**
 * @author tom
 */
public class DBTableRecord {
	/**
	 * read one record
	 */
	public void read(PDXTableListener pdxReaderListener, List<DBTableField> fields, InputStream inputStream) throws PDXReaderException {
		try {
			final List<DBTableValue> values = new ArrayList<DBTableValue>();
			for (final DBTableField pdxTableField : fields) {
				final DBTableValue pdxTableValue = new DBTableValue();
				pdxTableValue.read(pdxTableField, inputStream);
				values.add(pdxTableValue);
			}
			pdxReaderListener.record(values);
		} catch (final Exception e) {
			throw new PDXReaderException("Exception in read", e);
		}
	}
}
