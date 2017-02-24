package com.khubla.pdxreader.px;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.api.PDXReaderException;

/**
 * @author tom
 */
public class PXFileHeader {
   /**
    * block size
    */
   public static enum BlockSize {
      oneK(1), twoK(2), threeK(3), fourK(4);
      private int value;

      private BlockSize(int value) {
         this.value = value;
      }

      public int getValue() {
         return value;
      }

      public void setValue(int value) {
         this.value = value;
      }
   };

   /**
    * Block size
    */
   private BlockSize blockSize;
   /**
    * length of the header block (bytes)
    */
   private int headerBlockSize;
   /**
    * length of index record (-6)
    */
   private int indexRecordLength;
   /**
    * total records
    */
   private long numberRecords;
   /**
    * data block size code. 1 - 1k, 2 - 2k, 3 - 3k, 4 - 4k.
    */
   private int dataBlockSizeCode;
   private int blocksInUse;
   private int totalBlocksInFile;
   private int firstDataBlock;
   private int lastDataBlock;
   private int indexRootBlock;
   private int numberIndexLevels;
   private int numberIndexFields;

   public int getBlocksInUse() {
      return blocksInUse;
   }

   public BlockSize getBlockSize() {
      return blockSize;
   }

   public int getDataBlockSizeCode() {
      return dataBlockSizeCode;
   }

   public int getFirstDataBlock() {
      return firstDataBlock;
   }

   public int getHeaderBlockSize() {
      return headerBlockSize;
   }

   public int getIndexRecordLength() {
      return indexRecordLength;
   }

   public int getLastDataBlock() {
      return lastDataBlock;
   }

   public long getNumberRecords() {
      return numberRecords;
   }

   public int getTotalBlocksInFile() {
      return totalBlocksInFile;
   }

   /**
    * read (little endian)
    */
   public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
      try {
         indexRecordLength = littleEndianDataInputStream.readUnsignedShort();
         headerBlockSize = littleEndianDataInputStream.readUnsignedShort();
         final int fileType = littleEndianDataInputStream.readUnsignedByte();
         if (1 != fileType) {
            throw new Exception("PX file type was expected to be 1, but '" + fileType + "' was found");
         }
         dataBlockSizeCode = littleEndianDataInputStream.readUnsignedByte();
         if (1 == dataBlockSizeCode) {
            blockSize = BlockSize.oneK;
         } else if (2 == dataBlockSizeCode) {
            blockSize = BlockSize.twoK;
         } else if (3 == dataBlockSizeCode) {
            blockSize = BlockSize.threeK;
         } else if (4 == dataBlockSizeCode) {
            blockSize = BlockSize.fourK;
         } else {
            throw new Exception("Unknown block size code '" + dataBlockSizeCode + "'");
         }
         numberRecords = littleEndianDataInputStream.readInt();
         blocksInUse = littleEndianDataInputStream.readUnsignedShort();
         totalBlocksInFile = littleEndianDataInputStream.readUnsignedShort();
         firstDataBlock = littleEndianDataInputStream.readUnsignedShort();
         lastDataBlock = littleEndianDataInputStream.readUnsignedShort();
         indexRootBlock = littleEndianDataInputStream.readUnsignedShort();
         numberIndexLevels = littleEndianDataInputStream.readUnsignedShort();
         numberIndexFields = littleEndianDataInputStream.readUnsignedShort();
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in read", e);
      }
   }

   public int getIndexRootBlock() {
      return indexRootBlock;
   }

   public void setIndexRootBlock(int indexRootBlock) {
      this.indexRootBlock = indexRootBlock;
   }

   public int getNumberIndexLevels() {
      return numberIndexLevels;
   }

   public void setNumberIndexLevels(int numberIndexLevels) {
      this.numberIndexLevels = numberIndexLevels;
   }

   public int getNumberIndexFields() {
      return numberIndexFields;
   }

   public void setNumberIndexFields(int numberIndexFields) {
      this.numberIndexFields = numberIndexFields;
   }

   public void setBlocksInUse(int blocksInUse) {
      this.blocksInUse = blocksInUse;
   }

   public void setBlockSize(BlockSize blockSize) {
      this.blockSize = blockSize;
   }

   public void setDataBlockSizeCode(int dataBlockSizeCode) {
      this.dataBlockSizeCode = dataBlockSizeCode;
   }

   public void setFirstDataBlock(int firstDataBlock) {
      this.firstDataBlock = firstDataBlock;
   }

   public void setHeaderBlockSize(int headerBlockSize) {
      this.headerBlockSize = headerBlockSize;
   }

   public void setIndexRecordLength(int indexRecordLength) {
      this.indexRecordLength = indexRecordLength;
   }

   public void setLastDataBlock(int lastDataBlock) {
      this.lastDataBlock = lastDataBlock;
   }

   public void setNumberRecords(long numberRecords) {
      this.numberRecords = numberRecords;
   }

   public void setTotalBlocksInFile(int totalBlocksInFile) {
      this.totalBlocksInFile = totalBlocksInFile;
   }
}
