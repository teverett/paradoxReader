package test.com.khubla.pdxreader;

import java.io.*;
import java.util.*;

import org.junit.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.db.*;
import com.khubla.pdxreader.listener.*;
import com.khubla.pdxreader.listener.sql.*;
import com.khubla.pdxreader.util.*;

public class TestDBFile {
	@Test
	public void testReadToCSV() {
		try {
			final List<String> files = TestUtil.getTestFiles("src/test/resources/examples/", new String[] { ".DB" });
			for (final String filename : files) {
				final File inputFile = new File(filename);
				Assert.assertTrue(inputFile.exists());
				System.out.println(filename);
				final DBTableFile pdxFile = new DBTableFile();
				final PDXTableListener pdxReaderListener = new PDXTableReaderCSVListenerImpl();
				pdxFile.read(inputFile, pdxReaderListener);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testReadToSQL() {
		try {
			final List<String> files = TestUtil.getTestFiles("src/test/resources/examples/", new String[] { ".DB" });
			for (final String filename : files) {
				final File inputFile = new File(filename);
				Assert.assertTrue(inputFile.exists());
				System.out.println(filename);
				final DBTableFile pdxFile = new DBTableFile();
				final PDXTableListener pdxReaderListener = new PDXTableReaderSQLListenerImpl();
				pdxFile.read(inputFile, pdxReaderListener);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
