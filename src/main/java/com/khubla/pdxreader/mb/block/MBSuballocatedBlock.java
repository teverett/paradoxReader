package com.khubla.pdxreader.mb.block;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.mb.MBTableBlock;

public class MBSuballocatedBlock extends MBTableBlock {
   public MBSuballocatedBlock() {
      super(MBTableBlock.RecordType.suballocated);
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
   }
}
