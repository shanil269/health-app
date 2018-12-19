package com.example.muhtadi.fitnessapp;

import android.content.Entity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.Date;

import static com.example.muhtadi.fitnessapp.MainActivity.intentEdit;

public class UserInputActivity extends AppCompatActivity {
    int gender = 2;
    static int GENDER_FEMALE = 1;
    static int GENDER_MALE = 2;
    Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);
        preference = new Preference(this);
        if (getIntent().hasExtra(intentEdit)) {
            if (getIntent().getBooleanExtra(intentEdit, false)) {
                initDataView();
            }
        } else {
            if (preference.getDataStatus()) {
                nextPage();
            } else {
                initView();
            }
        }


    }

    public void initDataView() {
        final TextInputLayout edtHeight = findViewById(R.id.edtHeight);
        final TextInputLayout edtWeight = findViewById(R.id.edtWeight);
        final TextInputLayout edtAge = findViewById(R.id.edtAge);
        Button saveBtn = findViewById(R.id.btnSubmit);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton maleRadioButton = findViewById(R.id.maleButton);
        RadioButton femaleRadioButton = findViewById(R.id.femaleButton);
        if (preference.getGender() == GENDER_MALE) {
            maleRadioButton.setChecked(true);
            femaleRadioButton.setChecked(false);
        } else if (preference.getGender() == GENDER_FEMALE) {
            femaleRadioButton.setChecked(true);
            maleRadioButton.setChecked(false);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.maleButton) {
                    gender = GENDER_MALE;
                } else if (checkedId == R.id.femaleButton) {
                    gender = GENDER_FEMALE;
                }
            }
        });
        edtHeight.getEditText().setText(preference.getHeight() + "");
        edtWeight.getEditText().setText(preference.getWeight() + "");
        edtAge.getEditText().setText(preference.getAge() + "");
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtHeight.getEditText().getText().equals("")) {
                    edtHeight.setError("Please enter your height correctly");
                } else if (edtWeight.getEditText().getText().equals("")) {
                    edtWeight.setError("Please enter your weight correctly");
                } else if (edtAge.getEditText().getText().equals("")) {
                    edtAge.setError("Please enter your age correctly");
                } else {
                    preference.setAge(Float.parseFloat(edtAge.getEditText().getText().toString()));
                    preference.setHeight(Float.parseFloat(edtHeight.getEditText().getText().toString()));
                    preference.setWeight(Float.parseFloat(edtWeight.getEditText().getText().toString()));
                    preference.setGender(gender);
                    preference.setDataStatus(true);
                    nextPage();
                }
            }
        });
    }

    public void initView() {
        final TextInputLayout edtHeight = findViewById(R.id.edtHeight);
        final TextInputLayout edtWeight = findViewById(R.id.edtWeight);
        final TextInputLayout edtAge = findViewById(R.id.edtAge);
        Button saveBtn = findViewById(R.id.btnSubmit);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.maleButton) {
                    gender = GENDER_MALE;
                } else if (checkedId == R.id.femaleButton) {
                    gender = GENDER_FEMALE;
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtHeight.getEditText().getText().equals("")) {
                    edtHeight.setError("Please enter your height correctly");
                } else if (edtWeight.getEditText().getText().equals("")) {
                    edtWeight.setError("Please enter your weight correctly");
                } else if (edtAge.getEditText().getText().equals("")) {
                    edtAge.setError("Please enter your age correctly");
                } else {
                    preference.setAge(Float.parseFloat(edtAge.getEditText().getText().toString()));
                    preference.setHeight(Float.parseFloat(edtHeight.getEditText().getText().toString()));
                    preference.setWeight(Float.parseFloat(edtWeight.getEditText().getText().toString()));
                    preference.setGender(gender);
                    preference.setDataStatus(true);
                    nextPage();
                }
            }
        });
    }

    public void nextPage() {
        startActivity(new Intent(UserInputActivity.this, MainActivity.class));
        finish();
    }
}
