package test.com.khubla.pdxreader;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.listener.PDXIndexReaderConsoleListenerImpl;
import com.khubla.pdxreader.px.PXFile;
import com.khubla.pdxreader.util.TestUtil;

@Test
public class TestPXFile {
   public void testRead() {
      try {
         final List<String> files = TestUtil.getTestFiles("src/test/resources/examples/", new String[] { ".PX" });
         for (final String filename : files) {
            final File inputFile = new File(filename);
            Assert.assertTrue(inputFile.exists());
            System.out.println(filename);
            final PXFile pxFile = new PXFile();
            pxFile.read(inputFile, new PDXIndexReaderConsoleListenerImpl());
         }
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
