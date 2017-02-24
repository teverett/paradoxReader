package com.khubla.pdxreader.db;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.khubla.pdxreader.api.PDXReaderException;
import com.khubla.pdxreader.db.DBTableField.FieldType;
import com.khubla.pdxreader.util.StringUtil;

/**
 * @author tom
 */
public class DBTableValue {
   /**
    * the value
    */
   private String value;
   /**
    * the type
    */
   private DBTableField.FieldType type;

   public DBTableField.FieldType getType() {
      return type;
   }

   public String getValue() {
      return value;
   }

   /**
    * Read a table field
    */
   public void read(DBTableField pdxTableField, InputStream inputStream) throws PDXReaderException {
      try {
         /*
          * get the data
          */
         final byte[] data = new byte[pdxTableField.getLength()];
         inputStream.read(data);
         /*
          * convert to type
          */
         final FieldType fieldType = pdxTableField.getFieldType();
         switch (fieldType) {
            case A:
               value = StringUtil.readString(data);
               break;
            case D:
               final long d = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort();
               value = Long.toString(d);
               break;
            case S:
               final long s = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort();
               value = Long.toString(s);
               break;
            case I:
               data[0] = (byte) (data[0] & 0x7f); // handle unsigned integers
               final long i = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getInt();
               value = Long.toString(i);
               break;
            case C:
               // currency
               final double dollars = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getDouble();
               value = Double.toString(dollars);
               break;
            case M:
               // Memo
               value = StringUtil.ByteArrayToString(data);
            case N:
               final long n = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getLong();
               value = Double.toString(n);
               break;
            case L:
               // Logical
               value = StringUtil.ByteArrayToString(data);
               break;
            case B:
               // Binary
               value = StringUtil.ByteArrayToString(data);
               break;
            case O:
               // OLE
               value = StringUtil.ByteArrayToString(data);
               break;
            case E:
               // formatted memo
               value = StringUtil.ByteArrayToString(data);
               break;
            case G:
               // Graphic
               value = StringUtil.ByteArrayToString(data);
               break;
            case BCD:
               // BCD
               value = StringUtil.ByteArrayToString(data);
               break;
            case Bytes:
               // Bytes
               value = StringUtil.ByteArrayToString(data);
               break;
            case TS:
               // milliseconds since Jan 1, 1 AD, convert to UTC time
               data[0] = (byte) (data[0] & 0x7f); // handle unsigned number
               final double dt = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getDouble();
               long dateTime = (long) dt;
               if (dateTime == 0) {
                  value = null;
               } else {
                  dateTime -= 86400000; // millis in 1 day
                  dateTime -= 62135607600000l; // millis from 01.01.1970
                  value = Long.toString(dateTime);
               }
               break;
            case Auto:
               final short auto = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort();
               value = Short.toString(auto);
               break;
            default:
               throw new Exception("Unknown field type '" + fieldType.name() + "'");
         }
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in read", e);
      }
   }

   public void setType(DBTableField.FieldType type) {
      this.type = type;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
