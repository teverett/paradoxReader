package com.khubla.pdxreader.db;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.khubla.pdxreader.api.PDXReaderListener;

/**
 * @author tom
 */
public class DBTableRecord {
   /**
    * read one record
    */
   public void read(PDXReaderListener pdxReaderListener, List<DBTableField> fields, InputStream inputStream) throws Exception {
      try {
         final List<DBTableValue> values = new ArrayList<DBTableValue>();
         for (final DBTableField pdxTableField : fields) {
            final DBTableValue pdxTableValue = new DBTableValue();
            pdxTableValue.read(pdxTableField, inputStream);
            values.add(pdxTableValue);
         }
         pdxReaderListener.record(values);
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }
}
