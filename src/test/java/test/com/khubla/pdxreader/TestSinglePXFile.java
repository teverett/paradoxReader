package test.com.khubla.pdxreader;

import java.io.*;

import org.junit.*;

import com.khubla.pdxreader.listener.*;
import com.khubla.pdxreader.px.*;

public class TestSinglePXFile {
	@Ignore
	@Test
	public void testRead() {
		try {
			final File inputFile = new File("src/test/resources/examples/MTDEMO/VIDORDER.PX");
			final PXFile pxFile = new PXFile();
			pxFile.read(inputFile, new PDXIndexReaderConsoleListenerImpl());
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
