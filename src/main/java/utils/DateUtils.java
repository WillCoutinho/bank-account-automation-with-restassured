package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static String getDateBetweenDaysAndCurrentDate(Integer quantityDays){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, quantityDays);
        return getFormattedDate(cal.getTime());
    }

    public static String getFormattedDate(Date date){
        DateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDate.format(date);


    }

}
