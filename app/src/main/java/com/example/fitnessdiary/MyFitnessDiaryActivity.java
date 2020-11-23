package com.example.fitnessdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyFitnessDiaryActivity extends DatabaseContext implements PopupMenu.OnMenuItemClickListener {

    protected Button backButton;
    protected ListView diaryListView;
    protected String identity, date, exercise, series, reps, weight;

    protected void DeleteItem(){
        try{
            ExecuteSQL(
                    "DELETE FROM EXERCISETABLE WHERE ID = ?",
                    new Object[]{
                            identity
                    },
                    new OnQuerySuccess() {
                        @Override
                        public void successQuery() {
                            Toast.makeText(getApplicationContext(),
                                    "Deleted successfully!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteItem:
                DeleteItem();
                try {
                    FillListView();
                } catch (Exception e) {
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                    e.printStackTrace();
                }

                return true;
            case R.id.editItem:
                Intent intent = new Intent(MyFitnessDiaryActivity.this, UpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", identity);
                bundle.putString("Date", date);
                bundle.putString("Exercise", exercise);
                bundle.putString("Series", series);
                bundle.putString("Reps", reps);
                bundle.putString("Weight", weight);
                intent.putExtras(bundle);
                startActivityForResult(intent, 200, bundle);
                return true;
            default:
                return false;
        }
    }

    protected void FillListView() throws Exception{
        ArrayList<String> diaryInfo = new ArrayList<>();
        SelectSqlQuery(
                "SELECT * FROM EXERCISETABLE;",
                null,
                new SelectElement() {
                    @Override
                    public void ElementIterate(String ID, String DATE, String EXERCISE, String SERIES, String REPS, String WEIGHT) {
                        diaryInfo.add(ID + ":" + "Date:" + DATE + ":" + EXERCISE + " Series: " + SERIES + " Reps: " + REPS + " Weight: " + WEIGHT);

                    }
                }
        );

        diaryListView.clearChoices();
        ArrayAdapter<String> fillDiaryViewAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_list_view_design,
                R.id.textViewForListView,
                diaryInfo
        );
        diaryListView.setAdapter(fillDiaryViewAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fitness_diary);
        backButton = findViewById(R.id.btnBack);
        diaryListView = findViewById(R.id.listViewDiary);

        try {
            FillListView();
        } catch (Exception e) {
            Toast.makeText(
                    getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
            e.printStackTrace();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyFitnessDiaryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ///popups on listview
        diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popup = new PopupMenu(MyFitnessDiaryActivity.this, diaryListView);
                popup.setOnMenuItemClickListener(MyFitnessDiaryActivity.this::onMenuItemClick);
                popup.inflate(R.menu.popup_menu);
                popup.show();

                TextView selectedRow = view.findViewById(R.id.textViewForListView);
                String selectedText = selectedRow.getText().toString();
                String[] element = selectedText.split("[: ]+");


                String ID = element[0];
                String Date = element[2];
                String Exercise = element[3];
                String Series = element[5];
                String Reps = element[7];
                String Weight = element[9];

                identity = ID;
                date = Date;
                exercise = Exercise;
                series = Series;
                reps = Reps;
                weight = Weight;

            }
        });
        /////////////////
    }


}