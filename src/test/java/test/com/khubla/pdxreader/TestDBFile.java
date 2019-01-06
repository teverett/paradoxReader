package test.com.khubla.pdxreader;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.api.PDXTableListener;
import com.khubla.pdxreader.db.DBTableFile;
import com.khubla.pdxreader.listener.PDXTableReaderCSVListenerImpl;
import com.khubla.pdxreader.listener.sql.PDXTableReaderSQLListenerImpl;
import com.khubla.pdxreader.util.TestUtil;

@Test
public class TestDBFile {
   public void testReadToCSV() {
      try {
         final List<String> files = TestUtil.getTestFiles("src/test/resources/examples/", new String[] { ".DB" });
         for (final String filename : files) {
            final File inputFile = new File(filename);
            Assert.assertTrue(inputFile.exists());
            System.out.println(filename);
            final DBTableFile pdxFile = new DBTableFile();
            final PDXTableListener pdxReaderListener = new PDXTableReaderCSVListenerImpl();
            pdxFile.read(inputFile, pdxReaderListener);
         }
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   public void testReadToSQL() {
      try {
         final List<String> files = TestUtil.getTestFiles("src/test/resources/examples/", new String[] { ".DB" });
         for (final String filename : files) {
            final File inputFile = new File(filename);
            Assert.assertTrue(inputFile.exists());
            System.out.println(filename);
            final DBTableFile pdxFile = new DBTableFile();
            final PDXTableListener pdxReaderListener = new PDXTableReaderSQLListenerImpl();
            pdxFile.read(inputFile, pdxReaderListener);
         }
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
