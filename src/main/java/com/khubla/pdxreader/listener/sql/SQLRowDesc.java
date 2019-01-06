package com.khubla.pdxreader.listener.sql;

import com.khubla.pdxreader.api.PDXReaderException;
import com.khubla.pdxreader.db.DBTableField;
import com.khubla.pdxreader.db.DBTableField.FieldType;

/**
 * SQL Row
 *
 * @author tom
 */
public class SQLRowDesc {
   /**
    * generate SQLRowDesc from DBTableField
    *
    * @throws PDXReaderException
    */
   public static SQLRowDesc generateSQLRowDesc(DBTableField pdxTableField) throws PDXReaderException {
      return new SQLRowDesc(SQLSanitize.sanitize(pdxTableField.getName()), mapTypes(pdxTableField.getFieldType()));
   }

   /**
    * map Paradox types to SQL types
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