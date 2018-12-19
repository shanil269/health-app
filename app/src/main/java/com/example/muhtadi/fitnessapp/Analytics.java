package com.example.muhtadi.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Analytics extends AppCompatActivity {

    public String getGroup = "";
    public static List<String> list = new ArrayList<String>();
    public static List<String> groupList = new ArrayList<String>();
    public static List<Double> valueList = new ArrayList<Double>();
    public static List<String> dateList = new ArrayList<String>();
    public static List<String> timeList = new ArrayList<String>();
    public Double val = Double.valueOf(0);
    public static String dateString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Walking Companion");
        final ListView listView = (ListView)findViewById(R.id.list_view);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Analytics.this, R.layout.list_item, list);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        val = Double.parseDouble(getIntent().getStringExtra("value"));
        getGroup = getIntent().getStringExtra("group");
        valueList.add(val);
        groupList.add(getGroup);
        String dateString = dateFormat.format(cal.getTime());
        String timeString = timeFormat.format(cal.getTime());
        dateList.add(dateString);
        timeList.add(timeString);

        if(getGroup.equals("")){
            list.clear();
            groupList.clear();
            valueList.clear();
            dateList.clear();
            timeList.clear();

            try{
                InputStream inputStream = openFileInput("history.txt");

                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String line = "";
                    list.add(" "+decideGroup("Relaxed")+"Results"+"                          "+"Dates");
                    while((line = bufferedReader.readLine())!= null){
                        String words[] = line.split(" ");
                        valueList.add(Double.valueOf(words[0]));
                        groupList.add(words[1]);
                        dateList.add(words[2]);
                        timeList.add(words[3]);
                        list.add(" "+decideGroup(words[1])+"\n"+words[0]+" BPM                        "+words[2]+"\n");
                    }

                    inputStream.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
            listView.setAdapter(adapter);
        }
        else{
            String str = "";
            str = " "+decideGroup(getGroup)+"\n"+val+" BPM                        "+dateString+"\n";
            list.add(str);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Analytics.this, "Reading taken on:\n"+timeList.get(position), Toast.LENGTH_SHORT).show();
                Intent in = new Intent(Analytics.this, MainActivity.class);
                in.putExtra("actualData", valueList.get(position).toString());
                in.putExtra("group", groupList.get(position));
                startActivity(in);
            }
        });



        /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                list.remove(position);
                listView.setAdapter(adapter);
                return true;
            }
        });*/

    }

    public String decideGroup(String str){
        if(str.equals("Exercised?")){
//            str = "Range: (50-70)BPM";
            str = "";
        }
        else{
//            str = "Range: (65-80)BPM";
            str = "";
        }
        return str;
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
