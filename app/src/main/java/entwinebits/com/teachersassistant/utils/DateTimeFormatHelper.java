package entwinebits.com.teachersassistant.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shajib on 9/2/2016.
 */
public class DateTimeFormatHelper {

    public static String convetToDate(long callingTime) {
        Date date = new Date(callingTime);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String dateText = df.format(date);
        return dateText;

    }

    public static long convertDateToMiliSec(int selectedYear, int selectedMonth, int selectedDay) {
        Calendar c = Calendar.getInstance();
        c.set(selectedYear, selectedMonth, selectedDay);
        return c.getTimeInMillis();
    }

    public static String convertTimeFormatTo12Hour(int hour, int min){
        String s = hour+":"+min;
        DateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)
        Date d = null;
        try {
            d = f1.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("K:mma");
        return f2.format(d).toLowerCase(); // "12:18am"
    }
}
