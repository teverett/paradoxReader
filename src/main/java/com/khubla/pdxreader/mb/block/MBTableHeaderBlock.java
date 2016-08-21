package com.khubla.pdxreader.mb.block;

import java.io.IOException;

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
   public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws IOException {
      // sizeofBlock = littleEndianDataInputStream.readUnsignedShort();
      // modificationCount = littleEndianDataInputStream.readUnsignedShort();
   }

   public void setModificationCount(int modificationCount) {
      this.modificationCount = modificationCount;
   }
}
