package test.com.khubla.pdxreader;

import java.io.*;
import java.util.*;

import org.junit.*;

import com.khubla.pdxreader.mb.*;
import com.khubla.pdxreader.util.*;

/**
 * @author tom
 */
public class TestMBFile {
	@Test
	public void testRead() {
		try {
			final List<String> files = TestUtil.getTestFiles("src/test/resources/examples/", new String[] { ".MB" });
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
