package test.com.khubla.pdxreader.util;

import java.util.*;

import org.junit.*;

import com.khubla.pdxreader.util.*;

public class TestParadoxDate {
	/**
	 * date 0 Jan 1, 1 A.D.
	 */
	@Test
	public void testDate1() {
		try {
			final Date date = ParadoxDate.getDateFromParadoxDate(0);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Assert.assertTrue(calendar.get(Calendar.DATE) == 1);
			Assert.assertTrue(calendar.get(Calendar.MONTH) == 1);
			Assert.assertTrue(calendar.get(Calendar.YEAR) == 1);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 100 days from January 1, 1 A.D is April 11, 1 A.D.
	 */
	@Test
	public void testDate2() {
		try {
			final Date date = ParadoxDate.getDateFromParadoxDate(100);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Assert.assertTrue(calendar.get(Calendar.DATE) == 12);
			Assert.assertTrue(calendar.get(Calendar.MONTH) == 4);
			Assert.assertTrue(calendar.get(Calendar.YEAR) == 1);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 2944 days from January 1, 1 A.D is Jan 23, 9 A.D.
	 */
	@Test
	public void testDate3() {
		try {
			final Date date = ParadoxDate.getDateFromParadoxDate(2944);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Assert.assertTrue(calendar.get(Calendar.DATE) == 23);
			Assert.assertTrue(calendar.get(Calendar.MONTH) == 1);
			Assert.assertTrue(calendar.get(Calendar.YEAR) == 9);
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
