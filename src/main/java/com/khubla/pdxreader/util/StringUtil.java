package com.khubla.pdxreader.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.io.LittleEndianDataInputStream;

/**
 * @author tom
 */
public class StringUtil {
   private final static String DEFAULT_ENCODING = "Cp1252";

   public static String byteArrayToString(byte[] bytes) {
      final StringBuilder builder = new StringBuilder();
      for (final byte b : bytes) {
         builder.append(String.format("%02x", b));
      }
      return builder.toString();
   }

   public static String bytesToString(byte[] bytes, String encoding) {
      final Charset charset = Charset.forName(encoding);
      return new String(bytes, charset);
   }

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

   public static String readString(LittleEndianDataInputStream littleEndianDataInputStream) throws IOException {
      return readString(littleEndianDataInputStream, DEFAULT_ENCODING);
   }

   /**
    * read a null terminated string from a LittleEndianDataInputStream
    */
   public static String readString(LittleEndianDataInputStream littleEndianDataInputStream, String encoding) throws IOException {
      final List<Byte> chars = new ArrayList<Byte>();
      Byte c = littleEndianDataInputStream.readByte();
      while (c != 0) {
         chars.add(c);
         c = littleEndianDataInputStream.readByte();
      }
      final Byte[] buffer = new Byte[chars.size()];
      chars.toArray(buffer);
      return bytesToString(ArrayUtils.toPrimitive(buffer), encoding);
   }
}
