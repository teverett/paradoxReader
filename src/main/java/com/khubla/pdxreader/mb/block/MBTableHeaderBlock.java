package com.khubla.pdxreader.mb.block;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.mb.MBTableBlock;

public class MBTableHeaderBlock extends MBTableBlock {
   /**
    * modification count
    */
   private int modificationCount;

   public MBTableHeaderBlock() {
      super(MBTableBlock.RecordType.header);
   }

   public int getModificationCount() {
      return modificationCount;
   }

   @Override
   public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws Exception {
      final int blockType = littleEndianDataInputStream.readByte();
      if (blockType != super.recordType.getValue()) {
         throw new Exception("Block type mismatch");
      }
      final int sizeofBlock = littleEndianDataInputStream.readUnsignedShort();
      if ((sizeofBlock * BLOCK_SIZE_MULTIPLIER) != super.sizeofBlock) {
         throw new Exception("Block type mismatch");
      }
      modificationCount = littleEndianDataInputStream.readUnsignedShort();
   }

   public void setModificationCount(int modificationCount) {
      this.modificationCount = modificationCount;
   }
}
