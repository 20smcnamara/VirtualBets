package com.example.virtualbets;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

public class Constants {


    //public Context CURRENT_CONTEXT;
    public static long INIT_TIME;

    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;

    public static int width;
    public static int height;

    //Conversions
    public static double NanoToBase = 1000000000.0;
    public static double NanoToMilli = 1000000.0;

    public static boolean started = false;

    public static void setUp(SharedPreferences sharedPrefs, int w, int h){
        sharedPref = sharedPrefs;
        editor = sharedPref.edit();
        width = w;
        height = h;
        started = true;
    }
}
