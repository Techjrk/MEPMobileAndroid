package com.lecet.app.utility;

import com.lecet.app.data.models.Bid;

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

    public static Date addDays(int days) {
        Calendar calendar = Calendar.getInstance(); // this would default to now
        calendar.add(Calendar.DAY_OF_MONTH, days);

        return calendar.getTime();
    }

    public static Calendar getFirstDateOfTheCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    public static Date parseBidDate(String dateString) {
        try {
            return sBidFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Calendar> getBidDates(List<Bid> bids) {
        List<Calendar> bidDates = new ArrayList<>();
        for (Bid bid : bids) {
            Calendar calendar = Calendar.getInstance();
            Date date = DateUtility.parseBidDate(bid.getCreateDate());
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
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
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar;
    }
}
