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
    * read blobs data
    */
   private void preReadBlobs(File file) throws Exception {
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
         preReadBlobs(file);
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }
}
