package com.example.virtualbets;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.widget.EditText;

public class Constants {

    public static boolean started = false;

    public static int width;
    public static int height;

    public static int START_SCENE = 0;
    public static int LOGIN_SCENE = 1;
    public static int REGISTER_SCENE = 2;
    public static int HOME_SCENE = 3;

    public static Keyboard myKeyboard;

    public static String USERNAME_REQUIREMENTS = "LEN17-len4-";
    public static String PASSWORD_REQUIREMENTS = "noSpacelen5-";


    public static long INIT_TIME;

    public static double NanoToBase = 1000000000.0;
    public static double NanoToMilli = 1000000.0;

    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;

    public static void setUp(SharedPreferences sharedPrefs, int w, int h){
        sharedPref = sharedPrefs;
        editor = sharedPref.edit();
        width = w;
        height = h;
        started = true;
        myKeyboard = new Keyboard();
        INIT_TIME = System.currentTimeMillis();
    }
}
