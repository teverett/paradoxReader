package com.khubla.pdxreader.listener.sql;

import java.util.ArrayList;
import java.util.List;

import com.khubla.pdxreader.db.DBTableField;
import com.khubla.pdxreader.db.DBTableField.FieldType;
import com.khubla.pdxreader.db.DBTableHeader;
import com.khubla.pdxreader.db.DBTableValue;

/**
 * SQL Table
 *
 * @author tom
 */
public class SQLTableDesc {
   /**
    * illegal chars
    */
   public static final String ILLEGAL_CHARS = "\\\"/ -";

   /**
    * generate SQLTableDesc from DBTableHeader
    */
   public static SQLTableDesc generateSQLTableDesc(String filename, DBTableHeader pdxTableHeader) {
      final SQLTableDesc ret = new SQLTableDesc(sanitize(generateTableName(filename)));
      /*
       * build the table fields
       */
      for (final DBTableField pdxTableField : pdxTableHeader.getFields()) {
         final SQLRowDesc sqlRowDesc = new SQLRowDesc(sanitize(pdxTableField.getName()), mapTypes(pdxTableField.getFieldType()));
         ret.add(sqlRowDesc);
      }
      return ret;
   }

   /**
    * create table name from file name
    */
   private static String generateTableName(String filename) {
      return sanitize(filename.substring(0, filename.indexOf('.')));
   }

   /**
    * TODO
    *
    * @param fieldType
    * @return
    */
   private static String mapTypes(FieldType fieldType) {
      return "varchar(255)";
   }

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

   /**
    * create template
    */
   private final String CREATE_TEMPLATE = "CREATE TABLE %s (%s);";
   /**
    * insert template
    */
   private final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s);";
   /**
    * SQL table name
    */
   private final String sqlTableName;
   /**
    * the rows
    */
   private final List<SQLRowDesc> rows = new ArrayList<SQLRowDesc>();

   public SQLTableDesc(String tableName) {
      super();
      this.sqlTableName = tableName;
   }

   public void add(SQLRowDesc sqlRowDesc) {
      rows.add(sqlRowDesc);
   }

   /**
    * generate column name list
    */
   public String generateColumnNameList() {
      String columnList = "";
      for (final SQLRowDesc sqlRowDesc : rows) {
         if (columnList.length() != 0) {
            columnList += ",";
         }
         columnList += sqlRowDesc.getName();
      }
      return columnList;
   }

   public List<SQLRowDesc> getRows() {
      return rows;
   }

   public String getTableName() {
      return sqlTableName;
   }

   /**
    * generate the SQL CREATE
    */
   public String renderSQLCreate() {
      String columnList = "";
      for (final SQLRowDesc sqlRowDesc : rows) {
         if (columnList.length() != 0) {
            columnList += ",";
         }
         columnList += sqlRowDesc.getName() + " " + sqlRowDesc.getType();
      }
      return String.format(CREATE_TEMPLATE, sqlTableName, columnList);
   }

   /**
    * generate the SQL INSERT
    */
   public String renderSQLInsert(List<DBTableValue> values) {
      /*
       * values
       */
      String fields = "";
      for (final DBTableValue pdxTableValue : values) {
         if (fields.length() != 0) {
            fields += ",";
         }
         fields += "\"" + pdxTableValue.getValue() + "\"";
      }
      /*
       * insert
       */
      return String.format(INSERT_TEMPLATE, sqlTableName, generateColumnNameList(), fields);
   }
}
