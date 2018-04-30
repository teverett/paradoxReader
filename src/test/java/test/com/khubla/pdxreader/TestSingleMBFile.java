package test.com.khubla.pdxreader;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.mb.MBTableFile;

@Test(enabled = true)
public class TestSingleMBFile {
   public void testRead() {
      try {
         File inputFile = new File("src/test/resources/examples/CUSTOMER.MB");
         final MBTableFile mbTableFile = new MBTableFile();
         mbTableFile.read(inputFile);
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
