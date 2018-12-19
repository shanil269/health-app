package com.example.muhtadi.fitnessapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        final Calendar cal = Calendar.getInstance();
        TextView txtNumResult = findViewById(R.id.txtResultNum);
        TextView txtResult = findViewById(R.id.txtResult);
        int bpm = getIntent().getIntExtra("Value", 0);
        try {
            txtNumResult.setText(bpm + " BPM");
            if (bpm < 60) {
                txtResult.setText("Go For A Walk");
                txtResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                txtNumResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else if (bpm >= 60 && bpm <= 120) {
                txtResult.setText("Normal! You're Healthy");
                txtResult.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                txtNumResult.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            } else if (bpm > 120) {
                txtResult.setText("Risky! Emergency Medication Needed");
                txtResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                txtNumResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                txtResult.setText("Something Went Wrong");
            }
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("history.txt", Context.MODE_APPEND));
                outputStreamWriter.write(bpm + " " + "Relaxed" + " " + dateFormat.format(cal.getTime()) + " " + timeFormat.format(cal.getTime()) + "\n");
                outputStreamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e) {
            txtNumResult.setText("--");
            txtResult.setText("Something Went Wrong");
        }
        Button btnBPMhisotry = (Button) findViewById(R.id.btn_bpm_history);
        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBPMhisotry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, Main2Activity.class));
            }
        });
    }
}
