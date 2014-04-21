package com.khubla.pdxreader.mb;

import com.google.common.io.LittleEndianDataInputStream;

/**
 * @author tom
 */
public class MBTableBlobHeader {
   /**
    * record type
    */
   private int recordType;
   /**
    * size of block divided by 4k
    */
   private int blockSize;
   /**
    * modification count
    */
   private int modificationCount;

   public int getBlockSize() {
      return blockSize;
   }

   public int getModificationCount() {
      return modificationCount;
   }

   public int getRecordType() {
      return recordType;
   }

   public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws Exception {
      try {
         recordType = littleEndianDataInputStream.readByte();
         blockSize = littleEndianDataInputStream.readUnsignedShort();
         modificationCount = littleEndianDataInputStream.readUnsignedShort();
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }

   public void setBlockSize(int blockSize) {
      this.blockSize = blockSize;
   }

   public void setModificationCount(int modificationCount) {
      this.modificationCount = modificationCount;
   }

   public void setRecordType(int recordType) {
      this.recordType = recordType;
   }
}
