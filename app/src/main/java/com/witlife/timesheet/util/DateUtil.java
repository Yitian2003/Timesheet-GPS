package com.witlife.timesheet.util;

import com.witlife.timesheet.model.JobModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by yitian on 6/05/2017.
 */

public class DateUtil {
    public static String getDayOfWeek(Date date){
        String[] parts = date.toString().split(" ");
        return parts[0];
    }

    public static String getDate(Date date){
        String[] parts = date.toString().split(" ");
        return parts[1] + " " + parts[2] + ", " + parts[5];
    }

    public static String getDateAndDayOfWeek(Date date) {
        String[] parts = date.toString().split(" ");
        return parts[0] + ", " + parts[1] + " " + parts[2];
    }

    public static Date stringToDate(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy");
        Date date = null;
        try
        {
            date = sdf.parse(input);

        }
        catch (Exception ex ){
            System.out.println(ex);
        }
        return date;
    }

    public static String getTime(Date date){
        String[] parts = date.toString().split(" ");
        return parts[3];
    }

    public static String convertMonth(int month) {
        String monthString = "";
        switch (month) {
            case 1:
                monthString = "Jan";
                break;
            case 2:
                monthString = "Feb";
                break;
            case 3:
                monthString = "Mar";
                break;
            case 4:
                monthString = "Apr";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "Jun";
                break;
            case 7:
                monthString = "Jul";
                break;
            case 8:
                monthString = "Aug";
                break;
            case 9:
                monthString = "Sep";
                break;
            case 10:
                monthString = "Oct";
                break;
            case 11:
                monthString = "Nov";
                break;
            case 12:
                monthString = "Dec";
                break;
            default:
                break;
        }
        return monthString;
    }

    public static int compareTime(String timeString1, String timeString2) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date time1 = new Date();
        Date time2 = new Date();
        try {
            time1 = sdf.parse(timeString1);
            time2 = sdf.parse(timeString2);
        } catch (ParseException e){

        }
        return time1.compareTo(time2);
    }

    public static double timeDifferenceInHours(String timeString1, String timeString2) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date time1 = new Date();
        Date time2 = new Date();
        try {
            time1 = sdf.parse(timeString1);
            time2 = sdf.parse(timeString2);
        } catch (ParseException e ) {

        }
        double mills = time1.getTime() - time2.getTime();
        return mills /(1000 * 60 * 60);
    }

    public static boolean isSameDay(Date d1, Date d2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (d1 == null || d2 == null) {
            return false;
        }

        String strD1 = sdf.format(d1);
        String strD2 = sdf.format(d2);

        return strD1.equals(strD2);
    }

    public static boolean isSameWeek(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setFirstDayOfWeek(Calendar.MONDAY);
        c2.setFirstDayOfWeek(Calendar.MONDAY);
        c1.setTime(d1);
        c2.setTime(d2);
        return c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getWeekNo(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static String getRangeOfWeek(int weekNo){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.WEEK_OF_YEAR, weekNo);
        calendar.set(Calendar.YEAR, 2017);
        Date firstDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_WEEK, 8);
        Date lastDate = calendar.getTime();
        return DateUtil.getDate(firstDate) + " - " + DateUtil.getDate(lastDate);
    }
}
