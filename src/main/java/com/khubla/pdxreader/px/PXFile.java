package com.khubla.pdxreader.px;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.api.PDXReaderException;
import com.khubla.pdxreader.api.PDXReaderListener;

/**
 * @author tom
 */
public class PXFile {
   /**
    * max header size
    */
   private final static int MAX_HEADER_SIZE = 10240;
   /**
    * header
    */
   private PXFileHeader pxFileHeader;

   /**
    * ctor
    */
   public PXFile() {
   }

   /**
    * read
    */
   public void read(File file, PDXReaderListener pdxReaderListener) throws PDXReaderException {
      try {
         /*
          * start
          */
         pdxReaderListener.start();
         /*
          * set up streams
          */
         final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
         final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(bufferedInputStream);
         /*
          * mark and read the headers
          */
         bufferedInputStream.mark(MAX_HEADER_SIZE);
         readHeaders(littleEndianDataInputStream);
         /*
          * call the api
          */
         // pdxReaderListener.header(dbTableHeader);
         /*
          * read the block data
          */
         bufferedInputStream.reset();
         // readBlocks(bufferedInputStream, pdxReaderListener);
         /*
          * done
          */
         pdxReaderListener.finish();
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in read", e);
      }
   }
   /**
    * read block data
    */

   // private void readBlocks(BufferedInputStream bufferedInputStream, PDXReaderListener pdxReaderListener) throws PDXReaderException {
   // try {
   // /*
   // * init the array
   // */
   // blocks = new Hashtable<Integer, DBTableBlock>();
   // /*
   // * skip to the first block
   // */
   // bufferedInputStream.skip(dbTableHeader.getBlockSize().getValue() * 1024);
   // /*
   // * walk blocks
   // */
   // final int blocksInUse = dbTableHeader.getBlocksInUse();
   // for (int i = 0; i < blocksInUse; i++) {
   // /*
   // * block
   // */
   // final DBTableBlock pdxTableBlock = new DBTableBlock(i + 1, dbTableHeader.calculateRecordsPerBlock(), dbTableHeader.getFields());
   // /*
   // * mark at the start of the block
   // */
   // bufferedInputStream.mark(MAX_BLOCK_SIZE);
   // /*
   // * read the block data
   // */
   // pdxTableBlock.read(pdxReaderListener, bufferedInputStream);
   // /*
   // * store it. blocks are numbered from 1, not from 0.
   // */
   // blocks.put(pdxTableBlock.getBlockNumber(), pdxTableBlock);
   // /*
   // * reset to the start of the block
   // */
   // bufferedInputStream.reset();
   // /*
   // * skip ahead to next block
   // */
   // bufferedInputStream.skip(dbTableHeader.getBlockSize().getValue() * 1024);
   // }
   // } catch (final Exception e) {
   // throw new PDXReaderException("Exception in readBlocks", e);
   // }
   // }
   /**
    * read
    */
   private void readHeaders(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
      try {
         /*
          * read header
          */
         pxFileHeader = new PXFileHeader();
         pxFileHeader.read(littleEndianDataInputStream);
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in readHeaders", e);
      }
   }
}
