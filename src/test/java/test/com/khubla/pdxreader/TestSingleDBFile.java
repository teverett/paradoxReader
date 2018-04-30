package test.com.khubla.pdxreader;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.api.PDXTableListener;
import com.khubla.pdxreader.db.DBTableFile;
import com.khubla.pdxreader.listener.PDXTableReaderCSVListenerImpl;

@Test(enabled = false)
public class TestSingleDBFile {
   public void testRead() {
      try {
         File inputFile = new File("src/test/resources/PCLDATA/LPI.DB");
         // File inputFile = new File("src/test/resources/MEMBRE.DB");
         final DBTableFile pdxFile = new DBTableFile();
         final PDXTableListener pdxReaderListener = new PDXTableReaderCSVListenerImpl();
         pdxFile.read(inputFile, pdxReaderListener);
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
