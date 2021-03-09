package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=bbc8c0f7cacf86d2b827d6167514dc9a&lang=ru&units=metric";
    private final String IMAGE_URL = "http://openweathermap.org/img/wn/%s@2x.png";
    // private final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?q=moscow&appid=bbc8c0f7cacf86d2b827d6167514dc9a&lang=ru&units=metric";
    private String url;
    private String urlIcon;
    private EditText editTextCity;
    private TextView textViewWeather;
    private TextView textViewWeatherMorning;
    private TextView textViewWeatherAfternoon;
    private TextView textViewWeatherEvening;
    private TextView textViewWeatherNight;
    private ImageView imageViewWeatherMorning;
    private ImageView imageViewWeatherAfternoon;
    private ImageView imageViewWeatherEvening;
    private ImageView imageViewWeatherNight;
    private ImageView imageViewTemp;
    private ImageView imageViewHumidity;
    private ImageView imageViewWind;
    private ImageView imageViewTimeDay;
    private String showWeather;
    private String showWeatherExtra;
    private String city;
    private String inputJSONstring;
    private Map<String, String> cityParameters;
    private ArrayList<Integer> arrayTemp;
    private ArrayList<Integer> arrayTempFeels;
    private ArrayList<Integer> arrayHumidity;
    private ArrayList<String> mainForWeatherImage;  //clouds, rain , snow...
    private ArrayList<String> description;
    private ArrayList<String> windSpeed;
    private ArrayList<String> windDirection;
    private ArrayList<String> visibility;
    private ArrayList<String> timeOfWeather;
    private ArrayList<String> iconWeather;
    private Button buttonExtra;
    private int positionRadioButton;
    private RadioGroup radioGroup;
    private RadioButton buttonToday;
    private RadioButton buttonTommorow;
    private RadioButton buttonTommorow2;
    private RadioButton buttonTommorow3;
    private RadioButton buttonTommorow4;
    private String resultFromTimeOfObservation;
    private String dateForOut;
    private String dateForSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextCity = findViewById(R.id.editTextTextCity);
        textViewWeather = findViewById(R.id.textViewWeather);
        textViewWeatherMorning = findViewById(R.id.textViewWeatherMorning);
        textViewWeatherAfternoon = findViewById(R.id.textViewWeatherAfternoon);
        textViewWeatherEvening = findViewById(R.id.textViewWeatherEvening);
        textViewWeatherNight = findViewById(R.id.textViewWeatherNight);
        imageViewWeatherMorning = findViewById(R.id.imageViewMorning);
        imageViewWeatherAfternoon = findViewById(R.id.imageViewAfternon);
        imageViewWeatherEvening = findViewById(R.id.imageViewEvening);
        imageViewWeatherNight = findViewById(R.id.imageViewNight);
        imageViewTemp = findViewById(R.id.imageViewTemp);
        imageViewHumidity = findViewById(R.id.imageViewHumidity);
        imageViewWind = findViewById(R.id.imageViewWind);
        imageViewTimeDay = findViewById(R.id.imageViewTimeDay);
        buttonToday = findViewById(R.id.radioButtonToday);
        buttonTommorow = findViewById(R.id.radioButtonTommorow);
        buttonTommorow2 = findViewById(R.id.radioButtonTommorow2);
        buttonTommorow3 = findViewById(R.id.radioButtonTommorow3);
        buttonTommorow4 = findViewById(R.id.radioButtonTommorow4);
        radioGroup = findViewById(R.id.radioGroup);


        if (savedInstanceState != null) {
            city = savedInstanceState.getString("city");
        }else city = "Saint Petersburg";
        try {
            DownloadWeather task = new DownloadWeather();
            url = String.format(WEATHER_URL, city);
            inputJSONstring = task.execute(url).get();
            int index = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));      //index of current accepted button
            getContent(inputJSONstring, index);

            TimeOfObservation timeOfObservation = new TimeOfObservation();
            String s = String.format(getString(R.string.radiobuttonAfterTommorow), timeOfObservation.getDateForRadioButton(timeOfWeather.get(0), 2));
            buttonTommorow2.setText(s);
            s = String.format(getString(R.string.radiobutton2AfterTommorow), timeOfObservation.getDateForRadioButton(timeOfWeather.get(0), 3));
            buttonTommorow3.setText(s);
            s = String.format(getString(R.string.radiobutton3AfterTommorow), timeOfObservation.getDateForRadioButton(timeOfWeather.get(0), 4));
            buttonTommorow4.setText(s);

            imageViewTemp.setImageResource(R.drawable.temp);
            imageViewHumidity.setImageResource(R.drawable.humidity);
            imageViewWind.setImageResource(R.drawable.roseofwinds);
            imageViewTimeDay.setImageResource(R.drawable.timeday);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }



        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
                        positionRadioButton = radioGroup.indexOfChild(checkedRadioButton);
                        getContent(inputJSONstring, positionRadioButton);
                        System.out.println("checkedIndex = " + positionRadioButton);

                    }
                });

        buttonExtra = findViewById(R.id.buttonExtra);
        buttonExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resForSecondActivity = getContentForSecondActivity(dateForSearch);
                Intent intent = new Intent(getApplicationContext(), ExtraActivity.class);
                intent.putExtra("result",resForSecondActivity);
                startActivity(intent);
            }
        });
    }


    public void onClickShowWeather(View view) {
        city = editTextCity.getText().toString().trim();

        try {
            if (!city.isEmpty()) {
                buttonToday.setChecked(true);
                DownloadWeather task = new DownloadWeather();
                url = String.format(WEATHER_URL, city);
                inputJSONstring = task.execute(url).get();

                getContent(inputJSONstring, positionRadioButton);

            } else {
                Toast.makeText(this, "Empty input field", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city", city);
    }

    private void getContent(String inputJSONstring, int positionRadioButton) {
        if (inputJSONstring == null) {
            Toast.makeText(getApplicationContext(), "Incorrect City name or not internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        CityDescription cityDescription = new CityDescription();
        cityParameters = cityDescription.getCity(inputJSONstring);

        Weather weather = new Weather();
        arrayTemp = weather.getTempHumid(inputJSONstring, "temp");
        arrayTempFeels = weather.getTempHumid(inputJSONstring, "feels_like");
        arrayHumidity = weather.getTempHumid(inputJSONstring, "humidity");
        mainForWeatherImage = weather.getDescripVisibilTime(inputJSONstring, "main");
        description = weather.getDescripVisibilTime(inputJSONstring, "description");

        windSpeed = weather.getDescripVisibilTime(inputJSONstring, "speed");
        windDirection = weather.getDescripVisibilTime(inputJSONstring, "deg");
        visibility = weather.getDescripVisibilTime(inputJSONstring, "visibility");
        timeOfWeather = weather.getDescripVisibilTime(inputJSONstring, "dt_txt");
        iconWeather = weather.getDescripVisibilTime(inputJSONstring, "icon");

//        showWeatherExtra = String.format("Рассвет: %s \nЗакат: %s \nПродолжительность дня: %s ",
//                cityParameters.get("timeOfSunrise"),
//                cityParameters.get("timeOfSunset"), cityParameters.get("timeDayLength"));

        if (positionRadioButton == 0) {
            TimeOfObservation timeOfObservation = new TimeOfObservation();
            resultFromTimeOfObservation = timeOfObservation.getDateForView(timeOfWeather.get(0), 0);
            dateForOut = resultFromTimeOfObservation.substring(0, resultFromTimeOfObservation.indexOf("&"));
            dateForSearch = resultFromTimeOfObservation.substring(resultFromTimeOfObservation.indexOf("&") + 1);
            StringBuilder res = makeAStringForOutPut();
            textViewWeather.setText(res);
            chekingTime(dateForSearch);  // showing Weather to Spinner
        } else if (positionRadioButton == 1) {
            TimeOfObservation timeOfObservation = new TimeOfObservation();
            resultFromTimeOfObservation = timeOfObservation.getDateForView(timeOfWeather.get(0), 1);
            dateForOut = resultFromTimeOfObservation.substring(0, resultFromTimeOfObservation.indexOf("&"));
            dateForSearch = resultFromTimeOfObservation.substring(resultFromTimeOfObservation.indexOf("&") + 1);
            StringBuilder res = makeAStringForOutPut();
            textViewWeather.setText(res);
            chekingTime(dateForSearch);  // showing Weather to Spinner
        } else if (positionRadioButton == 2) {
            TimeOfObservation timeOfObservation = new TimeOfObservation();
            resultFromTimeOfObservation = timeOfObservation.getDateForView(timeOfWeather.get(0), 2);
            dateForOut = resultFromTimeOfObservation.substring(0, resultFromTimeOfObservation.indexOf("&"));
            dateForSearch = resultFromTimeOfObservation.substring(resultFromTimeOfObservation.indexOf("&") + 1);
            StringBuilder res = makeAStringForOutPut();
            textViewWeather.setText(res);
            chekingTime(dateForSearch);  // showing Weather to Spinner
        } else if (positionRadioButton == 3) {
            TimeOfObservation timeOfObservation = new TimeOfObservation();
            resultFromTimeOfObservation = timeOfObservation.getDateForView(timeOfWeather.get(0), 3);
            dateForOut = resultFromTimeOfObservation.substring(0, resultFromTimeOfObservation.indexOf("&"));
            dateForSearch = resultFromTimeOfObservation.substring(resultFromTimeOfObservation.indexOf("&") + 1);
            StringBuilder res = makeAStringForOutPut();
            textViewWeather.setText(res);
            chekingTime(dateForSearch);  // showing Weather to Spinner
        } else if (positionRadioButton == 4) {
            TimeOfObservation timeOfObservation = new TimeOfObservation();
            resultFromTimeOfObservation = timeOfObservation.getDateForView(timeOfWeather.get(0), 4);
            dateForOut = resultFromTimeOfObservation.substring(0, resultFromTimeOfObservation.indexOf("&"));
            dateForSearch = resultFromTimeOfObservation.substring(resultFromTimeOfObservation.indexOf("&") + 1);
            StringBuilder res = makeAStringForOutPut();
            textViewWeather.setText(res);
            chekingTime(dateForSearch);  // showing Weather to Spinner
        }


    }

    private StringBuilder makeAStringForOutPut() {
        System.out.println(dateForSearch);
        StringBuilder finalString = new StringBuilder();
        finalString.append(String.format("Дата: %s\nПогода в г. %s \nНаселение: %s человек",
                dateForOut, cityParameters.get("cityName"), cityParameters.get("population")));
        return finalString;
    }


    private String makeAExtraStringForOutPut(String dateForSearch) {
        StringBuilder finalString = new StringBuilder();

        for (int i = 0; i < timeOfWeather.size(); i++) {

            if (timeOfWeather.get(i).contains(dateForSearch)) {
                finalString.append(
                        String.format("%s, \nОщущается как: %s \nНа улице %s " +
                                        "\n\n\nВлажность: %s%%\n\n\n\nВетер: %s, %s м/с" +
                                        "\n\n\n\nПродолжительность дня: %s  ",
                                arrayTemp.get(i), arrayTempFeels.get(i), description.get(i),
                                arrayHumidity.get(i), windDirection.get(i), windSpeed.get(i),
                                cityParameters.get("timeDayLength")));
            }
        }
        return finalString.toString();
    }

    private String getPositionInArrayForIconWeather(String dateForSearch) {
        if (dateForSearch != null) {
            for (int i = 0; i < timeOfWeather.size(); i++) {
                if (timeOfWeather.get(i).contains(dateForSearch)) {
                    return iconWeather.get(i);
                }
                // System.out.println(iconWeather.get(i));
            }
        }
        return null;
    }

    private void chekingTime(String dateForSearch) {
        textViewWeatherMorning.setText(null);
        textViewWeatherAfternoon.setText(null);
        textViewWeatherEvening.setText(null);
        textViewWeatherNight.setText(null);

        imageViewWeatherMorning.setImageBitmap(null);
        imageViewWeatherAfternoon.setImageBitmap(null);
        imageViewWeatherEvening.setImageBitmap(null);
        imageViewWeatherNight.setImageBitmap(null);


        if (!makeAExtraStringForOutPut(dateForSearch + " 06:00:00").isEmpty()) {
            textViewWeatherMorning.setText("Температура утром: " + makeAExtraStringForOutPut(dateForSearch + " 06:00:00"));
            try {
                DownloadImageForWeather downloadImageForWeather = new DownloadImageForWeather();
                urlIcon = String.format(IMAGE_URL, getPositionInArrayForIconWeather(dateForSearch + " 06:00:00"));
                imageViewWeatherMorning.setImageBitmap(downloadImageForWeather.execute(urlIcon).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!makeAExtraStringForOutPut(dateForSearch + " 12:00:00").isEmpty()) {
            textViewWeatherAfternoon.setText("Температура днём: " + makeAExtraStringForOutPut(dateForSearch + " 12:00:00"));
            try {
                DownloadImageForWeather downloadImageForWeather = new DownloadImageForWeather();
                urlIcon = String.format(IMAGE_URL, getPositionInArrayForIconWeather(dateForSearch + " 12:00:00"));
                imageViewWeatherAfternoon.setImageBitmap(downloadImageForWeather.execute(urlIcon).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!makeAExtraStringForOutPut(dateForSearch + " 18:00:00").isEmpty()) {
            textViewWeatherEvening.setText("Температура вечером: " + makeAExtraStringForOutPut(dateForSearch + " 18:00:00"));
            try {
                DownloadImageForWeather downloadImageForWeather = new DownloadImageForWeather();
                urlIcon = String.format(IMAGE_URL, getPositionInArrayForIconWeather(dateForSearch + " 18:00:00"));
                imageViewWeatherEvening.setImageBitmap(downloadImageForWeather.execute(urlIcon).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!makeAExtraStringForOutPut(dateForSearch + " 21:00:00").isEmpty()) {
            textViewWeatherNight.setText("Температура ночью: " + makeAExtraStringForOutPut(dateForSearch + " 21:00:00"));
            try {
                DownloadImageForWeather downloadImageForWeather = new DownloadImageForWeather();
                urlIcon = String.format(IMAGE_URL, getPositionInArrayForIconWeather(dateForSearch + " 21:00:00"));
                imageViewWeatherNight.setImageBitmap(downloadImageForWeather.execute(urlIcon).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private String getContentForSecondActivity(String dateForSearch) {
        StringBuilder resForSecondActivity = new StringBuilder();

        if (dateForSearch != null) {
            resForSecondActivity.append(String.format("Дата: %s\nПогода в г. %s \nНаселение: %s человек\n" +
                            "Восход: %s \nЗакат: %s\nПродолжительность дня: %s\n\n",
                    dateForOut, cityParameters.get("cityName"), cityParameters.get("population"),
                    cityParameters.get("timeOfSunrise"), cityParameters.get("timeOfSunset"),
                    cityParameters.get("timeDayLength")));
            for (int i = 0; i < timeOfWeather.size(); i++) {
                if (timeOfWeather.get(i).contains(dateForSearch)) {
                    resForSecondActivity.append(String.format( "Температура в %s: %s°C, \nОщущается как: %s°C" +
                                    "\nНа улице %s \nВлажность: %s%%\nВетер: %s, %s м/с\nВидимость: %s м.\n\n",
                            timeOfWeather.get(i).substring(timeOfWeather.get(i).indexOf(" ")), arrayTemp.get(i),
                            arrayTempFeels.get(i), description.get(i), arrayHumidity.get(i),
                            windDirection.get(i), windSpeed.get(i), visibility.get(i)));

                }

            }
        }return resForSecondActivity.toString();
    }
}


//    public void onClickChangeDay(View view) {
//        button = (RadioButton) view;
//        if (button.getId() == R.id.radioButtonToday) {
//            getContent(inputJSONstring, 0);
//        } else if (button.getId() == R.id.radioButtonTommorow) {
//            getContent(inputJSONstring, 1);
//        }
//
//

//    }


//    private Map<String, String> getCity(String inputJSONstring) {
//      Map<String, String> cityParameters = new HashMap<>();
//        if (inputJSONstring == null) {
//            Toast.makeText(getApplicationContext(), "Incorrect City name", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//        try {
//            JSONObject jsonObject = new JSONObject(inputJSONstring);
//            String cityName = jsonObject.getJSONObject("city").getString("name");
//            String population = jsonObject.getJSONObject("city").getString("population");
//            Calendar calender = Calendar.getInstance();
//            long timeZone =jsonObject.getJSONObject("city").getLong("timezone") * 1000;
//            long sunrise = jsonObject.getJSONObject("city").getLong("sunrise") * 1000;
//            long sunset = Long.parseLong(jsonObject.getJSONObject("city").getString("sunset")) * 1000;
//            long dayLength = sunset - sunrise;
//
//            calender.setTimeInMillis(sunrise + timeZone);
//            int hourSunrise = calender.get(Calendar.HOUR_OF_DAY);
//            int minuteSunrise = calender.get(Calendar.MINUTE);
//            String timeOfSunrise = String.format(Locale.getDefault(), "%d:%02d", hourSunrise, minuteSunrise);
//
//
//            calender.setTimeInMillis(sunset + timeZone);
//            int hourSunset = calender.get(Calendar.HOUR_OF_DAY);
//            int minuteSunset = calender.get(Calendar.MINUTE);
//            String timeOfSunset = String.format(Locale.getDefault(), "%d:%02d", hourSunset, minuteSunset);
//            //System.out.println(hourSunset + ":" + minuteSunset);
//            calender.setTimeInMillis(dayLength);
//            String hourDayLength = String.valueOf(calender.get(Calendar.HOUR_OF_DAY));
//            String minuteDayLength = String.valueOf(calender.get(Calendar.MINUTE));
//            String timeDayLength = String.format("%sч %sмин", hourDayLength, minuteDayLength);
//
//            cityParameters.put("cityName", cityName);
//            cityParameters.put("population", population);
//            cityParameters.put("timeOfSunrise", timeOfSunrise);
//            cityParameters.put("timeOfSunset", timeOfSunset);
//            cityParameters.put("timeDayLength", timeDayLength);
//
//            weather = String.format("Погода в г. %s \nНаселение: %s человек\nРассвет: %s \nЗакат: %s \nПродолжительность дня: %s"
//                    , cityName, population, timeOfSunrise, timeOfSunset, timeDayLength);
//
//            textViewWeather.setText(weather);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            System.out.println("incorrect");
//            Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
//        }
//        return null;
//}


//    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            URL url = null;
//            HttpURLConnection urlConnection = null;
//            StringBuilder result = new StringBuilder();
//            try {
//                url = new URL(strings[0]);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = urlConnection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
//                String line = reader.readLine();
//                while (line != null) {
//                    result.append(line);
//                    //System.out.println(line);
//                    line = reader.readLine();
//
//                }
//                System.out.println(result);
//                return result.toString();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//            }
//            return null;
//        }


//        @Override
//        protected void onPostExecute(String s) {
//
//            super.onPostExecute(s);
//            //System.out.println(s);
//            if (s == null) {
//                Toast.makeText(getApplicationContext(), "Incorrect City name", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                String cityName = jsonObject.getJSONObject("city").getString("name");
//                Calendar calender = Calendar.getInstance();
//                long timeZone = Long.parseLong(jsonObject.getJSONObject("city").getString("timezone")) * 1000;
//                long sunrise = Long.parseLong(jsonObject.getJSONObject("city").getString("sunrise")) * 1000;
//                long sunset = Long.parseLong(jsonObject.getJSONObject("city").getString("sunset")) * 1000;
//                long dayLength = sunset - sunrise;
//                calender.setTimeInMillis(sunrise + timeZone);
//                String hourSunrise = String.valueOf(calender.get(Calendar.HOUR_OF_DAY));
//                String minuteSunrise = String.valueOf(calender.get(Calendar.MINUTE));
//                String timeOfSunrise = String.format("%s:%s", hourSunrise, minuteSunrise);
//                //System.out.println( hourSunrise + ":" + minuteSunrise);
//                calender.setTimeInMillis(sunset + timeZone);
//                String hourSunset = String.valueOf(calender.get(Calendar.HOUR_OF_DAY));
//                String minuteSunset = String.valueOf(calender.get(Calendar.MINUTE));
//                String timeOfSunset = String.format("%s:%s", hourSunset, minuteSunset);
//                //System.out.println(hourSunset + ":" + minuteSunset);
//                calender.setTimeInMillis(dayLength);
//                String hourDayLength = String.valueOf(calender.get(Calendar.HOUR_OF_DAY));
//                String minuteDayLength = String.valueOf(calender.get(Calendar.MINUTE));
//                String timeDayLength= String.format("%sч %sмин", hourDayLength, minuteDayLength);
//               // System.out.println(hourDayLength + "ч " + minuteDayLength +"мин");
//                weather = String.format("In city: %s \n\nSunrise: %s \nSunset: %s \nDay Langth: %s"
//                        , cityName, timeOfSunrise, timeOfSunset, timeDayLength);
//
//
//
////                String temp = jsonObject.getJSONObject("main").getString("temp");
////                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
//              //  weather = String.format("%s \n\nТемпература: %s \nНа улице: %s", cityName, temp, description);
//                textViewWeather.setText(weather);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                System.out.println("incorrect");
//                Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
//            }
//        }





