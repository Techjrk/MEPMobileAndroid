package com.lecet.app.utility;

import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
            Date date = project.getBidDate();
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
}
