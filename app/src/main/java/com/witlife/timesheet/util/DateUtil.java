package com.witlife.timesheet.util;

import java.util.Date;

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
        return parts[1] + " " + parts[2];
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
}
