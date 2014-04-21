package com.khubla.pdxreader.mb;

import java.io.InputStream;

import com.google.common.io.LittleEndianDataInputStream;

/**
 * @author tom
 */
public class MBTableBlob {
   /**
    * blob header
    */
   private MBTableBlobHeader mbTableBlobHeader;

   public void read(InputStream inputStream) throws Exception {
      try {
         /*
          * read the blob header
          */
         final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(inputStream);
         mbTableBlobHeader = new MBTableBlobHeader();
         mbTableBlobHeader.read(littleEndianDataInputStream);
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }
}
