package com.khubla.pdxreader.px;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.api.PDXReaderException;
import com.khubla.pdxreader.px.block.PXIndexBlock;

/**
 * @author tom
 */
public class PXFile {
   /**
    * max header size
    */
   private final static int MAX_HEADER_SIZE = 10240;
   /**
    * max block size
    */
   private final static int MAX_BLOCK_SIZE = 10240;
   /**
    * header
    */
   private PXFileHeader pxFileHeader;
   /**
    * blocks
    */
   private List<PXIndexBlock> blocks;

   /**
    * ctor
    */
   public PXFile() {
   }

   /**
    * read
    */
   public void read(File file) throws PDXReaderException {
      try {
         /*
          * set up streams
          */
         final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
         final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(bufferedInputStream);
         try {
            /*
             * mark and read the headers
             */
            bufferedInputStream.mark(MAX_HEADER_SIZE);
            readHeaders(littleEndianDataInputStream);
            /*
             * read the block data
             */
            bufferedInputStream.reset();
            readBlocks(bufferedInputStream);
         } finally {
            littleEndianDataInputStream.close();
            bufferedInputStream.close();
         }
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in read", e);
      }
   }

   /**
    * read block data
    */
   private void readBlocks(BufferedInputStream bufferedInputStream) throws PDXReaderException {
      try {
         /*
          * init the array
          */
         blocks = new ArrayList<PXIndexBlock>();
         /*
          * skip to the first index block
          */
         bufferedInputStream.skip(pxFileHeader.getBlockSize().getValue() * 1024);
         /*
          * walk index blocks
          */
         final int blocksInUse = pxFileHeader.getBlocksInUse();
         for (int i = 0; i < blocksInUse; i++) {
            /*
             * block
             */
            final PXIndexBlock pxIndexBlock = new PXIndexBlock();
            /*
             * mark at the start of the block
             */
            bufferedInputStream.mark(MAX_BLOCK_SIZE);
            /*
             * read the block data
             */
            pxIndexBlock.read(bufferedInputStream);
            /*
             * store it. blocks are numbered from 1, not from 0.
             */
            blocks.add(pxIndexBlock);
            /*
             * reset to the start of the block
             */
            bufferedInputStream.reset();
            /*
             * skip ahead to next block
             */
            bufferedInputStream.skip(pxFileHeader.getBlockSize().getValue() * 1024);
         }
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in readBlocks", e);
      }
   }

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
