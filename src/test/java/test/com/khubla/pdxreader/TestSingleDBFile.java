package test.com.khubla.pdxreader;

import java.io.*;

import org.junit.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.db.*;
import com.khubla.pdxreader.listener.*;

public class TestSingleDBFile {
	@Test
	public void testRead() {
		try {
			final File inputFile = new File("src/test/resources/examples/HERCULES.DB");
			final DBTableFile pdxFile = new DBTableFile();
			final PDXTableListener pdxReaderListener = new PDXTableReaderDebugListener();
			pdxFile.read(inputFile, pdxReaderListener);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
