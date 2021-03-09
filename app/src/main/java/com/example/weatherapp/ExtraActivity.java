package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ExtraActivity extends AppCompatActivity {
    private TextView textView;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);
        textView = findViewById(R.id.textViewExtra);
        Intent intent =getIntent();
        result = intent.getStringExtra("result");
        textView.setText(result);
    }
}