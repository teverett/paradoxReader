package com.khubla.pdxreader.db;

import java.util.ArrayList;
import java.util.List;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.api.PDXReaderException;
import com.khubla.pdxreader.util.StringUtil;

/**
 * @author tom
 */
public class DBTableHeader {
   /**
    * block size
    */
   public static enum BlockSize {
      oneK(1), twoK(2), threeK(3), fourK(4), eightK(8), sixteenK(16), thirtytwoK(32);
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
    * table type
    */
   public static enum TableType {
      keyed(0), unkeyed(2);
      private int value;

      private TableType(int value) {
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
    * table type
    */
   private TableType tableType;
   /**
    * size of single record
    */
   private int recordBufferSize;
   /**
    * length of the header block (bytes)
    */
   private int headerBlockSize;
   /**
    * total records
    */
   private long numberRecords;
   /**
    * number fields
    */
   private int numberFields;
   /**
    * number key fields
    */
   private int numberKeyFields;
   /**
    * data block size code. 1 - 1k, 2 - 2k, 3 - 3k, 4 - 4k, etc
    */
   private int dataBlockSizeCode;
   private int blocksInUse;
   private int totalBlocksInFile;
   private int firstDataBlock;
   private int lastDataBlock;
   private int firstFreeBlock;
   /**
    * filename. There is a filename embedded in the files. I'm not sure what it does, but I do read it.
    */
   private String embeddedFilename;
   /**
    * fields
    */
   private List<DBTableField> fields;
   /**
    * encryption
    */
   private byte[] encryption = new byte[8];

   public byte[] getEncryption() {
      return encryption;
   }

   public void setEncryption(byte[] encryption) {
      this.encryption = encryption;
   }

   private byte sortOrder;

   public byte getSortOrder() {
      return sortOrder;
   }

   public void setSortOrder(byte sortOrder) {
      this.sortOrder = sortOrder;
   }

   /**
    * figure out the total records in a block
    */
   public int calculateRecordsPerBlock() {
      return (blockSize.value * 1024) / recordBufferSize;
   }

   public int getBlocksInUse() {
      return blocksInUse;
   }

   public BlockSize getBlockSize() {
      return blockSize;
   }

   public int getDataBlockSizeCode() {
      return dataBlockSizeCode;
   }

   public String getEmbeddedFilename() {
      return embeddedFilename;
   }

   public List<DBTableField> getFields() {
      return fields;
   }

   public int getFirstDataBlock() {
      return firstDataBlock;
   }

   public int getFirstFreeBlock() {
      return firstFreeBlock;
   }

   public int getHeaderBlockSize() {
      return headerBlockSize;
   }

   public int getLastDataBlock() {
      return lastDataBlock;
   }

   public int getNumberFields() {
      return numberFields;
   }

   public int getNumberKeyFields() {
      return numberKeyFields;
   }

   public long getNumberRecords() {
      return numberRecords;
   }

   public int getRecordBufferSize() {
      return recordBufferSize;
   }

   public TableType getTableType() {
      return tableType;
   }

   public int getTotalBlocksInFile() {
      return totalBlocksInFile;
   }

   /**
    * read (little endian)
    */
   public void read(LittleEndianDataInputStream littleEndianDataInputStream) throws Exception {
      try {
         /*
          * record size
          */
         recordBufferSize = littleEndianDataInputStream.readUnsignedShort();
         /*
          * size of this header block
          */
         headerBlockSize = littleEndianDataInputStream.readUnsignedShort();
         /*
          * type of file
          */
         final int tableType = littleEndianDataInputStream.readUnsignedByte();
         if (0 == tableType) {
            this.tableType = TableType.keyed;
         } else if (2 == tableType) {
            this.tableType = TableType.unkeyed;
         } else {
            throw new Exception("Unknown table type '" + tableType + "'");
         }
         /*
          * size of a data block
          */
         dataBlockSizeCode = littleEndianDataInputStream.readUnsignedByte();
         if (1 == dataBlockSizeCode) {
            blockSize = BlockSize.oneK;
         } else if (2 == dataBlockSizeCode) {
            blockSize = BlockSize.twoK;
         } else if (3 == dataBlockSizeCode) {
            blockSize = BlockSize.threeK;
         } else if (4 == dataBlockSizeCode) {
            blockSize = BlockSize.fourK;
         } else if (8 == dataBlockSizeCode) {
            blockSize = BlockSize.eightK;
         } else if (16 == dataBlockSizeCode) {
            blockSize = BlockSize.sixteenK;
         } else if (32 == dataBlockSizeCode) {
            blockSize = BlockSize.thirtytwoK;
         } else {
            throw new Exception("Unknown block size code '" + dataBlockSizeCode + "'");
         }
         /*
          * total records in file
          */
         numberRecords = littleEndianDataInputStream.readInt();
         /*
          * number of blocks in use
          */
         blocksInUse = littleEndianDataInputStream.readUnsignedShort();
         /*
          * total blocks in file
          */
         totalBlocksInFile = littleEndianDataInputStream.readUnsignedShort();
         firstDataBlock = littleEndianDataInputStream.readUnsignedShort();
         lastDataBlock = littleEndianDataInputStream.readUnsignedShort();
         littleEndianDataInputStream.skipBytes(15);
         // byte 0x21
         numberFields = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x22
         littleEndianDataInputStream.skipBytes(1);
         // byte 0x23
         numberKeyFields = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x24
         littleEndianDataInputStream.read(encryption);
         // byte 0x2c
         sortOrder = littleEndianDataInputStream.readByte();
         // byte 0x2d
         // skip 6 bytes
         littleEndianDataInputStream.skipBytes(0x06);
         // byte 0x33
         littleEndianDataInputStream.skipBytes(0x45);
         // byte 0x78
         readFieldTypesAndSizes(littleEndianDataInputStream);
         // name
         littleEndianDataInputStream.skipBytes(20);
         // int ptrptr = littleEndianDataInputStream.readhort();
         embeddedFilename = StringUtil.readString(littleEndianDataInputStream);
         /*
          * skip forward 250 bytes
          */
         final int skipBytes = 250;
         // dumpBytes(skipBytes, littleEndianDataInputStream);
         littleEndianDataInputStream.skipBytes(skipBytes);
         readFieldNames(littleEndianDataInputStream);
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }

   /**
    * read the field descriptions
    */
   private void readFieldNames(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
      try {
         for (final DBTableField pdxTableField : fields) {
            pdxTableField.readFieldName(littleEndianDataInputStream);
         }
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in readFieldNames", e);
      }
   }

   /**
    * read the field descriptions
    */
   private void readFieldTypesAndSizes(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
      try {
         fields = new ArrayList<DBTableField>();
         for (int i = 0; i < numberFields; i++) {
            final DBTableField pdxTableField = new DBTableField();
            if (pdxTableField.readFieldTypeAndSize(littleEndianDataInputStream)) {
               fields.add(pdxTableField);
            }
         }
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in readFieldTypesAndSizes", e);
      }
   }

   public void report() {
      for (final DBTableField pdxTableField : fields) {
         System.out.println("Field '" + pdxTableField.getName() + "' type '" + pdxTableField.getFieldType().toString() + "'");
      }
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

   public void setEmbeddedFilename(String embeddedFilename) {
      this.embeddedFilename = embeddedFilename;
   }

   public void setFields(List<DBTableField> fields) {
      this.fields = fields;
   }

   public void setFirstDataBlock(int firstDataBlock) {
      this.firstDataBlock = firstDataBlock;
   }

   public void setFirstFreeBlock(int firstFreeBlock) {
      this.firstFreeBlock = firstFreeBlock;
   }

   public void setHeaderBlockSize(int headerBlockSize) {
      this.headerBlockSize = headerBlockSize;
   }

   public void setLastDataBlock(int lastDataBlock) {
      this.lastDataBlock = lastDataBlock;
   }

   public void setNumberFields(int numberFields) {
      this.numberFields = numberFields;
   }

   public void setNumberKeyFields(int numberKeyFields) {
      this.numberKeyFields = numberKeyFields;
   }

   public void setNumberRecords(long numberRecords) {
      this.numberRecords = numberRecords;
   }

   public void setRecordBufferSize(int recordBufferSize) {
      this.recordBufferSize = recordBufferSize;
   }

   public void setTableType(TableType tableType) {
      this.tableType = tableType;
   }

   public void setTotalBlocksInFile(int totalBlocksInFile) {
      this.totalBlocksInFile = totalBlocksInFile;
   }
}
