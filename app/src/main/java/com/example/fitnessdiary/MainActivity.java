package com.example.fitnessdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends DatabaseContext {

    protected Button selectDateButton;
    protected  Button addExerciseButton;
    protected Button historyOfExercisesButton;
    protected EditText exerciseEditText, seriesEditText, repsEditText, weightEditText;
    protected ListView fitnessDiaryListView;

    Calendar date = Calendar.getInstance();
    int year = date.get(Calendar.YEAR);
    int month = date.get(Calendar.MONTH);
    int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);

    String dateForAdding = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectDateButton = findViewById(R.id.btnSelectDate);
        addExerciseButton = findViewById(R.id.btnAddExercise);
        historyOfExercisesButton = findViewById(R.id.btnHistoryOfExercises);

        exerciseEditText = findViewById(R.id.textViewForExercise);
        seriesEditText = findViewById(R.id.textViewForSeries);
        repsEditText = findViewById(R.id.textViewForReps);
        weightEditText = findViewById(R.id.textViewForWeight);

        fitnessDiaryListView = findViewById(R.id.listViewDiary);


//getting date from date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        /////initialize database
        try {
            initDatabase();
        } catch (Exception e) {
            Toast.makeText(
                    getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    ExecuteSQL("INSERT INTO EXERCISETABLE(DATE, EXERCISE, SERIES, REPS, WEIGHT)" +
                                    "VALUES(?, ?, ?, ?, ?)",
                            new Object[]{
                                    dateForAdding,
                                    exerciseEditText.getText().toString(),
                                    seriesEditText.getText().toString(),
                                    repsEditText.getText().toString(),
                                    weightEditText.getText().toString()
                            },
                            new OnQuerySuccess() {
                                @Override
                                public void successQuery() {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Successfully inserted",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            });

                }catch (Exception e){
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
                selectDateButton.setText("Open Diary");
            }
        });

        /////////////////////

        ///-----open diary-------///
        historyOfExercisesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyFitnessDiaryActivity.class);
                startActivity(intent);
            }
        });
        ///
    }
}