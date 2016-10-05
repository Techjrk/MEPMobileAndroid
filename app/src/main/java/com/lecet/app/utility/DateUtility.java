package com.lecet.app.utility;

import java.util.Calendar;
import java.util.Date;

/**
 * File: DateUtility Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class DateUtility {

    public static Date addDays(int days) {
        Calendar calendar = Calendar.getInstance(); // this would default to now
        calendar.add(Calendar.DAY_OF_MONTH, days);

        return calendar.getTime();
    }

}
