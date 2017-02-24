package com.khubla.pdxreader.px.block;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.api.PDXReaderException;

/**
 * @author tom
 */
public class PXIndexBlock {
   /**
    * header
    */
   private PXIndexBlockHeader pxFileBlockHeader;
   /**
    * Index Records
    */
   private List<PXIndexBlockRecord> indexRecords;

   public PXIndexBlock() {
   }

   /**
    * read data. This assumes that the inputStream is on byte 0 from the start of the block
    */
   public void read(InputStream inputStream) throws PDXReaderException {
      try {
         /*
          * read the header
          */
         final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(inputStream);
         readHeader(littleEndianDataInputStream);
         /*
          * read the records
          */
         indexRecords = new ArrayList<PXIndexBlockRecord>();
         final PXIndexBlockRecord pxFileIndexRecord = new PXIndexBlockRecord();
         pxFileIndexRecord.read(littleEndianDataInputStream);
         indexRecords.add(pxFileIndexRecord);
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in read", e);
      }
   }

   /**
    * read header
    */
   private void readHeader(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
      try {
         pxFileBlockHeader = new PXIndexBlockHeader();
         pxFileBlockHeader.read(littleEndianDataInputStream);
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in readHeader", e);
      }
   }
}
