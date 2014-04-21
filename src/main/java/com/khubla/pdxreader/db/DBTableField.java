package com.khubla.pdxreader.db;

import com.google.common.io.LittleEndianDataInputStream;
import com.khubla.pdxreader.util.StringUtil;

/**
 * @author tom
 */
public class DBTableField {
   /**
    * field type
    */
   public static enum FieldType {
      A(1), D(2), S(3), $(5), N(6), M(0x0c), B(0x0d), Auto(22);
      private int value;

      private FieldType(int value) {
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
    * field type
    */
   private FieldType fieldType;
   /**
    * field type
    */
   private int type;
   /**
    * field length
    */
   private int length;
   /**
    * name
    */
   private String name;

   public FieldType getFieldType() {
      return fieldType;
   }

   public int getLength() {
      return length;
   }

   public String getName() {
      return name;
   }

   public int getType() {
      return type;
   }

   /**
    * names
    */
   public void readFieldName(LittleEndianDataInputStream littleEndianDataInputStream) throws Exception {
      try {
         name = StringUtil.readString(littleEndianDataInputStream);
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }

   /**
    * types and sizes, 2 bytes per field
    */
   public void readFieldTypeAndSize(LittleEndianDataInputStream littleEndianDataInputStream) throws Exception {
      try {
         type = littleEndianDataInputStream.readUnsignedByte();
         length = littleEndianDataInputStream.readUnsignedByte();
         switch (type) {
            case 01:
               fieldType = FieldType.A;
               break;
            case 02:
               fieldType = FieldType.D;
               if (length != 4) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 03:
               fieldType = FieldType.S;
               if (length != 2) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 05:
               fieldType = FieldType.$;
               if (length != 8) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 06:
               fieldType = FieldType.N;
               if (length != 8) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 0Xc:
               fieldType = FieldType.M;
               break;
            case 0Xd:
               fieldType = FieldType.B;
               break;
            case 22:
               fieldType = FieldType.Auto;
               break;
            default:
               throw new Exception("Unknown field type '" + type + "'");
         }
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }

   public void setFieldType(FieldType fieldType) {
      this.fieldType = fieldType;
   }

   public void setLength(int length) {
      this.length = length;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setType(int type) {
      this.type = type;
   }
}
