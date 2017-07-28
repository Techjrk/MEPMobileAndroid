package com.lecet.app.utility;

import android.util.Log;

import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * File: DateUtility Created: 10/5/16 Author: domandtom
 * <p>
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class DateUtility {

    private static final SimpleDateFormat sBidFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final SimpleDateFormat sDisplayFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static Date addDays(int days) {
        Calendar calendar = Calendar.getInstance(); // this would default to now
        calendar.add(Calendar.DAY_OF_MONTH, days);

        return calendar.getTime();
    }

    public static Date addDays(Date date, int days) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);

        return calendar.getTime();
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance(); // this would default to now
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);

        return calendar.getTime();
    }

    public static Date addMinutes(int minutes) {
        Calendar calendar = Calendar.getInstance(); // this would default to now
        calendar.add(Calendar.MINUTE, minutes);

        return calendar.getTime();
    }

    public static Calendar getFirstDateOfTheCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    public static Date getLastDateOfTheCurrentMonth() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date parseBidDate(String dateString) {
        try {
            return sBidFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateForDisplay(Date date) {
        if(date != null) {
            return sDisplayFormat.format(date);
        }
        return null;
    }

    public static Date setDateToStartOfDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime();
    }

    public static List<Calendar> getBidDates(List<Bid> bids) {
        List<Calendar> bidDates = new ArrayList<>();
        for (Bid bid : bids) {
            Calendar calendar = Calendar.getInstance();
            Date date = bid.getCreateDate();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            bidDates.add(calendar);
        }
        return bidDates;
    }

    public static List<Calendar> getProjectDates(List<Project> projects) {

        List<Calendar> bidDates = new ArrayList<>();
        for (Project project : projects) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getDefault());
            //calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = project.getBidDateCalendar();
            //Date date = project.getBidDate();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);

            bidDates.add(calendar);
        }
        return bidDates;
    }

    public static Calendar getCalendarHour0(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar;
    }
    public static Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance(); // this would default to now
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);

        return calendar.getTime();
    }

    public static Date convertUTCDate2LocalDate(Date UTCDate) {
        if (UTCDate == null) return null;
        int hoursdiff=0;
        Calendar cal = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        cal.setTimeZone(tz);
        sdf.setTimeZone(tz);
        cal.setTime(UTCDate);

        long longUTC = cal.getTimeInMillis();
        int inUTC = tz.getOffset(longUTC);

        String sdate = sdf.format(UTCDate);

        Log.d("Time zone1: ", "Timezone1:" + tz.getDisplayName() + "date: " + sdate);
        //  sdf.setTimeZone(cal.getTimeZone());

        // TimeZone tz = cal.getTimeZone();
        tz = TimeZone.getDefault();
        cal.setTimeZone(tz);
        sdf.setTimeZone(tz);
        long longLocal = cal.getTimeInMillis();
        int inLocal = tz.getOffset(longLocal);
        hoursdiff = (inUTC - inLocal)/ (1000 * 60 * 60);
        Log.d("Time diff: ", "Timezonediff:" + hoursdiff);

        sdate = sdf.format(UTCDate);

        Log.d("Time zone2: ", "Timezone2:" + tz.getDisplayName() + "date: " + sdate);

/* date formatter in local timezone */

        Date ddate = null;
        try {
            ddate = sdf.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DateUtility.addHours(UTCDate,-hoursdiff);
    }
}
