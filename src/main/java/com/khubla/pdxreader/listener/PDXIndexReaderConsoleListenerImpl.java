package com.khubla.pdxreader.listener;

import java.util.Date;

import com.khubla.pdxreader.api.PDXIndexListener;
import com.khubla.pdxreader.px.PXFileHeader;
import com.khubla.pdxreader.px.block.PXIndexBlockRecord;

/**
 * @author tom
 */
public class PDXIndexReaderConsoleListenerImpl implements PDXIndexListener {
   /**
    * total records
    */
   private int totalRecords = 0;

   @Override
   public void finish() {
      System.out.println("# total records " + totalRecords);
   }

   @Override
   public void header(PXFileHeader pxFileHeader) {
   }

   @Override
   public void record(PXIndexBlockRecord pxIndexBlockRecord) {
      /*
       * count the record
       */
      totalRecords++;
      System.out.println("blockNumberForKey: " + pxIndexBlockRecord.getBlockNumberForKey() + " statistics:" + pxIndexBlockRecord.getStatistics() + " unknown:" + pxIndexBlockRecord.getUnknown());
   }

   @Override
   public void start() {
      System.out.println("# generated " + new Date().toString());
   }
}
