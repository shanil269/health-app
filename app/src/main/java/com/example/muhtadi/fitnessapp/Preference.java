package com.example.muhtadi.fitnessapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Date;

public class Preference {
    public static final String STATUS = "pref_status";
    public static final String HEIGHT = "pref_height";
    public static final String WEIGHT = "pref_weight";
    public static final String AGE = "pref_age";
    public static final String GENDER = "pref_gender";


    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public Preference(Context context) {
        sharedPreferences = context.getSharedPreferences("323_preference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void setDataStatus(boolean status) {
        editor.putBoolean(STATUS, status);
        editor.apply();
        editor.commit();
    }

    public void setHeight(float height) {
        editor.putFloat(HEIGHT, height);
        editor.apply();
        editor.commit();
    }

    public void setWeight(float weight) {
        editor.putFloat(WEIGHT, weight);
        editor.apply();
        editor.commit();
    }

    public void setAge(float age) {
        editor.putFloat(AGE, age);
        editor.apply();
        editor.commit();
    }


    public void setGender(int gender) {
        editor.putInt(GENDER, gender);
        editor.apply();
        editor.commit();
    }


    public float getHeight() {
        return sharedPreferences.getFloat(HEIGHT, 0.0f);
    }

    public float getWeight() {
        return sharedPreferences.getFloat(WEIGHT, 0.0f);
    }

    public boolean getDataStatus() {
        return sharedPreferences.getBoolean(STATUS, false);
    }

    public float getAge() {
        return sharedPreferences.getFloat(AGE, 0.0f);
    }

    public int getGender() {
        return sharedPreferences.getInt(GENDER, 0);
    }


}
