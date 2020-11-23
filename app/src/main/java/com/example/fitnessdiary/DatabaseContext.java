package com.example.fitnessdiary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import androidx.appcompat.app.AppCompatActivity;

import static android.os.Build.ID;

public abstract class DatabaseContext extends AppCompatActivity {
    protected void initDatabase() throws SQLException {

        SQLiteDatabase databaseFitnessDiary = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/fitnessDiary.db",
                null
        );

        String createQuery = "CREATE TABLE if not exists EXERCISETABLE( " +
                "ID integer PRIMARY KEY AUTOINCREMENT, " +
                "DATE text not null, " +
                "EXERCISE text not null, " +
                "SERIES integer not null, " +
                "REPS integer not null, " +
                "WEIGHT real not null" +
                "); ";

        databaseFitnessDiary.execSQL(createQuery);
        databaseFitnessDiary.close();
    }

    protected  void ExecuteSQL(String SQL, Object[] args, OnQuerySuccess successMessage) throws SQLException {
        SQLiteDatabase databaseFitnessDiary = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/fitnessDiary.db",
                null
        );

        databaseFitnessDiary.execSQL(SQL, args);
        successMessage.successQuery();
        databaseFitnessDiary.close();
    }

    public void SelectSqlQuery(String SQL, String[] args, SelectElement iterate) throws Exception{
        SQLiteDatabase databaseFitnessDiary = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/fitnessDiary.db",
                null
        );

        Cursor cursor = databaseFitnessDiary.rawQuery(SQL, args);
        while (cursor.moveToNext()){
            String ID = cursor.getString(cursor.getColumnIndex("ID"));
            String DATE = cursor.getString(cursor.getColumnIndex("DATE"));
            String EXERCISE = cursor.getString(cursor.getColumnIndex("EXERCISE"));
            String SERIES = cursor.getString(cursor.getColumnIndex("SERIES"));
            String REPS = cursor.getString(cursor.getColumnIndex("REPS"));
            String WEIGHT = cursor.getString(cursor.getColumnIndex("WEIGHT"));
            iterate.ElementIterate(ID, DATE, EXERCISE, SERIES, REPS, WEIGHT);
        }
        databaseFitnessDiary.close();
    }

    protected interface OnQuerySuccess{
        public void successQuery();
    }

    protected interface SelectElement{
        public void ElementIterate(String ID, String Date, String Exercise, String Series, String Reps, String Weight);
    }
}
