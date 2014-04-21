package test.com.khubla.pdxreader;

import java.io.InputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.mb.MBTableFile;

/**
 * @author tom
 */
@Test
public class TestMBFile {
   private void testRead(String filename) {
      try {
         final InputStream inputStream = TestDBFile.class.getResourceAsStream(filename);
         Assert.assertNotNull(inputStream);
         final MBTableFile mbTableFile = new MBTableFile();
         mbTableFile.read(inputStream);
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   @Test(enabled = false)
   public void testReadCUSTOMER() {
      try {
         testRead("/CUSTOMER.MB");
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   @Test(enabled = false)
   public void testReadHURCULES() {
      try {
         testRead("/HERCULES.MB");
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
