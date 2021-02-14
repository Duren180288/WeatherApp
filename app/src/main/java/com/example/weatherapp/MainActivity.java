package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?q=Moscow&appid=bbc8c0f7cacf86d2b827d6167514dc9a&lang=ru&units=metric";
    private EditText editTextCity;
    private TextView textViewWeather;
    private String weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextCity = findViewById(R.id.editTextTextCity);
        textViewWeather = findViewById(R.id.textViewWeather);

        DownloadWeatherTask task = new DownloadWeatherTask();
        String url = String.format(WEATHER_URL, "Saint Petersburg");
        task.execute(url);

    }

    public void onClickShowWeather(View view) {
        String city = editTextCity.getText().toString().trim();
        try{
        if (!city.isEmpty()){
            DownloadWeatherTask task = new DownloadWeatherTask();
            String url = String.format(WEATHER_URL, city);
            task.execute(url);
        }else {
            Toast.makeText(this, "Empty input field", Toast.LENGTH_SHORT).show();
        }
        }catch (Exception e){
        }

    }



    private  class DownloadWeatherTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null){
                    result.append(line);
                    System.out.println(line);
                    line = reader.readLine();
                    System.out.println(line);
                }

                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;


            } catch (IOException e) {
                e.printStackTrace();
                return null;

            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
            }
           // return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //System.out.println(s);
            if (s == null){
                Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                String cityName = jsonObject.getString("name");
                String temp =jsonObject.getJSONObject("main").getString("temp");
                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                weather = String.format("%s \n\nТемпература: %s \nНа улице: %s", cityName, temp, description);
                textViewWeather.setText(weather);
            } catch (JSONException e) {
                e.printStackTrace();
               // Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
            }
        }


    }


}