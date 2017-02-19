package com.khubla.pdxreader.db;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
    * <p>
    * $ and N are double precision floating point numbers
    * </p>
    * <p>
    * D are signed long ints (they are dates). Number of days since January 1, 1 AD.
    * </p>
    * <p>
    * S are signed shorts
    * </p>
    * <p>
    * M, B and U are BLOBS.
    * </p>
    * <p>
    * A are null terminated, fixed length strings
    * </p>
    */
   public void read(DBTableField pdxTableField, InputStream inputStream) throws Exception {
      try {
         /*
          * get the data
          */
         final byte[] data = new byte[pdxTableField.getLength()];
         inputStream.read(data);
         /*
          * convert to type
          */
         switch (pdxTableField.getFieldType()) {
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
            case $:
               final double dollars = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getDouble();
               value = Double.toString(dollars);
               break;
            case M:
               value = StringUtil.ByteArrayToString(data);
            case N:
               final long n = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getLong();
               value = Double.toString(n);
               break;
            case B:
               value = StringUtil.ByteArrayToString(data);
               break;
            case TS:
               // milliseconds since Jan 1, 1 AD, convert to UTC time
               data[0] = (byte) (data[0] & 0x7f); // handle unsigned number
               double dt = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getDouble();
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
         }
      } catch (final Exception e) {
         throw new Exception("Exception in read", e);
      }
   }

   public void setType(DBTableField.FieldType type) {
      this.type = type;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
