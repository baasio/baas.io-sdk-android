/**
 * KTH Developed by Java
 *
 * @Copyright 2011 by Service Platform Development Team, KTH, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of KTH, Inc.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with KTH.
 */

package com.kth.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ISO8601Util {

    public static String now() {
        return getISO9601FromCalendar(GregorianCalendar.getInstance());
    }

    public static String getISO9601FromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
                .format(date);
        return formatted.substring(0, 26) + ":" + formatted.substring(26);
    }

    public static Calendar getCalendarFromISO8601(final String iso8601string) throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 26) + s.substring(27);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
                .parse(s);
        calendar.setTime(date);
        return calendar;
    }

    public static String getISO8601FromUnixTime(final long millis) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(millis);

        return getISO9601FromCalendar(calendar);
    }

    public static long getUnixTimeFromISO8601(final String iso8601string) throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();

        String s = iso8601string.replace("Z", "+00:00");

        if (iso8601string.length() <= 22) {
            try {
                s = s.substring(0, 19) + s.substring(20);
            } catch (IndexOutOfBoundsException e) {
                throw new ParseException("Invalid length", 0);
            }

            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.getDefault()).parse(s);
            calendar.setTime(date);
        } else if (iso8601string.length() <= 25) {
            try {
                s = s.substring(0, 22) + s.substring(23);
            } catch (IndexOutOfBoundsException e) {
                throw new ParseException("Invalid length", 0);
            }

            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
                    .parse(s);
            calendar.setTime(date);
        } else {
            try {
                s = s.substring(0, 26) + s.substring(27);
            } catch (IndexOutOfBoundsException e) {
                throw new ParseException("Invalid length", 0);
            }

            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
                    .parse(s);
            calendar.setTime(date);
        }

        return calendar.getTimeInMillis();
    }
}
