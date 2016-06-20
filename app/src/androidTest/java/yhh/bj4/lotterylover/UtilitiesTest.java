package yhh.bj4.lotterylover;

import android.test.AndroidTestCase;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.set(2016, Calendar.JUNE, 13, 0, 0, 0);
        expectedCalendar.set(Calendar.MILLISECOND, 0);
        assertEquals(expectedCalendar.getTimeInMillis(), result);
    }

    public void testAddDigits() {
        assertEquals(4, Utilities.addDigits(49));
    }

    public void testGetLastDigit() {
        assertEquals(4, Utilities.getLastDigit(14));
        assertEquals(8, Utilities.getLastDigit(8));
        assertEquals(2, Utilities.getLastDigit(32));
    }

    public void testAddDigitsOnce() {
        assertEquals(3, Utilities.addDigitsOnce(12));
        assertEquals(4, Utilities.addDigitsOnce(40));
        assertEquals(8, Utilities.addDigitsOnce(17));
    }

    public void testGetPlusAndLastDigitMap() {
        Map<Integer, Integer> map = Utilities.getPlusAndLastDigitMap(50);
        assertEquals(51, map.size());

        assertEquals(0, (int) map.get(0));
        assertEquals(19, (int) map.get(1));
        assertEquals(28, (int) map.get(2));
        assertEquals(37, (int) map.get(3));
        assertEquals(46, (int) map.get(4));

        assertEquals(1, (int) map.get(5));
        assertEquals(10, (int) map.get(6));
        assertEquals(29, (int) map.get(7));
        assertEquals(38, (int) map.get(8));
    }
}
