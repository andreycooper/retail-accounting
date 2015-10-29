package by.cooper.android.retailaccounting.util;


import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class Utils {
    private Utils() {
    }

    public static String convertDateMillisToPattern(long millis, String pattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(millis);
    }
}
