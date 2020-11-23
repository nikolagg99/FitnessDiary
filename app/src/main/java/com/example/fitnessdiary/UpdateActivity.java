package com.example.fitnessdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateActivity extends DatabaseContext {

    protected String ID, Date, exercise, series, reps, weight;

    Calendar date = Calendar.getInstance();
    int year = date.get(Calendar.YEAR);
    int month = date.get(Calendar.MONTH);
    int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);

    String dateForAdding = "";

    protected Button selectDateButton;
    protected EditText exerciseEditText, seriesEditText, repsEditText, weightEditText;
    protected Button updateExercisesButton, btnBackUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        selectDateButton = findViewById(R.id.btnSelectUpdatedDate);
        updateExercisesButton = findViewById(R.id.btnUpdateExercise);
        btnBackUpdate = findViewById(R.id.btnBackUpdate);

        exerciseEditText = findViewById(R.id.textViewForUpdateExercise);
        seriesEditText = findViewById(R.id.textViewForUpdateSeries);
        repsEditText = findViewById(R.id.textViewForUpdateReps);
        weightEditText = findViewById(R.id.textViewForUpdateWeight);

        Bundle bundle = getIntent().getExtras();
        dateForAdding = bundle.getString("Date");
        if(bundle!=null){
            ID = bundle.getString("ID");
            selectDateButton.setText(bundle.getString("Date"));
            exerciseEditText.setText(bundle.getString("Exercise"));
            seriesEditText.setText(bundle.getString("Series"));
            repsEditText.setText(bundle.getString("Reps"));
            weightEditText.setText(bundle.getString("Weight"));
        }


        //getting date from date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
                date.set(year, month, dayOfMonth);
                String dateString = simpleFormat.format(date.getTime());
                dateForAdding = dateString;
                selectDateButton.setText(dateForAdding);
            }
        },year,month,dayOfMonth);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();

            }
        });

/////////////////////////////

        updateExercisesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ExecuteSQL(
                            "UPDATE EXERCISETABLE SET DATE=?, EXERCISE=?, SERIES=?, REPS=?, WEIGHT=? WHERE ID=?",
                            new Object[]{
                                    dateForAdding,
                                    exerciseEditText.getText().toString(),
                                    seriesEditText.getText().toString(),
                                    repsEditText.getText().toString(),
                                    weightEditText.getText().toString(),
                                    ID
                            },
                            new OnQuerySuccess() {
                                @Override
                                public void successQuery() {
                                    Toast.makeText(getApplicationContext(),"Record updated", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);

                    e.printStackTrace();
                }

                finishActivity(200);
                startActivity(new Intent(UpdateActivity.this, MyFitnessDiaryActivity.class));

            }
        });
        ///////////////

        btnBackUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity(200);
                startActivity(new Intent(UpdateActivity.this, MyFitnessDiaryActivity.class));
            }
        });

    }
}