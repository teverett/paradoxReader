package test.com.khubla.pdxreader;

import org.junit.*;

import com.khubla.pdxreader.*;
import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.listener.*;

public class TestDatabase {
	@Test
	public void testRead() {
		try {
			final Database database = new Database("src/test/resources/examples/AREA-PDX");
			Assert.assertTrue(database.getMbfiles().size() == 0);
			Assert.assertTrue(database.getDbfiles().size() == 1);
			Assert.assertTrue(database.getPxfiles().size() == 1);
			Assert.assertTrue(database.getValfiles().size() == 1);
			final PDXTableListener pdxReaderListener = new PDXTableReaderCSVListenerImpl();
			database.readTables(pdxReaderListener);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
