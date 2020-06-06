package test.com.khubla.pdxreader;

import java.io.*;
import java.util.*;

import org.junit.*;

import com.khubla.pdxreader.listener.*;
import com.khubla.pdxreader.px.*;
import com.khubla.pdxreader.util.*;

public class TestPXFile {
	@Test
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
