package com.khubla.pdxreader.util;

import java.io.IOException;

import com.google.common.io.LittleEndianDataInputStream;

/**
 * @author tom
 */
public class StringUtil {
   /**
    * read a fixed length string from a byte buffer
    */
   public static String readString(byte[] data) throws IOException {
      final StringBuilder stringBuilder = new StringBuilder();
      int i = 0;
      while ((data[i] != 0) && (i < (data.length - 1))) {
         stringBuilder.append((char) data[i++]);
      }
      return stringBuilder.toString().trim();
   }

   /**
    * read a null terminated string from a LittleEndianDataInputStream
    */
   public static String readString(LittleEndianDataInputStream littleEndianDataInputStream) throws IOException {
      final StringBuilder stringBuilder = new StringBuilder();
      int c = littleEndianDataInputStream.readUnsignedByte();
      while (c != 0) {
         stringBuilder.append((char) c);
         c = littleEndianDataInputStream.readUnsignedByte();
      }
      return stringBuilder.toString();
   }

   public static String ByteArrayToString(byte[] bytes) {
      final StringBuilder builder = new StringBuilder();
      for (byte b : bytes) {
         builder.append(String.format("%02x", b));
      }
      return builder.toString();
   }
}
