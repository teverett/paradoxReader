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
   private int unk1;
   private int unk2;
   private int unk3;
   private int unk4;
   /**
    * unknown bytes 0x12, 0x13
    */
   private int unk5;
   private int unk6;
   /**
    * byte 0x2b
    */
   private int unk7;
   /**
    * byte 0x2f
    */
   private int unk8;
   /**
    * byte 0x3c
    */
   private int unk9;
   /**
    * byte 0x3e
    */
   private int unk10;
   /**
    * byte 0x48
    */
   private int unk11;
   /**
    * byte 0x4d
    */
   private int unk12;
   /**
    * byte 0x50
    */
   private int unk13;

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

   public int getUnk1() {
      return unk1;
   }

   public int getUnk10() {
      return unk10;
   }

   public int getUnk11() {
      return unk11;
   }

   public int getUnk12() {
      return unk12;
   }

   public int getUnk13() {
      return unk13;
   }

   public int getUnk2() {
      return unk2;
   }

   public int getUnk3() {
      return unk3;
   }

   public int getUnk4() {
      return unk4;
   }

   public int getUnk5() {
      return unk5;
   }

   public int getUnk6() {
      return unk6;
   }

   public int getUnk7() {
      return unk7;
   }

   public int getUnk8() {
      return unk8;
   }

   public int getUnk9() {
      return unk9;
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
            this.tableType = TableType.indexedDB;
         } else if (1 == tableType) {
            this.tableType = TableType.indexPX;
         } else if (2 == tableType) {
            this.tableType = TableType.nonindexedDB;
         } else if (3 == tableType) {
            this.tableType = TableType.noincrementingsecondaryindexXnn;
         } else if (4 == tableType) {
            this.tableType = TableType.secondaryindexYnn;
         } else if (5 == tableType) {
            this.tableType = TableType.incrementingsecondaryindexXnn;
         } else if (6 == tableType) {
            this.tableType = TableType.nonincrementingsecondaryindexXGn;
         } else if (7 == tableType) {
            this.tableType = TableType.secondaryindexYGn;
         } else if (8 == tableType) {
            this.tableType = TableType.incrementingsecondaryindexXGn;
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
         unk5 = littleEndianDataInputStream.readUnsignedShort();
         /*
          * modified
          */
         modified1 = littleEndianDataInputStream.readUnsignedByte();
         indexFieldNumber = littleEndianDataInputStream.readUnsignedByte();
         primaryIndexWorkspace = littleEndianDataInputStream.readInt();
         /*
          * unknown pointer
          */
         unk6 = readPointer(littleEndianDataInputStream);
         indexRoot = littleEndianDataInputStream.readUnsignedShort();
         numIndexLevels = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x21
         numberFields = littleEndianDataInputStream.readUnsignedShort();
         // byte 0x23
         numberKeyFields = littleEndianDataInputStream.readUnsignedShort();
         // byte 0x24
         littleEndianDataInputStream.read(encryption);
         // byte 0x29
         sortOrder = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x2a
         modified2 = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x2b
         unk7 = littleEndianDataInputStream.readUnsignedShort();
         // byte 0x2d
         change1 = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x2e
         change2 = littleEndianDataInputStream.readUnsignedByte();
         /*
          * unknown
          */
         // byte 0x2f
         unk8 = littleEndianDataInputStream.readUnsignedByte();
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
         unk9 = littleEndianDataInputStream.readUnsignedByte();
         // byte 3d
         auxPasswords = littleEndianDataInputStream.readByte();
         // byte 3e
         unk10 = littleEndianDataInputStream.readUnsignedShort();
         // byte 0x40
         cryptInfoStartPtr = readPointer(littleEndianDataInputStream);
         // byte 0x44
         cryptInfoEndPtr = readPointer(littleEndianDataInputStream);
         // byte 0x48
         unk11 = littleEndianDataInputStream.readUnsignedByte();
         // byte 0x49
         autoInc = littleEndianDataInputStream.readInt();
         // byte 0x4d
         unk12 = littleEndianDataInputStream.readUnsignedShort();
         // byte 0x4f
         indexUpdateRequired = byteToBool(littleEndianDataInputStream.readByte());
         // byte 0x50
         unk13 = readPointer(littleEndianDataInputStream);
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
         // byte 0x78 (for v40).
         readFieldTypesAndSizes(littleEndianDataInputStream);
         // there are two bytes per field. so we are now at 0x78 + (numFields*2).
         /*
          * well i dunno what's going on. but the data b/t here and the filename is 4 bytes per field, plus an extra 4 bytes
          */
         unk1 = littleEndianDataInputStream.readUnsignedByte();
         unk2 = littleEndianDataInputStream.readUnsignedByte();
         unk3 = littleEndianDataInputStream.readUnsignedByte();
         unk4 = littleEndianDataInputStream.readUnsignedByte();
         // 4 bytes per field
         readUnknownFieldBytes(littleEndianDataInputStream);
         /*
          * read the name it has different sizes for different versions and is null padded
          */
         if (fileVersionID == 0x0c) {
            final byte[] fn = new byte[261];
            littleEndianDataInputStream.read(fn);
            embeddedFilename = StringUtil.readString(fn).trim();
         } else {
            final byte[] fn = new byte[79];
            littleEndianDataInputStream.read(fn);
            embeddedFilename = StringUtil.readString(fn).trim();
         }
         /*
          * now read the field names
          */
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

   /**
    * read the unknown field bytes. 4 bytes per field. no idea what they mean.
    */
   private void readUnknownFieldBytes(LittleEndianDataInputStream littleEndianDataInputStream) throws PDXReaderException {
      try {
         for (final DBTableField pdxTableField : fields) {
            final byte[] unknownFieldBytes = new byte[4];
            littleEndianDataInputStream.read(unknownFieldBytes);
            pdxTableField.setUnknownFieldBytes(unknownFieldBytes);
         }
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in readUnknownFieldBytes", e);
      }
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

   public void setUnk1(int unk1) {
      this.unk1 = unk1;
   }

   public void setUnk10(int unk10) {
      this.unk10 = unk10;
   }

   public void setUnk11(int unk11) {
      this.unk11 = unk11;
   }

   public void setUnk12(int unk12) {
      this.unk12 = unk12;
   }

   public void setUnk13(int unk13) {
      this.unk13 = unk13;
   }

   public void setUnk2(int unk2) {
      this.unk2 = unk2;
   }

   public void setUnk3(int unk3) {
      this.unk3 = unk3;
   }

   public void setUnk4(int unk4) {
      this.unk4 = unk4;
   }

   public void setUnk5(int unk5) {
      this.unk5 = unk5;
   }

   public void setUnk6(int unk6) {
      this.unk6 = unk6;
   }

   public void setUnk7(int unk7) {
      this.unk7 = unk7;
   }

   public void setUnk8(int unk8) {
      this.unk8 = unk8;
   }

   public void setUnk9(int unk9) {
      this.unk9 = unk9;
   }

   public void setWriteProtected(boolean writeProtected) {
      this.writeProtected = writeProtected;
   }
}
