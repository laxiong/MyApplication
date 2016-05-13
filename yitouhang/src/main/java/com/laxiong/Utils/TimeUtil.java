package com.laxiong.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by admin on 2016/5/11.
 */
public class TimeUtil {
    /*
	 * @param "2015-05-31"
	 * @return Calendar
	 */
    public static Calendar StringToCalendar(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /*
     * @param "2015-05-31"
     * @return Date
     */
    public static Date StringToDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(str);
        return date;
    }

    /*
     * @return "2015-05-31"
     */
    public static String calendarToString(Calendar mCalendar) {
        String dateStr = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateStr = sdf.format(mCalendar.getTime());
        } catch (Exception e) {
        }
        return dateStr;
    }


}
