package com.example.weatherapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeOfObservation {
   private String today;
   private String tommorow;
   private String tommorow2;
   private String tommorow3;
   private String tommorow4;
   private String resultForOut;
   private String resultForSearch;
   private String result;
   private Date date1;


    String getDateForView(String date, int dayShift) {

        if (date != null) {
            DateFormat dateFormatForOut = new SimpleDateFormat("EEEE (dd-MM-yyyy)", Locale.getDefault());
            DateFormat dateFormatForSearch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                date1 = dateFormatForSearch.parse(date);
                //System.out.println(date);

               // System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DAY_OF_MONTH, dayShift);
            resultForOut = dateFormatForOut.format(calendar.getTime());
            resultForSearch = dateFormatForSearch.format(calendar.getTime());
            result = resultForOut + "&" + resultForSearch;

//            System.out.println(resultForOut);
//            System.out.println(resultForSearch);
        }
        return result;
    }

    String getDateForRadioButton(String date, int dayShift) {
        if (date != null) {
            DateFormat dateFormatForOut = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DAY_OF_MONTH, dayShift);
            result = dateFormatForOut.format(calendar.getTime());
        }
        return result;
    }
}
