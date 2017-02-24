package com.khubla.pdxreader.px;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.api.PDXReaderException;

/**
 * @author tom
 */
public class PXFileBlock {
   /**
    * header
    */
   private PXFileBlockHeader pxFileBlockHeader;
   /**
    * block number
    */
   private final int blockNumber;

   public PXFileBlock(int blockNumber) {
      this.blockNumber = blockNumber;
   }

   public int getBlockNumber() {
      return blockNumber;
   }

   /**
    * Index Records
    */
   private List<PXFileIndexRecord> indexRecords;

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
         indexRecords = new ArrayList<PXFileIndexRecord>();
         PXFileIndexRecord pxFileIndexRecord = new PXFileIndexRecord();
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
         pxFileBlockHeader = new PXFileBlockHeader();
         pxFileBlockHeader.read(littleEndianDataInputStream);
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in readHeader", e);
      }
   }
}
