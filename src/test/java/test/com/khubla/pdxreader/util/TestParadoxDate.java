package test.com.khubla.pdxreader.util;

import java.util.Calendar;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.khubla.pdxreader.util.ParadoxDate;

@Test
public class TestParadoxDate {
   public void testDate1() {
      try {
         Date date = ParadoxDate.getDateFromParadoxDate(0);
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         Assert.assertTrue(calendar.get(Calendar.DATE) == 1);
         Assert.assertTrue(calendar.get(Calendar.MONTH) == 1);
         Assert.assertTrue(calendar.get(Calendar.YEAR) == 1);
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   /*
    * 100 days from January 1, 1 A.D is April 11, 1 A.D.
    */
   public void testDate2() {
      try {
         Date date = ParadoxDate.getDateFromParadoxDate(100);
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         Assert.assertTrue(calendar.get(Calendar.DATE) == 12);
         Assert.assertTrue(calendar.get(Calendar.MONTH) == 4);
         Assert.assertTrue(calendar.get(Calendar.YEAR) == 1);
      } catch (final Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
