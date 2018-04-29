package com.khubla.pdxreader.db;

import java.io.IOException;
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

   private static boolean byteToBool(byte input) throws Exception {
      if ((input < 0) || (input > 1)) {
         throw new Exception("Illegal boolean value " + Byte.toString(input) + "''");
      }
      return input == 1;
   }

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
   private byte[] encryption = new byte[4];
   private int sortOrder;
   private int modified1;
   private int modified2;
   private int indexFieldNumber;
   private int primaryIndexWorkspace;
   private int indexRoot;
   private int numIndexLevels;
   private int change1;
   private int change2;
   private int tableNamePtrPtr;
   private int fldInfoPtr;
   private boolean writeProtected;
   private int fileVersionID;
   private int fileVersion;
   private int maxBlocks;
   private int auxPasswords;
   private int cryptInfoStartPtr;
   private int cryptInfoEndPtr;
   private int autoInc;
   private boolean indexUpdateRequired;
   private boolean refIntegrity;

   /**
    * figure out the total records in a block
    */
   public int calculateRecordsPerBlock() {
      return (blockSize.value * 1024) / recordBufferSize;
   }

   public int getAutoInc() {
      return autoInc;
   }

   public int getAuxPasswords() {
      return auxPasswords;
   }

   public int getBlocksInUse() {
      return blocksInUse;
   }

   public BlockSize getBlockSize() {
      return blockSize;
   }

   public int getChange1() {
      return change1;
   }

   public int getChange2() {
      return change2;
   }

   public int getCryptInfoEndPtr() {
      return cryptInfoEndPtr;
   }

   public int getCryptInfoStartPtr() {
      return cryptInfoStartPtr;
   }

   public int getDataBlockSizeCode() {
      return dataBlockSizeCode;
   }

   public String getEmbeddedFilename() {
      return embeddedFilename;
   }

   public byte[] getEncryption() {
      return encryption;
   }

   public List<DBTableField> getFields() {
      return fields;
   }

   public int getFileVersion() {
      return fileVersion;
   }

   private int getFileVersion(int i) throws Exception {
      if (i == 0x03) {
         return 30;
      } else if (i == 0x04) {
         return 35;
      } else if ((i >= 0x05) && (i <= 0x09)) {
         return 40;
      } else if ((i == 0x0a) || (i == 0x0b)) {
         return 50;
      } else if (i == 0x0c) {
         return 70;
      } else {
         throw new Exception("Unknown file version " + Integer.toHexString(i) + "");
      }
   }

   public int getFileVersionID() {
      return fileVersionID;
   }

   public int getFirstDataBlock() {
      return firstDataBlock;
   }

   public int getFirstFreeBlock() {
      return firstFreeBlock;
   }

   public int getFldInfoPtr() {
      return fldInfoPtr;
   }

   public int getHeaderBlockSize() {
      return headerBlockSize;
   }

   public int getIndexFieldNumber() {
      return indexFieldNumber;
   }

   public int getIndexRoot() {
      return indexRoot;
   }

   public boolean getIndexUpdateRequired() {
      return indexUpdateRequired;
   }

   public int getLastDataBlock() {
      return lastDataBlock;
   }

   public int getMaxBlocks() {
      return maxBlocks;
   }

   public int getModified1() {
      return modified1;
   }

   public int getModified2() {
      return modified2;
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

   public int getNumIndexLevels() {
      return numIndexLevels;
   }

   public int getPrimaryIndexWorkspace() {
      return primaryIndexWorkspace;
   }

   public int getRecordBufferSize() {
      return recordBufferSize;
   }

   public boolean getRefIntegrity() {
      return refIntegrity;
   }

   public int getSortOrder() {
      return sortOrder;
   }

   public int getTableNamePtrPtr() {
      return tableNamePtrPtr;
   }

   public TableType getTableType() {
      return tableType;
   }

   public int getTotalBlocksInFile() {
      return totalBlocksInFile;
   }

   public boolean getWriteProtected() {
      return writeProtected;
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
         /*
          * unknown bytes 0x12, 0x13
          */
         littleEndianDataInputStream.skipBytes(2);
         /*
          * modified
          */
         modified1 = littleEndianDataInputStream.readUnsignedByte();
         indexFieldNumber = littleEndianDataInputStream.readUnsignedByte();
         primaryIndexWorkspace = littleEndianDataInputStream.readInt();
         /*
          * unknown pointer
          */
         littleEndianDataInputStream.skipBytes(4);
         indexRoot = littleEndianDataInputStream.readUnsignedShort();
         numIndexLevels = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x21
         numberFields = littleEndianDataInputStream.readUnsignedShort();
         // byte 0x22
         // littleEndianDataInputStream.skipBytes(1);
         // byte 0x23
         numberKeyFields = littleEndianDataInputStream.readUnsignedShort();
         // byte 0x24
         littleEndianDataInputStream.read(encryption);
         // byte 0x29
         sortOrder = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x2a
         modified2 = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x2b
         littleEndianDataInputStream.skipBytes(2);
         // byte 0x2d
         change1 = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x2e
         change2 = littleEndianDataInputStream.readUnsignedByte();
         /*
          * unknown
          */
         // byte 0x2f
         littleEndianDataInputStream.skipBytes(1);
         // byte 0x30
         tableNamePtrPtr = readPointer(littleEndianDataInputStream);
         // byte 0x34
         fldInfoPtr = readPointer(littleEndianDataInputStream);
         // byte 0x38
         writeProtected = byteToBool(littleEndianDataInputStream.readByte());
         // byte 0x39
         fileVersionID = littleEndianDataInputStream.readByte();
         fileVersion = this.getFileVersion(fileVersionID);
         // byte 0x3a
         maxBlocks = littleEndianDataInputStream.readUnsignedShort();
         // byte 3c
         littleEndianDataInputStream.skipBytes(1);
         // byte 3d
         auxPasswords = littleEndianDataInputStream.readByte();
         // byte 3e
         littleEndianDataInputStream.skipBytes(2);
         // byte 0x40
         cryptInfoStartPtr = readPointer(littleEndianDataInputStream);
         // byte 0x44
         cryptInfoEndPtr = readPointer(littleEndianDataInputStream);
         // byte 0x48
         littleEndianDataInputStream.skipBytes(1);
         // byte 0x49
         autoInc = littleEndianDataInputStream.readInt();
         // byte 0x4d
         littleEndianDataInputStream.skipBytes(2);
         // byte 0x4f
         indexUpdateRequired = byteToBool(littleEndianDataInputStream.readByte());
         // byte 0x50
         littleEndianDataInputStream.skipBytes(4);
         // byte 0x55
         refIntegrity = byteToBool(littleEndianDataInputStream.readByte());
         // byte 0x56
         /*
          * if file version >= 40 then we need to be at index 0x78, otherwise at index 0x58
          */
         if (fileVersion >= 40) {
            littleEndianDataInputStream.skipBytes(0x23);
         } else {
            littleEndianDataInputStream.skipBytes(0x03);
         }
         // byte 0x78
         readFieldTypesAndSizes(littleEndianDataInputStream);
         // name
         littleEndianDataInputStream.skipBytes(20);
         embeddedFilename = StringUtil.readString(littleEndianDataInputStream);
         /*
          * skip forward 250 bytes
          */
         final int skipBytes = 250;
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

   private int readPointer(LittleEndianDataInputStream littleEndianDataInputStream) throws IOException {
      final byte[] ptr = new byte[4];
      littleEndianDataInputStream.read(ptr);
      return 0;
   }

   public void report() {
      for (final DBTableField pdxTableField : fields) {
         System.out.println("Field '" + pdxTableField.getName() + "' type '" + pdxTableField.getFieldType().toString() + "'");
      }
   }

   public void setAutoInc(int autoInc) {
      this.autoInc = autoInc;
   }

   public void setAuxPasswords(int auxPasswords) {
      this.auxPasswords = auxPasswords;
   }

   public void setBlocksInUse(int blocksInUse) {
      this.blocksInUse = blocksInUse;
   }

   public void setBlockSize(BlockSize blockSize) {
      this.blockSize = blockSize;
   }

   public void setChange1(int change1) {
      this.change1 = change1;
   }

   public void setChange2(int change2) {
      this.change2 = change2;
   }

   public void setCryptInfoEndPtr(int cryptInfoEndPtr) {
      this.cryptInfoEndPtr = cryptInfoEndPtr;
   }

   public void setCryptInfoStartPtr(int cryptInfoStartPtr) {
      this.cryptInfoStartPtr = cryptInfoStartPtr;
   }

   public void setDataBlockSizeCode(int dataBlockSizeCode) {
      this.dataBlockSizeCode = dataBlockSizeCode;
   }

   public void setEmbeddedFilename(String embeddedFilename) {
      this.embeddedFilename = embeddedFilename;
   }

   public void setEncryption(byte[] encryption) {
      this.encryption = encryption;
   }

   public void setFields(List<DBTableField> fields) {
      this.fields = fields;
   }

   public void setFileVersion(int fileVersion) {
      this.fileVersion = fileVersion;
   }

   public void setFileVersionID(int fileVersionID) {
      this.fileVersionID = fileVersionID;
   }

   public void setFirstDataBlock(int firstDataBlock) {
      this.firstDataBlock = firstDataBlock;
   }

   public void setFirstFreeBlock(int firstFreeBlock) {
      this.firstFreeBlock = firstFreeBlock;
   }

   public void setFldInfoPtr(int fldInfoPtr) {
      this.fldInfoPtr = fldInfoPtr;
   }

   public void setHeaderBlockSize(int headerBlockSize) {
      this.headerBlockSize = headerBlockSize;
   }

   public void setIndexFieldNumber(int indexFieldNumber) {
      this.indexFieldNumber = indexFieldNumber;
   }

   public void setIndexRoot(int indexRoot) {
      this.indexRoot = indexRoot;
   }

   public void setIndexUpdateRequired(boolean indexUpdateRequired) {
      this.indexUpdateRequired = indexUpdateRequired;
   }

   public void setLastDataBlock(int lastDataBlock) {
      this.lastDataBlock = lastDataBlock;
   }

   public void setMaxBlocks(int maxBlocks) {
      this.maxBlocks = maxBlocks;
   }

   public void setModified1(int modified1) {
      this.modified1 = modified1;
   }

   public void setModified2(int modified2) {
      this.modified2 = modified2;
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

   public void setNumIndexLevels(int numIndexLevels) {
      this.numIndexLevels = numIndexLevels;
   }

   public void setPrimaryIndexWorkspace(int primaryIndexWorkspace) {
      this.primaryIndexWorkspace = primaryIndexWorkspace;
   }

   public void setRecordBufferSize(int recordBufferSize) {
      this.recordBufferSize = recordBufferSize;
   }

   public void setRefIntegrity(boolean refIntegrity) {
      this.refIntegrity = refIntegrity;
   }

   public void setSortOrder(int sortOrder) {
      this.sortOrder = sortOrder;
   }

   public void setTableNamePtrPtr(int tableNamePtrPtr) {
      this.tableNamePtrPtr = tableNamePtrPtr;
   }

   public void setTableType(TableType tableType) {
      this.tableType = tableType;
   }

   public void setTotalBlocksInFile(int totalBlocksInFile) {
      this.totalBlocksInFile = totalBlocksInFile;
   }

   public void setWriteProtected(boolean writeProtected) {
      this.writeProtected = writeProtected;
   }
}
