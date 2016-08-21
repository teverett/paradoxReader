package com.khubla.pdxreader.mb.block;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.mb.MBTableBlock;

public class MBFreeBlock extends MBTableBlock {
   public MBFreeBlock() {
      super(MBTableBlock.RecordType.free);
   }

   @Override
   public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws Exception {
      // sizeofBlock = littleEndianDataInputStream.readUnsignedShort();
      // modificationCount = littleEndianDataInputStream.readUnsignedShort();
   }
}
