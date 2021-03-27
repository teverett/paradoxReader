package test.com.khubla.pdxreader.util;

import java.util.*;

import org.junit.*;

import com.khubla.pdxreader.util.*;

public class TestParadoxDate {
	/**
	 * date 0 Jan 1, 1900
	 */
	@Test
	public void testDate1() {
		try {
			final Date date = ParadoxDate.getDateFromParadoxDate(0);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Assert.assertTrue(calendar.get(Calendar.DATE) == 1);
			Assert.assertTrue(calendar.get(Calendar.MONTH) == 0);
			Assert.assertTrue(calendar.get(Calendar.YEAR) == 1900);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * The internal representation of 1/1/100 (the lowest valid date) is 36,160 (00008D40h)
	 */
	@Test
	public void testDate2() {
		try {
			final Date date = ParadoxDate.getDateFromParadoxDate(36160);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Assert.assertTrue(calendar.get(Calendar.DATE) == 1);
			Assert.assertTrue(calendar.get(Calendar.MONTH) == 0);
			Assert.assertTrue(calendar.get(Calendar.YEAR) == 100);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * The internal representation of May 4, 1996 is decimal 728,783 (000B1ECFh)
	 */
	@Test
	public void testDate3() {
		try {
			final Date date = ParadoxDate.getDateFromParadoxDate(728783);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Assert.assertTrue(calendar.get(Calendar.DATE) == 4);
			// month is 0 indexed, so May is the 4th month
			Assert.assertTrue(calendar.get(Calendar.MONTH) == 4);
			Assert.assertTrue(calendar.get(Calendar.YEAR) == 1996);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * The internal representation of January 2, 100 is 36,161.
	 */
	@Test
	public void testDate4() {
		try {
			final Date date = ParadoxDate.getDateFromParadoxDate(36161);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Assert.assertTrue(calendar.get(Calendar.DATE) == 2);
			// month is 0 indexed, so Jan is 0
			Assert.assertTrue(calendar.get(Calendar.MONTH) == 0);
			Assert.assertTrue(calendar.get(Calendar.YEAR) == 100);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
