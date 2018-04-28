package com.khubla.pdxreader.util;

import java.util.Calendar;
import java.util.Date;

public class ParadoxDate {
   static public Date getDateFromParadoxDate(int d) {
      final Calendar calendar = Calendar.getInstance();
      /*
       * Jan 1, 1 A.D.
       */
      calendar.set(Calendar.YEAR, 1);
      calendar.set(Calendar.MONTH, 1);
      calendar.set(Calendar.DATE, 1);
      /*
       * add days
       */
      calendar.add(Calendar.DATE, d);
      /*
       * return date
       */
      return calendar.getTime();
   }
}
