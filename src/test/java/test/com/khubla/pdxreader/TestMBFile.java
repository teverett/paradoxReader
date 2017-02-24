package test.com.khubla.pdxreader;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.mb.MBTableFile;
import com.khubla.pdxreader.util.TestUtil;

/**
 * @author tom
 */
@Test
public class TestMBFile {
   public void testRead() {
      try {
         final List<String> files = TestUtil.getTestFiles("src/test/resources/", new String[] { ".MB" });
         for (final String filename : files) {
            final File inputFile = new File(filename);
            Assert.assertTrue(inputFile.exists());
            System.out.println(filename);
            final MBTableFile mbTableFile = new MBTableFile();
            mbTableFile.read(inputFile);
         }
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
