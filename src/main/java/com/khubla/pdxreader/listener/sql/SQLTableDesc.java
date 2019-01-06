package com.khubla.pdxreader.listener.sql;

import java.util.ArrayList;
import java.util.List;

import com.khubla.pdxreader.api.PDXReaderException;
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
    *
    * @throws PDXReaderException
    */
   public static SQLTableDesc generateSQLTableDesc(String filename, DBTableHeader pdxTableHeader) throws PDXReaderException {
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
    * map Paradocx types to SQL types
    *
    * @throws PDXReaderException
    */
   private static String mapTypes(FieldType fieldType) throws PDXReaderException {
      try {
         String ret = "";
         switch (fieldType) {
            case A:
               ret = "VARCHAR(255)";
               break;
            case D:
               ret = "DATETIME";
               break;
            case S:
               ret = "INTEGER";
               break;
            case I:
               ret = "INTEGER";
               break;
            case C:
               ret = "INTEGER";
               break;
            case N:
               ret = "DOUBLE";
               break;
            case L:
               ret = "INTEGER";
               break;
            case M:
               ret = "BLOB";
               break;
            case B:
               ret = "BLOB";
               break;
            case E:
               ret = "BLOB";
               break;
            case O:
               ret = "BLOB";
               break;
            case G:
               ret = "BLOB";
               break;
            case T:
               ret = "INTEGER";
               break;
            case TS:
               ret = "INTEGER";
               break;
            case Auto:
               ret = "INTEGER";
               break;
            case BCD:
               ret = "INTEGER";
               break;
            case Bytes:
               ret = "BLOB";
               break;
            default:
               throw new PDXReaderException("Unable to map type '" + fieldType + "'");
         }
         return ret;
      } catch (final Exception e) {
         throw new PDXReaderException("Exception in mapTypes", e);
      }
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
      sqlTableName = tableName;
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
