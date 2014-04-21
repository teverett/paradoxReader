package test.com.khubla.pdxreader;

import java.io.InputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.PDXReaderCSVListenerImpl;
import com.khubla.pdxreader.api.PDXReaderListener;
import com.khubla.pdxreader.db.DBTableFile;

@Test
public class TestDBFile {
   private void testRead(String filename) {
      try {
         final InputStream inputStream = TestDBFile.class.getResourceAsStream(filename);
         Assert.assertNotNull(inputStream);
         final DBTableFile pdxFile = new DBTableFile();
         final PDXReaderListener pdxReaderListener = new PDXReaderCSVListenerImpl();
         pdxFile.read(inputStream, pdxReaderListener);
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   public void testReadCONTACTS() {
      try {
         testRead("/CONTACTS.DB");
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   @Test(enabled = false)
   public void testReadCUSTOMER() {
      try {
         testRead("/CUSTOMER.DB");
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   @Test(enabled = false)
   public void testReadHURCULES() {
      try {
         testRead("/HERCULES.DB");
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   public void testReadORDERS() {
      try {
         testRead("/ORDERS.DB");
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   public void testReadSERVER() {
      try {
         testRead("/SERVER.DB");
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
