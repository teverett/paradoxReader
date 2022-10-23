package com.khubla.pdxreader.api;

import java.util.*;

import com.khubla.pdxreader.db.*;

/**
 * @author tom
 */
public interface PDXTableListener {
	void finish();

	void header(DBTableHeader pdxTableHeader);

	void record(List<DBTableValue> values);

	void start(String filename);
}
