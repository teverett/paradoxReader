package com.khubla.pdxreader.mb;

import com.google.common.io.LittleEndianDataInputStream;

/**
 * @author tom
 */
public class MBTableHeader {
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

   /**
    * MB block header size
    */
   public final static int MB_BLOCK_HEADER_SIZE = 4 * 1024;;
   /**
    * record type
    */
   private RecordType recordType;
   /**
    * number blocks
    */
   private int numberBlocks;

   public int getNumberBlocks() {
      return numberBlocks;
   }

   public RecordType getRecordType() {
      return recordType;
   }

   /**
    * read (little endian)
    */
   public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws Exception {
      try {
         final int type = littleEndianDataInputStream.readByte();
         switch (type) {
            case 0:
               recordType = RecordType.header;
               break;
            case 2:
               recordType = RecordType.single;
               break;
            case 3:
               recordType = RecordType.suballocated;
               break;
            case 4:
               recordType = RecordType.free;
               break;
            default:
               throw new Exception("Unknown record type '" + type + "'");
         }
         numberBlocks = littleEndianDataInputStream.readUnsignedShort();
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }

   public void setNumberBlocks(int numberBlocks) {
      this.numberBlocks = numberBlocks;
   }

   public void setRecordType(RecordType recordType) {
      this.recordType = recordType;
   }
}
