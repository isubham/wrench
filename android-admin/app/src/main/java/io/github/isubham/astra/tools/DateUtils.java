package io.github.isubham.astra.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public DateUtils() {
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }

    public static String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date.getTime());
    }

    public static boolean isEndDateGreaterThanFromDate(String fromDate, String toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date d1 = sdf.parse(fromDate);
            Date d2 = sdf.parse(toDate);

            long fromDateInMillis = d1.getTime();
            long toDateInMillis = d2.getTime();

            return fromDateInMillis <= toDateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFirstDateOfMonth(int monthManipulatedBy) {

        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + monthManipulatedBy, 1);
        return getDate(cal.getTime());
    }
}
