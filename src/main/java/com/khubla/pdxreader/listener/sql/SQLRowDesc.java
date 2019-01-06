package com.khubla.pdxreader.listener.sql;

/**
 * SQL Row
 *
 * @author tom
 */
public class SQLRowDesc {
   private final String name;
   private final String type;

   public SQLRowDesc(String name, String type) {
      super();
      this.name = name;
      this.type = type;
   }

   public String getName() {
      return name;
   }

   public String getType() {
      return type;
   }
}