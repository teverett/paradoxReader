package test.com.khubla.pdxreader;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.PDXReaderCSVListenerImpl;
import com.khubla.pdxreader.api.PDXReaderListener;
import com.khubla.pdxreader.db.DBTableFile;

@Test(enabled = false)
public class TestSingleDBFile {
   public void testRead() {
      try {
         // File inputFile = new File("src/test/resources/examples/PCLDATA/LPI.DB");
         File inputFile = new File("src/test/resources/MEMBRE.DB");
         final DBTableFile pdxFile = new DBTableFile();
         final PDXReaderListener pdxReaderListener = new PDXReaderCSVListenerImpl();
         pdxFile.read(inputFile, pdxReaderListener);
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
