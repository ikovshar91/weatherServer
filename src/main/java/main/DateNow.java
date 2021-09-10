package main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateNow {
    public static String getDateNow(){
        Date date = new Date();
        SimpleDateFormat formatSimpleDate = new SimpleDateFormat("yyyy-MM-dd 'T' hh:mm:ss a zzz");

        return formatSimpleDate.format(date);
    }
}
