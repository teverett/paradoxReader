package com.khubla.pdxreader.mb;

import java.io.IOException;

import com.google.common.io.LittleEndianDataInputStream;

public abstract class MBTableBlock {
   /**
    * record type
    */
   public static enum RecordType {
      header(0), single(2), suballocated(3), free(4);
      private int value;

      private RecordType(int value) {
         this.value = value;
      }

      public int getValue() {
         return value;
      }

      public void setValue(int value) {
         this.value = value;
      }
   }

   protected static final int BLOCK_SIZE_MULTIPLIER = 4096;

   public static RecordType getRecordType(int type) throws Exception {
      switch (type) {
         case -1:
            // no idea what a -1 record type is...
            return null;
         case 0:
            return RecordType.header;
         case 2:
            return RecordType.single;
         case 3:
            return RecordType.suballocated;
         case 4:
            return RecordType.free;
         default:
            throw new Exception("Unknown record type '" + type + "'");
      }
   }

   /**
    * offset of this block from the start of the file
    */
   protected int fileOffset;
   /**
    * record type
    */
   protected final RecordType recordType;
   /**
    * sizeofBlock
    */
   protected int sizeofBlock; // Size of block divided by 4k (1 because the header is 4k)

   public MBTableBlock(RecordType recordType) {
      this.recordType = recordType;
   }

   public int getFileOffset() {
      return fileOffset;
   }

   public RecordType getRecordType() {
      return recordType;
   }

   public int getSizeofBlock() {
      return sizeofBlock;
   }

   public int preRead(LittleEndianDataInputStream littleEndianDataInputStream) throws IOException {
      sizeofBlock = BLOCK_SIZE_MULTIPLIER * littleEndianDataInputStream.readUnsignedShort();
      return 2;
   }

   public abstract void read(LittleEndianDataInputStream littleEndianDataInputStream) throws Exception;

   public void setFileOffset(int fileOffset) {
      this.fileOffset = fileOffset;
   }

   public void setSizeofBlock(int sizeofBlock) {
      this.sizeofBlock = sizeofBlock;
   }
}
