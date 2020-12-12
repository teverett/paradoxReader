package com.khubla.pdxreader.util;

import java.util.*;

/**
 * Paradox stores a date as a long integer. The integer contains the date expressed as the number of
 * days since January 1, 1 (the year 1 A.D.). Although dates are expressed (internally) as the
 * number of days since 1/1/1, the lowest year that Paradox allows you to enter is 100. If a value
 * less than 100 is entered, it is treated as 19xx, where xx is the value. The internal
 * representation of 1/1/100 (the lowest valid date) is 36,160 (00008D40h). The internal
 * representation of January 2, 100 is 36,161. The internal representation of May 4, 1996 is decimal
 * 728,783 (000B1ECFh). Paradox accepts dates between Jan 1, 100 and Dec 31, 9999.
 *
 * @author tom
 */
public class ParadoxDate {
	static public Date getDateFromParadoxDate(int d) {
		final Calendar calendar = Calendar.getInstance();
		if (d >= 100) {
			/*
			 * Jan 1, 1 A.D. 0 hour, 0 min, 0 sec
			 */
			calendar.set(Calendar.YEAR, 1);
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DATE, 0);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			/*
			 * add days
			 */
			calendar.add(Calendar.DATE, d);
			/*
			 * return date
			 */
		} else {
			/*
			 * Jan 1, 1 A.D. 0 hour, 0 min, 0 sec
			 */
			calendar.set(Calendar.YEAR, 1900 + d);
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
		}
		return calendar.getTime();
	}
}
