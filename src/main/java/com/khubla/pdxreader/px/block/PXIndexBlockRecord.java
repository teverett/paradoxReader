package com.khubla.pdxreader.px.block;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.api.PDXReaderException;

public class PXIndexBlockRecord {
   private int blockNumberForKey;
   private int statistics;
   private int unknown;

   public int getBlockNumberForKey() {
      return blockNumberForKey;
   }

   public int getStatistics() {
      return statistics;
   }

   public int getUnknown() {
      return unknown;
   }

   public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
      try {
         blockNumberForKey = littleEndianDataInputStream.readInt();
         statistics = littleEndianDataInputStream.readInt();
         unknown = littleEndianDataInputStream.readInt();
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in read", e);
      }
   }

   public void setBlockNumberForKey(int blockNumberForKey) {
      this.blockNumberForKey = blockNumberForKey;
   }

   public void setStatistics(int statistics) {
      this.statistics = statistics;
   }

   public void setUnknown(int unknown) {
      this.unknown = unknown;
   }
}
