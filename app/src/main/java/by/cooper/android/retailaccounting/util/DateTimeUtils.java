package by.cooper.android.retailaccounting.util;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

public final class DateTimeUtils {
    private static final int DEFAULT_HOUR_OF_DAY = 12;
    private static final int DEFAULT_MINUTE = 0;

    private DateTimeUtils() {
    }

    public static String convertDateMillisToPattern(long millis, String pattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(millis);
    }

    public static Calendar getCalendarInLocal() {
        DateTime dateTime = DateTime.now(DateTimeZone.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.toDate());
        return calendar;
    }

    public static Calendar getCalendarInLocal(long millis) {
        DateTime dateTime = new DateTime(DateTimeZone.UTC.convertUTCToLocal(millis));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.toDate());
        return calendar;
    }

    public static long getUtcTimeFromFields(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, DEFAULT_HOUR_OF_DAY, DEFAULT_MINUTE);
        LocalDateTime localDateTime = LocalDateTime.fromDateFields(calendar.getTime());
        DateTimeZone timeZone = DateTimeZone.UTC;
        return localDateTime.toDateTime(timeZone).getMillis();
    }
}
