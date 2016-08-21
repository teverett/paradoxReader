package com.khubla.pdxreader.mb;

import com.khubla.pdxreader.mb.block.MBFreeBlock;
import com.khubla.pdxreader.mb.block.MBSingleBlock;
import com.khubla.pdxreader.mb.block.MBSuballocatedBlock;
import com.khubla.pdxreader.mb.block.MBTableHeaderBlock;

public class MBTableBlockFactory {
   public static MBTableBlock getMBTableBlock(MBTableBlock.RecordType recordType) throws Exception {
      switch (recordType) {
         case header:
            return new MBTableHeaderBlock();
         case single:
            return new MBSingleBlock();
         case suballocated:
            return new MBSuballocatedBlock();
         case free:
            return new MBFreeBlock();
         default:
            throw new Exception("Unknown record type '" + recordType + "'");
      }
   }
}
