package test.com.khubla.pdxreader;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.px.PXFile;

@Test(enabled = false)
public class TestSinglePXFile {
   public void testRead() {
      try {
         File inputFile = new File("src/test/resources/examples/MTDEMO/VIDORDER.PX");
         final PXFile pxFile = new PXFile();
         pxFile.read(inputFile);
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
