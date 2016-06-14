package yhh.bj4.lotterylover;

import android.test.AndroidTestCase;

import java.util.Calendar;
import java.util.List;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class UtilitiesTest extends AndroidTestCase {
    public void testConvertStringNumberToList() {
        String data1 = "03 , 10 , 14 , 21 , 23 , 36  ";
        List<Integer> result = Utilities.convertStringNumberToList(data1);
        assertEquals(6, result.size());
        assertTrue(3 == result.get(0));
        assertTrue(10 == result.get(1));
        assertTrue(14 == result.get(2));
        assertTrue(21 == result.get(3));
        assertTrue(23 == result.get(4));
        assertTrue(36 == result.get(5));
    }

    public void testConvertStringDateToLong() {
        String date = "2016/06/13";
        long result = Utilities.convertStringDateToLong(date);
        Calendar exptectedCalendar = Calendar.getInstance();
        exptectedCalendar.set(2016, 6, 13, 0, 0, 0);
        exptectedCalendar.set(Calendar.MILLISECOND, 0);
        assertEquals(exptectedCalendar.getTimeInMillis(), result);
    }
}
