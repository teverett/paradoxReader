package com.khubla.pdxreader.api;

import java.util.List;

import com.khubla.pdxreader.db.DBTableHeader;
import com.khubla.pdxreader.db.DBTableValue;

/**
 * @author tom
 */
public interface PDXReaderListener {
   void finish();

   void header(DBTableHeader pdxTableHeader);

   void record(List<DBTableValue> values);

   void start();
}
