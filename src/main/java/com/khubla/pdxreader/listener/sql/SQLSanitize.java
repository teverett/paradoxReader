package com.khubla.pdxreader.listener.sql;

public class SQLSanitize {
   /**
    * illegal chars
    */
   public static final String ILLEGAL_CHARS = "\\\"/ -#?!*";

   /**
    * replace illegal chars with underscores
    */
   public static String sanitize(String str) {
      String ret = "";
      for (int i = 0; i < str.length(); i++) {
         char c = str.charAt(i);
         for (int j = 0; j < ILLEGAL_CHARS.length(); j++) {
            if (str.charAt(i) == ILLEGAL_CHARS.charAt(j)) {
               c = '_';
               break;
            }
         }
         ret += c;
      }
      return ret;
   }
}
