package yhh.bj4.lotterylover;

import android.test.AndroidTestCase;

import java.util.Calendar;
import java.util.List;

import yhh.bj4.lotterylover.fragments.calendar.CalendarUtility;

/**
 * Created by yenhsunhuang on 2016/7/4.
 */
public class CalendarUtilitiesTest extends AndroidTestCase {
    public void testGetAllDateAtYearAndMonthWithSpecificDate() {
        List rtn = CalendarUtility.getAllDateAtYearAndMonth(2016, Calendar.JUNE);
        assertEquals(42, rtn.size());
        rtn = CalendarUtility.getAllDateAtYearAndMonth(2016, Calendar.JANUARY);
        assertEquals(42, rtn.size());
        rtn = CalendarUtility.getAllDateAtYearAndMonth(2026, Calendar.FEBRUARY);
        assertEquals(42, rtn.size());
    }

    public void testGetAllDateAtYearAndMonthRandomly() {
        for (int i = 0; i < 10; ++i) {
            int year = (int) (Math.random() * 10000);
            int month = (int) ((Math.random() * 10000) % 12);
            List rtn = CalendarUtility.getAllDateAtYearAndMonth(year, month);
            assertEquals(42, rtn.size());
        }
    }
}
