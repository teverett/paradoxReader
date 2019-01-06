package test.com.khubla.pdxreader;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.Database;
import com.khubla.pdxreader.api.PDXTableListener;
import com.khubla.pdxreader.listener.PDXTableReaderCSVListenerImpl;

@Test
public class TestDatabase {
   public void testRead() {
      try {
         Database database = new Database("src/test/resources/examples/AREA-PDX");
         Assert.assertTrue(database.getMbfiles().size() == 0);
         Assert.assertTrue(database.getDbfiles().size() == 1);
         Assert.assertTrue(database.getPxfiles().size() == 1);
         Assert.assertTrue(database.getValfiles().size() == 1);
         final PDXTableListener pdxReaderListener = new PDXTableReaderCSVListenerImpl();
         database.readTables(pdxReaderListener);
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
