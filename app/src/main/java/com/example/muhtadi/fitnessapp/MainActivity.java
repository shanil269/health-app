package com.example.muhtadi.fitnessapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


import com.example.muhtadi.fitnessapp.R;
import com.example.muhtadi.fitnessapp.StepDetector;
import com.example.muhtadi.fitnessapp.StepListener;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener {
    private TextView TvSteps;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    public static String intentEdit = "EDT_INTENT";
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    FloatingActionButton fab;
    static int GENDER_FEMALE = 1;
    static int GENDER_MALE = 2;
    static float MALE_STRIDE_LENGTH_METER = 0.762f;
    static float FEMALE_STRIDE_LENGTH_METER = 0.671f;
    private Chronometer chronometer;
    private boolean isStart;
    Preference preference;
    String kcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle("Walking Companion");
        }
        preference = new Preference(this);
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = (TextView) findViewById(R.id.tv_steps);
        TvSteps.setText(TEXT_NUM_STEPS + 0);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                chronometer = chronometerChanged;
            }
        });
        Button btnBPMhisotry = (Button) findViewById(R.id.btn_bpm_history);
        final Button BtnStart = (Button) findViewById(R.id.btn_start);
        final Button BtnStop = (Button) findViewById(R.id.btn_stop);
        final Button btn_edit_info = (Button) findViewById(R.id.btn_edit_info);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HeartRateMonitor.class));
            }
        });

        btnBPMhisotry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //stop watch
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                isStart = true;
                //steps
                numSteps = 0;
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                BtnStart.setVisibility(View.GONE);
                BtnStop.setVisibility(View.VISIBLE);
            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //stop watch
                chronometer.stop();
                isStart = false;
                //steps
                sensorManager.unregisterListener(MainActivity.this);
                int elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
                int elapsedSeconds = elapsedMillis / 1000;
                if (preference.getGender() == GENDER_MALE) {
                    kcal = String.format("%.2f", calculateEnergyExpenditure(preference.getHeight(), preference.getAge(), preference.getWeight(),
                            preference.getGender(), elapsedSeconds, numSteps, MALE_STRIDE_LENGTH_METER));
                    globalAlert("You've Walked " + numSteps + " steps in " + getDurationString(elapsedSeconds) + " and you've" +
                            " burnt " + kcal + " KCal");
                } else if (preference.getGender() == GENDER_FEMALE) {
                    kcal = String.format("%.2f", calculateEnergyExpenditure(preference.getHeight(), preference.getAge(), preference.getWeight(),
                            preference.getGender(), elapsedSeconds, numSteps, FEMALE_STRIDE_LENGTH_METER));
                    globalAlert("You've Walked " + numSteps + " steps in " + getDurationString(elapsedSeconds) + " and you've" +
                            " burnt " + kcal + " KCal");
                }
                BtnStart.setVisibility(View.VISIBLE);
                BtnStop.setVisibility(View.GONE);
                TvSteps.setText(TEXT_NUM_STEPS + 0);
                chronometer.setBase(SystemClock.elapsedRealtime());

            }
        });
        btn_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserInputActivity.class);
                i.putExtra(intentEdit, true);
                startActivity(i);
                finish();
            }
        });

    }

    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        String txt_hours = " hour ";
        String txt_mins = " minute ";
        String txt_seconds = " second ";
        if (hours > 1) {
            txt_hours = " hours ";
        }
        if (minutes > 1) {
            txt_mins = " minutes ";
        }
        if (seconds > 1) {
            txt_seconds = " seconds ";
        }
        if (hours > 0) {
            return hours + txt_hours + minutes + txt_mins + seconds + txt_seconds;
        } else if (minutes > 0) {
            return minutes + txt_mins + seconds + txt_seconds;
        } else {
            return seconds + txt_seconds;
        }

    }

    public AlertDialog globalAlert(String text) {
        //Toast.makeText(context,text,Toast.LENGTH_LONG).show();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Walking Companion");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        return alertDialog;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }


    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);


    }


    public static float calculateEnergyExpenditure(float height, float age, float weight, int gender,
                                                   int durationInSeconds, int stepsTaken, float strideLengthInMetres) {


        float harrisBenedictRmR = convertKilocaloriesToMlKmin(harrisBenedictRmr(gender, weight, age, height), weight);

        float kmTravelled = calculateDistanceTravelledInKM(stepsTaken, strideLengthInMetres);
        float hours = durationInSeconds / 3600;
        float speedInMph = (kmTravelled * (0.621f)) / hours;
        float metValue = getMetForActivity(speedInMph);

        float constant = 3.5f;

        float correctedMets = metValue * (constant / harrisBenedictRmR);
        return correctedMets * hours * weight;
    }

    public static float convertKilocaloriesToMlKmin(float kilocalories, float weightKgs) {
        float kcalMin = kilocalories / 1440;
        kcalMin /= 5;

        return ((kcalMin / (weightKgs)) * 1000);
    }

    public static float convertMetresToCentimetre(float metres) {
        return metres * 100;
    }

    public static float calculateDistanceTravelledInKM(int stepsTaken, float entityStrideLength) {
        return (((float) stepsTaken * entityStrideLength) / 1000);
    }

    /**
     * Gets the MET value for an activity. Based on https://sites.google.com/site/compendiumofphysicalactivities/Activity-Categories/walking .
     *
     * @param speedInMph The speed in miles per hour
     * @return The met value.
     */
    private static float getMetForActivity(float speedInMph) {
        if (speedInMph < 2.0) {
            return 2.0f;
        } else if (Float.compare(speedInMph, 2.0f) == 0) {
            return 2.8f;
        } else if (Float.compare(speedInMph, 2.0f) > 0 && Float.compare(speedInMph, 2.7f) <= 0) {
            return 3.0f;
        } else if (Float.compare(speedInMph, 2.8f) > 0 && Float.compare(speedInMph, 3.3f) <= 0) {
            return 3.5f;
        } else if (Float.compare(speedInMph, 3.4f) > 0 && Float.compare(speedInMph, 3.5f) <= 0) {
            return 4.3f;
        } else if (Float.compare(speedInMph, 3.5f) > 0 && Float.compare(speedInMph, 4.0f) <= 0) {
            return 5.0f;
        } else if (Float.compare(speedInMph, 4.0f) > 0 && Float.compare(speedInMph, 4.5f) <= 0) {
            return 7.0f;
        } else if (Float.compare(speedInMph, 4.5f) > 0 && Float.compare(speedInMph, 5.0f) <= 0) {
            return 8.3f;
        } else if (Float.compare(speedInMph, 5.0f) > 0) {
            return 9.8f;
        }
        return 0;
    }

    /**
     * Calculates the Harris Benedict RMR value for an entity. Based on above calculation for Com
     *
     * @param gender   Users gender.
     * @param weightKg Weight in Kg.
     * @param age      Age in years.
     * @param heightCm Height in CM.
     * @return Harris benedictRMR value.
     */
    private static float harrisBenedictRmr(int gender, float weightKg, float age, float heightCm) {
        if (gender == GENDER_FEMALE) {
            return 655.0955f + (1.8496f * heightCm) + (9.5634f * weightKg) - (4.6756f * age);
        } else {
            return 66.4730f + (5.0033f * heightCm) + (13.7516f * weightKg) - (6.7550f * age);
        }

    }


}