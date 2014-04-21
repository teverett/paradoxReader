package com.khubla.pdxreader.mb;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.LittleEndianDataInputStream;

/**
 * @author tom
 */
public class MBTableFile {
   /**
    * max header size
    */
   private final static int MAX_HEADER_SIZE = 10240;
   /**
    * max block size
    */
   private final static int MAX_BLOCK_SIZE = 10240;
   /**
    * headers
    */
   private MBTableHeader mbTableHeader = null;
   /**
    * blocks
    */
   private List<MBTableBlob> blobs;

   public MBTableHeader getMbTableHeader() {
      return mbTableHeader;
   }

   /**
    * read
    */
   public void read(InputStream inputStream) throws Exception {
      try {
         /*
          * set up streams
          */
         final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
         final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(bufferedInputStream);
         /*
          * mark and read the headers
          */
         bufferedInputStream.mark(MAX_HEADER_SIZE);
         readHeaders(littleEndianDataInputStream);
         /*
          * read the block data
          */
         bufferedInputStream.reset();
         readBlobs(bufferedInputStream);
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }

   /**
    * read blobs data
    */
   private void readBlobs(BufferedInputStream bufferedInputStream) throws Exception {
      try {
         /*
          * init the array
          */
         blobs = new ArrayList<MBTableBlob>();
         /*
          * skip over the header block
          */
         bufferedInputStream.skip(MBTableHeader.MB_BLOCK_HEADER_SIZE);
         /*
          * walk blocks
          */
         final int totalBlocks = mbTableHeader.getNumberBlocks();
         for (int i = 0; i < totalBlocks; i++) {
            /*
             * blob
             */
            final MBTableBlob mbTableBlob = new MBTableBlob();
            /*
             * mark at the start of the block
             */
            bufferedInputStream.mark(MAX_BLOCK_SIZE);
            /*
             * read the block data
             */
            mbTableBlob.read(bufferedInputStream);
            /*
             * store it. blocks are numbered from 1, not from 0.
             */
            blobs.add(mbTableBlob);
            /*
             * reset to the start of the block
             */
            bufferedInputStream.reset();
            /*
             * skip ahead to next block
             */
            bufferedInputStream.skip(MAX_BLOCK_SIZE);
         }
      } catch (final Exception e) {
         throw new Exception("Exception in readBlobs", e);
      }
   }

   /**
    * read
    */
   private void readHeaders(LittleEndianDataInputStream littleEndianDataInputStream) throws Exception {
      try {
         /*
          * read header
          */
         mbTableHeader = new MBTableHeader();
         mbTableHeader.read(littleEndianDataInputStream);
      } catch (final Exception e) {
         throw new Exception("Exception in readHeaders", e);
      }
   }

   public void setMbTableHeader(MBTableHeader mbTableHeader) {
      this.mbTableHeader = mbTableHeader;
   }
}
