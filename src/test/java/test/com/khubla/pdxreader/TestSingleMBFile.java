package test.com.khubla.pdxreader;

import java.io.*;

import org.junit.*;

import com.khubla.pdxreader.mb.*;

public class TestSingleMBFile {
	@Test
	public void testRead() {
		try {
			final File inputFile = new File("src/test/resources/examples/CUSTOMER.MB");
			final MBTableFile mbTableFile = new MBTableFile();
			mbTableFile.read(inputFile);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
