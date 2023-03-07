package cn.bossfriday.jmeter.utils;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtil
 *
 * @author chenx
 */
public class DateUtil {

    public static final String DEFAULT_DATE_FORMAT_MILL = "yyyyMMddHHmmssSSS";
    public static final String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";
    public static final String DEFAULT_DATE_HYPHEN_FORMAT = "yyyyMMdd";
    public static final String DEFAULT_DATETIME_HYPHEN_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtil() {

    }

    /***
     * date2Str
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String date2Str(Date date, String pattern) {
        if (date == null) {
            return null;
        }

        if (StringUtils.isEmpty(pattern)) {
            pattern = DEFAULT_DATETIME_HYPHEN_FORMAT;
        }

        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * str2Date
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date str2Date(String dateStr, String formatStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
        return simpleDateFormat.parse(dateStr);
    }

    /**
     * str2Calendar
     *
     * @param dateStr
     * @param formatStr
     * @return
     * @throws ParseException
     */
    public static Calendar str2Calendar(String dateStr, String formatStr) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(str2Date(dateStr, formatStr));

        return calendar;
    }

    /**
     * timestampToLong
     *
     * @param timestamp
     * @return
     */
    public static long timestampToLong(Timestamp timestamp) {
        return timestamp.getTime();
    }

    /**
     * longToTimestamp
     *
     * @param time
     * @return
     */
    public static Timestamp longToTimestamp(long time) {
        return new Timestamp(time);
    }
}
