package test.com.khubla.pdxreader;

import java.io.*;
import java.util.*;

import org.junit.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.db.*;

/**
 * Asserts decoded values from the example tables, which are Borland's DBDEMOS sample data.
 */
public class TestNumericFields {
	static List<Map<String, String>> read(String filename) throws Exception {
		final List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		new DBTableFile().read(new File(filename), new PDXTableListener() {
			private List<DBTableField> fields;

			@Override
			public void finish() {
				/*
				 * nothing to close: rows are collected as they arrive
				 */
			}

			@Override
			public void header(DBTableHeader pdxTableHeader) {
				fields = pdxTableHeader.getFields();
			}

			@Override
			public void record(List<DBTableValue> values) {
				final Map<String, String> row = new HashMap<String, String>();
				for (int i = 0; i < values.size(); i++) {
					row.put(fields.get(i).getName(), values.get(i).getValue());
				}
				rows.add(row);
			}

			@Override
			public void start(String filename) {
				/*
				 * the filename is already known to the caller
				 */
			}
		});
		return rows;
	}

	@Test
	public void testNumberAndCurrency() throws Exception {
		final Map<String, String> first = read("src/test/resources/examples/ORDERS.DB").get(0);
		Assert.assertEquals("1001.0", first.get("Order No"));
		Assert.assertEquals("1221.0", first.get("Customer No"));
		Assert.assertEquals("7320.0", first.get("Total Invoice"));
		Assert.assertEquals("7320.0", first.get("Amount Paid"));
		Assert.assertEquals("0.0", first.get("Balance Due"));
	}

	@Test
	public void testAutoIncrement() throws Exception {
		final List<Map<String, String>> rows = read("src/test/resources/examples/CUSTOMER.DB");
		for (int i = 0; i < rows.size(); i++) {
			Assert.assertEquals(Integer.toString(i + 1), rows.get(i).get("CustNo"));
		}
	}

	@Test
	public void testDate() throws Exception {
		Assert.assertEquals("725830", read("src/test/resources/examples/ORDERS.DB").get(0).get("Sale Date"));
	}
}
