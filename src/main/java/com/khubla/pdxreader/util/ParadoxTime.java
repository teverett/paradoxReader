package com.khubla.pdxreader.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ParadoxTime {
   static public String getTimeFromParadoxTime(byte[] data) {
      /*
       * milliseconds since Jan 1, 1 AD, convert to UTC time
       */
      data[0] = (byte) (data[0] & 0x7f); // handle unsigned number
      final double dt = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getDouble();
      long dateTime = (long) dt;
      if (dateTime == 0) {
         return null;
      } else {
         dateTime -= 86400000; // millis in 1 day
         dateTime -= 62135607600000l; // millis from 01.01.1970
         return Long.toString(dateTime);
      }
   }
}
