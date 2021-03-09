package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Weather {
    private int temp;
    private int tempFeelsLike;
    private int humidity;
    private String mainForWeatherImage;  //clouds, rain , snow...
    private String description;
    private String windSpeed;
    private String windDirection;             // in dergees;
    private String visibility;
    private String timeOfWeather;
    private String iconWeather;
    private ArrayList<Integer> arrayTempHumidWind;
    private ArrayList<String> arrayDescripVisibilTime;

    ArrayList<Integer> getTempHumid(String inputJSONstring, String keyForTaking) {
        arrayTempHumidWind = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(inputJSONstring);
            int jsonArrLength = jsonObject.getJSONArray("list").length();
            for (int i = 0; i < jsonArrLength; i++) {
                arrayTempHumidWind.add((int) Math.round(jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main").getDouble(keyForTaking)));
            }
           // System.out.println("LENGTH  " + jsonObject.getJSONArray("list").length());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayTempHumidWind;
    }

    ArrayList<String> getDescripVisibilTime(String inputJSONstring, String keyForTaking) {
        arrayDescripVisibilTime = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(inputJSONstring);
            int jsonArrLength = jsonObject.getJSONArray("list").length();
            if (keyForTaking.equals("speed")) {
                for (int i = 0; i < jsonArrLength; i++) {
                    arrayDescripVisibilTime.add(jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("wind").getString(keyForTaking));
                }
            } else if (keyForTaking.equals("deg")) {
                for (int i = 0; i < jsonArrLength; i++) {
                    int degOfWind = jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("wind").getInt(keyForTaking);
                    String descriptionOfWind = getWindirrectionDescription(degOfWind);
                    arrayDescripVisibilTime.add(descriptionOfWind + " (" + degOfWind +"°)");
                    ;
                }
            } else if (keyForTaking.equals("dt_txt") || keyForTaking.equals("visibility")) {
                for (int i = 0; i < jsonArrLength; i++) {
                    arrayDescripVisibilTime.add(jsonObject.getJSONArray("list").getJSONObject(i).getString(keyForTaking));
                }

            } else {
                for (int i = 0; i < jsonArrLength; i++) {
                    arrayDescripVisibilTime.add(jsonObject.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString(keyForTaking));


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayDescripVisibilTime;
    }


    private String getWindirrectionDescription(int degOfWind) {
        String descriptionOfWind = "";
        if (degOfWind == 0) {
            descriptionOfWind = "C";
        } else if (degOfWind == 90) {
            descriptionOfWind = "B";
        } else if (degOfWind == 180) {
            descriptionOfWind = "Ю";
        } else if (degOfWind == 270) {
            descriptionOfWind = "З";
        } else if (degOfWind > 0 && degOfWind < 90) {
            descriptionOfWind = "CB";
        } else if (degOfWind > 90 && degOfWind < 180) {
            descriptionOfWind = "ЮB";
        } else if (degOfWind > 180 && degOfWind < 270) {
            descriptionOfWind = "ЮЗ";
        } else if (degOfWind > 270 && degOfWind < 360) {
            descriptionOfWind = "CЗ";
        } else
            descriptionOfWind = "incorrect date";

        return descriptionOfWind;
    }
}

