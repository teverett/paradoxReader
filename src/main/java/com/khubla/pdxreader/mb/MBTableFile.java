package com.khubla.pdxreader.mb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.LittleEndianDataInputStream;

/**
 * @author tom
 */
public class MBTableFile {
   /**
    * blocks
    */
   private final List<MBTableBlock> blocks = new ArrayList<MBTableBlock>();

   /**
    * This method finds all the blocks with their types and offsets
    */
   private void preReadBlocks(File file) throws Exception {
      try {
         /*
          * set up streams
          */
         final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
         final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(bufferedInputStream);
         /*
          * offset tracker
          */
         int fileOffset = 0;
         /*
          * loop
          */
         while (littleEndianDataInputStream.available() > 1) {
            /*
             * read type
             */
            final int type = littleEndianDataInputStream.readByte();
            /*
             * get an appropriate Block type
             */
            final MBTableBlock mbTableBlock = MBTableBlockFactory.getMBTableBlock(MBTableBlock.getRecordType(type));
            /*
             * set the offset
             */
            mbTableBlock.setFileOffset(fileOffset);
            /*
             * pre-read
             */
            final int bytesPreRead = mbTableBlock.preRead(littleEndianDataInputStream);
            /*
             * add to list
             */
            blocks.add(mbTableBlock);
            /*
             * skip forward to next one
             */
            final int bytesToSkip = mbTableBlock.getSizeofBlock() - (1 + bytesPreRead);
            littleEndianDataInputStream.skip(bytesToSkip);
            /*
             * update the offset
             */
            fileOffset += mbTableBlock.getSizeofBlock();
         }
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }

   /**
    * read
    */
   public void read(File file) throws Exception {
      try {
         /*
          * pre-read
          */
         preReadBlocks(file);
         /*
          * read
          */
         readBlocks(file);
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }

   /**
    * Read all blocks
    */
   private void readBlocks(File file) throws Exception {
      try {
         for (final MBTableBlock mbTableBlock : blocks) {
            /*
             * set up streams
             */
            final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            final LittleEndianDataInputStream littleEndianDataInputStream = new LittleEndianDataInputStream(bufferedInputStream);
            /*
             * ffd
             */
            littleEndianDataInputStream.skip(mbTableBlock.getFileOffset());
            /*
             * read
             */
            mbTableBlock.read(littleEndianDataInputStream);
         }
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }
}
