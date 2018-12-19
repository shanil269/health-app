package com.example.muhtadi.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    public static int summation;
    public static int average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Walking Companion");
        final List<Entry> graphValues = new ArrayList<>();
        final List<BarEntry> graphValuesBar = new ArrayList<>();
        final List<String> timeValues = new ArrayList<>();
        final List<String> dateValues = new ArrayList<>();

        try{
            InputStream inputStream = openFileInput("history.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                int i = 0;
                summation = 0;
                while((line = bufferedReader.readLine())!= null){
                    String words[] = line.split(" ");
                    summation = summation + Integer.parseInt(words[0]);
                    graphValues.add(new Entry(Float.parseFloat(words[0]),i));
                    graphValuesBar.add(new BarEntry(Float.parseFloat(words[0]), i));
                    timeValues.add(words[3]);
                    dateValues.add(words[2]);
                    i++;
                }
                average = summation/i;
                inputStream.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        PieDataSet pieDataSet = new PieDataSet(graphValues, " ");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextSize(0f);
        pieDataSet.setHighlightEnabled(false);
        PieData data = new PieData(timeValues, pieDataSet);

        PieChart pieChart = (PieChart)findViewById(R.id.chart);

        pieChart.setData(data);

        pieChart.animateY(1000);
        pieChart.animateY(500);
        pieChart.setCenterText(String.valueOf(average));
        pieChart.setCenterTextSize(70f);
        pieChart.setHoleRadius(80f);
        pieChart.invalidate();

        BarDataSet barDataSet = new BarDataSet(graphValuesBar, "Pulse");

        barDataSet.setColor(getResources().getColor(android.R.color.holo_red_dark));
        BarData barData = new BarData(timeValues, barDataSet);
        BarChart barChart = (BarChart)findViewById(R.id.barGraphHome);
        barChart.setData(barData);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.animateY(800);

        Button listButton = (Button)findViewById(R.id.button9);

        listButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent in = new Intent(Main2Activity.this,Analytics.class);
                in.putExtra("value", Double.valueOf(0).toString());
                in.putExtra("group", "");
                startActivity(in);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}


