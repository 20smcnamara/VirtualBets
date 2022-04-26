package com.example.virtualbets;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Constants {

    public static boolean started = false;

    public static int width;
    public static int height;

    public static int START_SCENE = 0;
    public static int LOGIN_SCENE = 1;
    public static int REGISTER_SCENE = 2;
    public static int HOME_SCENE = 3;
    public static int COMMUNITY_SETUP_SCENE = 4;
    public static int PET_SETUP_SCENE = 5;
    public static int COMMUNITY_CREATION_SCENE = 6;

    public static Keyboard myKeyboard;

    public static String USERNAME_REQUIREMENTS = "LEN17-len4-";
    public static String PASSWORD_REQUIREMENTS = "noSpacelen5-";


    public static long INIT_TIME;

    public static double NanoToBase = 1000000000.0;
    public static double NanoToMilli = 1000000.0;

    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;

    public static HashMap<String, Bitmap> images = new HashMap<String, Bitmap>();

    private static Resources resources;

    public static void setUp(SharedPreferences sharedPrefs, Resources resources, Context context, int w, int h){
        sharedPref = sharedPrefs;
        editor = sharedPref.edit();
        width = w;
        height = h;
        started = true;
        myKeyboard = new Keyboard();
        INIT_TIME = System.currentTimeMillis();
        Constants.resources = resources;
        loadImages(context);
    }

    public static void loadImages(Context context) {
        File data = new File("WizardData");
        String FullData = data.toString();
        ArrayList<String> toLoad = new ArrayList<>();
        int mode = 0;

        for (String line: FullData.split("\n")){
            if (line.equals("ASSESTS")){
                mode = 1;
            } else if (mode == 1  && line.equals("END")){
                break;
            } else {
                try { // Gross wizard of oz stuff cuz I am about to sleep
                    if (line.startsWith("s")) {
                        Bitmap b = BitmapFactory.decodeResource(resources, R.drawable.steve);
                        images.put(line, b);
                    }
                    if (line.startsWith("t")) {
                        Bitmap b = BitmapFactory.decodeResource(resources, R.drawable.telon);
                        images.put(line, b);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap getImage(String name){
        return images.get(name);
    }
}
