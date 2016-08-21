package com.khubla.pdxreader.mb.block;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.mb.MBTableBlock;

public class MBSuballocatedBlock extends MBTableBlock {
   public MBSuballocatedBlock() {
      super(MBTableBlock.RecordType.suballocated);
   }

   @Override
   public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws Exception {
      // sizeofBlock = littleEndianDataInputStream.readUnsignedShort();
      // modificationCount = littleEndianDataInputStream.readUnsignedShort();
   }
}
