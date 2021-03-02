package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=bbc8c0f7cacf86d2b827d6167514dc9a&lang=ru&units=metric";
    // private final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?q=moscow&appid=bbc8c0f7cacf86d2b827d6167514dc9a&lang=ru&units=metric";
    private String url;
    private EditText editTextCity;
    private TextView textViewWeather;
    private TextView textViewWeatherExtra;
    private String showWeather;
    private String showWeatherExtra;
    private String city;
    private String inputJSONstring;
    private ArrayList<Integer> arrayTempNow;
    private ArrayList<Integer> arrayTempFeelsNow;
    private ArrayList<Integer> arrayHumidity;
    private ArrayList<String> mainForWeatherImage;  //clouds, rain , snow...
    private ArrayList<String> description;
    private ArrayList<String> windSpeed;
    private ArrayList<String> windDirection;
    private ArrayList<String> visibility;
    private ArrayList<String> timeOfWeather;
    private Button buttonExtra;
    private Switch switchExtra;
    private int positionRadioButton;
    private RadioGroup radioGroup;
    private RadioButton buttonToday;
    private RadioButton buttonTommorow;
    private RadioButton buttonTommorow2;
    private RadioButton buttonTommorow3;
    private RadioButton buttonTommorow4;
    private String dayOfWeek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextCity = findViewById(R.id.editTextTextCity);
        textViewWeather = findViewById(R.id.textViewWeather);
        textViewWeatherExtra = findViewById(R.id.textViewWeatherExtra);
        buttonToday = findViewById(R.id.radioButtonToday);
        buttonTommorow = findViewById(R.id.radioButtonTommorow);
        buttonTommorow2 = findViewById(R.id.radioButtonTommorow2);
        buttonTommorow3 = findViewById(R.id.radioButtonTommorow3);
        buttonTommorow4 = findViewById(R.id.radioButtonTommorow4);
        radioGroup = findViewById(R.id.radioGroup);


        //    String url = String.format(WEATHER_URL, "Saint Petersburg");
        try {
            DownloadWeatherTask task = new DownloadWeatherTask();
            url = String.format(WEATHER_URL, "Moscow");
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
                if (textViewWeatherExtra.getVisibility() == View.VISIBLE) {
                    textViewWeatherExtra.setVisibility(View.INVISIBLE);
                } else {
                    textViewWeatherExtra.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    public void onClickShowWeather(View view) {
        city = editTextCity.getText().toString().trim();

        try {
            if (!city.isEmpty()) {
                DownloadWeatherTask task = new DownloadWeatherTask();
                url = String.format(WEATHER_URL, city);
                inputJSONstring = task.execute(url).get();
                buttonToday.setChecked(true);

                getContent(inputJSONstring, positionRadioButton);

            } else {
                Toast.makeText(this, "Empty input field", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }

    }

    private void getContent(String inputJSONstring, int positionRadioButton) {
        if (inputJSONstring == null) {
            Toast.makeText(getApplicationContext(), "Incorrect City name or not internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        CityDescription cityDescription = new CityDescription();
        Map<String, String> cityParameters = cityDescription.getCity(inputJSONstring);

        Weather weather = new Weather();
        arrayTempNow = weather.getTempHumid(inputJSONstring, "temp");
        arrayTempFeelsNow = weather.getTempHumid(inputJSONstring, "feels_like");
        arrayHumidity = weather.getTempHumid(inputJSONstring, "humidity");
        mainForWeatherImage = weather.getDescripVisibilTime(inputJSONstring, "main");
        description = weather.getDescripVisibilTime(inputJSONstring, "description");

        windSpeed = weather.getDescripVisibilTime(inputJSONstring, "speed");
        windDirection = weather.getDescripVisibilTime(inputJSONstring, "deg");
        visibility = weather.getDescripVisibilTime(inputJSONstring, "visibility");
        timeOfWeather = weather.getDescripVisibilTime(inputJSONstring, "dt_txt");

//        System.out.println(arrayTempNow);
//        System.out.println(arrayTempFeelsNow);

        TimeOfObservation timeOfObservation = new TimeOfObservation();
        String resultFromTimeOfObservation = timeOfObservation.getDateForView(timeOfWeather.get(0), 0);
        String dateForOut = resultFromTimeOfObservation.substring(0, resultFromTimeOfObservation.indexOf("&"));
        String dateForSearch = resultFromTimeOfObservation.substring(resultFromTimeOfObservation.indexOf("&") + 1);
        System.out.println(dateForOut);
        System.out.println(dateForSearch);


        showWeather = String.format("Дата: %s\nПогода в г. %s \nНаселение: %s человек " +
                        "\n\nТемпература: %s\nОщущается как: %s \nВлажность: %s%% \n",
                dateForOut,
                cityParameters.get("cityName"), cityParameters.get("population"),
                arrayTempNow.get(0), arrayTempFeelsNow.get(0), arrayHumidity.get(0));

        showWeatherExtra = String.format("Рассвет: %s \nЗакат: %s \nПродолжительность дня: %s ",
                cityParameters.get("timeOfSunrise"),
                cityParameters.get("timeOfSunset"), cityParameters.get("timeDayLength"));

        if (positionRadioButton == 0) {
            textViewWeather.setText(showWeather);

            textViewWeatherExtra.setText(showWeatherExtra);
        } else if (positionRadioButton == 1) {
            textViewWeather.setText("Hello!");
            textViewWeatherExtra.setText("Motherfucker!");
        } else if (positionRadioButton == 2) {


            textViewWeather.setText("Hello2!");
            textViewWeatherExtra.setText("Motherfucker!");
        }

    }

    private String makeAStringForOutPut(String dateForSearch) {
        String finalString = "";


        return finalString;
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





