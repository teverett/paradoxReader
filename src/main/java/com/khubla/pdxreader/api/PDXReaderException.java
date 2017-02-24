package com.khubla.pdxreader.api;

public class PDXReaderException extends Exception {
   private static final long serialVersionUID = 1L;

   public PDXReaderException(String message) {
      super(message);
   }

   public PDXReaderException(String message, Exception e) {
      super(message, e);
   }
}