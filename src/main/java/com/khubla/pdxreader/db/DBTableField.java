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
      /**
       * <pre>
       * A - Alpha, length 255
       * D - Date, length 4
       * S - Short Integer, length 2
       * I - Long Integer, length 4
       * C - Currency, length 8
       * N - Number, length 8
       * L - Logical, length 1
       * M - Memo, variable length
       * B - Binary, variable length
       * E - Formatting memo, variable length
       * O - OLE, variable length
       * G - Graphic Blob, variable length
       * T - Time, length 4
       * TS - TimeStamp, length 8
       * Auto - AutoIncrement
       * BCD - BCD, length 17
       * Bytes - Bytes, variable length
       * </pre>
       */
      A(1), D(2), S(3), I(4), C(5), N(6), L(9), M(0x0c), B(0x0d), E(0x0e), O(0x0f), G(0x10), T(0x14), TS(0x15), Auto(0x16), BCD(0x17), Bytes(0x18);
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
            case 0x01:
               fieldType = FieldType.A;
               break;
            case 0x02:
               fieldType = FieldType.D;
               if (length != 4) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 0x03:
               fieldType = FieldType.S;
               if (length != 2) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 0x04:
               fieldType = FieldType.I;
               if (length != 4) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 0x05:
               fieldType = FieldType.C;
               if (length != 8) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 0x06:
               fieldType = FieldType.N;
               if (length != 8) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 0x09:
               fieldType = FieldType.L;
               if (length != 1) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 0xc:
               fieldType = FieldType.M;
               break;
            case 0xd:
               fieldType = FieldType.B;
               break;
            case 0xe:
               fieldType = FieldType.E;
               break;
            case 0xf:
               fieldType = FieldType.O;
               break;
            case 0x10:
               fieldType = FieldType.G;
               break;
            case 0x14:
               fieldType = FieldType.T;
               if (length != 4) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 0x15:
               fieldType = FieldType.TS;
               if (length != 8) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 0x16:
               fieldType = FieldType.Auto;
               break;
            case 0x17:
               fieldType = FieldType.BCD;
               if (length != 17) {
                  throw new Exception("Invalid field length '" + length + "' for type '" + type + "'");
               }
               break;
            case 0x18:
               fieldType = FieldType.Bytes;
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
