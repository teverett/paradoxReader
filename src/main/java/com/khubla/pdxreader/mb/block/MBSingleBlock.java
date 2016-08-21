package com.khubla.pdxreader.mb.block;

import java.io.IOException;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.mb.MBTableBlock;

public class MBSingleBlock extends MBTableBlock {
   public MBSingleBlock() {
      super(MBTableBlock.RecordType.single);
   }

   @Override
   public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws IOException {
      // sizeofBlock = littleEndianDataInputStream.readUnsignedShort();
      // modificationCount = littleEndianDataInputStream.readUnsignedShort();
   }
}
