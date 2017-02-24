package test.com.khubla.pdxreader;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.PDXReaderCSVListenerImpl;
import com.khubla.pdxreader.api.PDXReaderListener;
import com.khubla.pdxreader.px.PXFile;
import com.khubla.pdxreader.util.TestUtil;

@Test
public class TestPXFile {
   public void testRead() {
      try {
         final List<String> files = TestUtil.getTestFiles("src/test/resources/", new String[] { ".PX" });
         for (final String filename : files) {
            final File inputFile = new File(filename);
            Assert.assertTrue(inputFile.exists());
            System.out.println(filename);
            final PXFile pxFile = new PXFile();
            final PDXReaderListener pdxReaderListener = new PDXReaderCSVListenerImpl();
            pxFile.read(inputFile, pdxReaderListener);
         }
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
