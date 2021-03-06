package com.example.weatherapp;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSOutput;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CityDescription {
   private String cityName;
   private String population;
   private String timeOfSunrise;
   private String timeOfSunset;
   private String timeDayLength;
   private long timeZone;
   private long sunrise;
   private long sunset;
   private long dayLength;
   private Map<String, String> cityParameters;


    Map<String, String> getCity(String inputJSONstring) {
        cityParameters = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(inputJSONstring);
            cityName = jsonObject.getJSONObject("city").getString("name");
            population = jsonObject.getJSONObject("city").getString("population");
            Calendar calender = Calendar.getInstance();
            timeZone = jsonObject.getJSONObject("city").getLong("timezone") * 1000;
            sunrise = jsonObject.getJSONObject("city").getLong("sunrise") * 1000;
            sunset = jsonObject.getJSONObject("city").getLong("sunset") * 1000;
            dayLength = sunset - sunrise;


            calender.setTimeInMillis(sunrise);
            int hourSunrise = calender.get(Calendar.HOUR_OF_DAY);
            int minuteSunrise = calender.get(Calendar.MINUTE);
            timeOfSunrise = String.format(Locale.getDefault(), "%d:%02d", hourSunrise, minuteSunrise);


            calender.setTimeInMillis(sunset);
            int hourSunset = calender.get(Calendar.HOUR_OF_DAY);
            int minuteSunset = calender.get(Calendar.MINUTE);
            timeOfSunset = String.format(Locale.getDefault(), "%d:%02d", hourSunset, minuteSunset);
            //System.out.println(hourSunset + ":" + minuteSunset);

            calender.setTimeInMillis(dayLength-timeZone);
            int hourDayLength = calender.get(Calendar.HOUR_OF_DAY);
            int minuteDayLength = calender.get(Calendar.MINUTE);
            timeDayLength = String.format(Locale.getDefault(),"%dч. %02dмин.", hourDayLength, minuteDayLength);
            System.out.println(dayLength);
            System.out.println(hourDayLength+"  "+ minuteDayLength);
            System.out.println(timeDayLength);

            cityParameters.put("cityName", cityName);
            cityParameters.put("population", population);
            cityParameters.put("timeOfSunrise", timeOfSunrise);
            cityParameters.put("timeOfSunset", timeOfSunset);
            cityParameters.put("timeDayLength", timeDayLength);

//            weather = String.format("Погода в г. %s \nНаселение: %s человек\nРассвет: %s \nЗакат: %s \nПродолжительность дня: %s"
//                    , cityName, population, timeOfSunrise, timeOfSunset, timeDayLength);
//
//            textViewWeather.setText(weather);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("incorrect");
        }
        return cityParameters;
    }
}
