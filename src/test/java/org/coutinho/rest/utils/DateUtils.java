package org.coutinho.rest.utils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    @NotNull
    public static String getDateBetweenDaysAndCurrentDate(Integer quantityDays){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, quantityDays);
        return getFormattedDate(cal.getTime());
    }

    @NotNull
    public static String getFormattedDate(Date date){
        DateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDate.format(date);
    }
}
