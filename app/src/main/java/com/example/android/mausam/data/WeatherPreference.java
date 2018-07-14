package com.example.android.mausam.data;

import android.content.Context;

public class WeatherPreference {



    public static final String PREF_CITY_NAME = "city_name";

    public static final String PREF_COORD_LAT = "coord_lat";
    public static final String PREF_COORD_LONG = "coord_long";

    private static final String DEFAULT_WEATHER_LOCATION = "94043,USA";
    private static final double[] DEFAULT_WEATHER_COORDINATES = {37.4284, 122.0724};

    private static final String DEFAULT_MAP_LOCATION =
            "1600 Amphitheatre Parkway, Mountain View, CA 94043";




    public static void setLocationSetting(Context context, String locationSettig, double lat, double lon){

    }

    public static void setLocationDetails(Context context, String cityName, double lat, double lon){

    }

    public static void resetLocationCoord(Context context){

    }

    public static String getPreferedWatherLocation(Context context){

        return getDefaultWeatherLocation();
    }


    public static double[] getLocationCoordinates(Context context){

        return getDefaultWeatherCoordinates();
    }

    public static boolean isMetric(Context context){

        return false;
    }

    public static boolean isLatLongAvailable(Context context){

        return false;
    }

    public static String getDefaultWeatherLocation(){

        return DEFAULT_WEATHER_LOCATION;
    }



    public static double[] getDefaultWeatherCoordinates(){

        return DEFAULT_WEATHER_COORDINATES;
    }
}
