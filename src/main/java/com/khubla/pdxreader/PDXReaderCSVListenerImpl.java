package com.khubla.pdxreader;

import java.util.Date;
import java.util.List;

import com.khubla.pdxreader.api.PDXReaderListener;
import com.khubla.pdxreader.db.DBTableField;
import com.khubla.pdxreader.db.DBTableHeader;
import com.khubla.pdxreader.db.DBTableValue;

/**
 * @author tom
 */
public class PDXReaderCSVListenerImpl implements PDXReaderListener {
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
      boolean first = true;
      // System.out.println("Embedded Name: " + pdxTableHeader.getEmbeddedFilename());
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
   public void start() {
      System.out.println("# generated " + new Date().toString());
   }
}
