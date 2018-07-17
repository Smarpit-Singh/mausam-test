package com.example.android.mausam.data;

import android.content.Context;

import com.example.android.mausam.data.WeatherContract.WeatherEntry;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDBHelpet extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    public WeatherDBHelpet(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public WeatherDBHelpet(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + WeatherEntry.TABLE_NAME + " ( " +
                        WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WeatherEntry.COLUMN_DATE + " INTEGER, " +
                        WeatherEntry.COLUMN_WEATHER_ID + " INTEGER, " +
                        WeatherEntry.COLUMN_MIN_TEMP + " REAL, " +
                        WeatherEntry.COLUMN_MAX_TEMP + " REAL, " +
                        WeatherEntry.COLUMN_HUMIDITY + " REAL, " +
                        WeatherEntry.COLUMN_PRESSURE + " REAL, " +
                        WeatherEntry.COLUMN_WIND_SPEED + " REAL, " +
                        WeatherEntry.COLUMN_DEGREES + " REAL);";

        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
