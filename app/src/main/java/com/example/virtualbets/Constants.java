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
import java.util.Calendar;
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
    public static int EVENT_CREATION_SCENE = 7;
    public static int BAD_SCENE = 8;
    public static int LAST_SCENE = 9;

    public static Keyboard myKeyboard;

    public static String PET_NAME_REQUIREMENTS = "LEN28-";
    public static String CODE_REQUIREMENTS = "LEN7-len5-NUMSnoSpace";
    public static String USERNAME_REQUIREMENTS = "LEN17-len4-";
    public static String PASSWORD_REQUIREMENTS = "noSpacelen5-";


    public static long INIT_TIME;

    public static double NanoToBase = 1000000000.0;
    public static double NanoToMilli = 1000000.0;

    public static Integer[] PRIMARY_THEME_COLOR = Draw.LIGHT_ORANGE;
    public static Integer[] SECONDARY_THEME_COLOR = Draw.DARK_ORANGE;

    public static MainController controller;

    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;

    public static WizardData wiz = new WizardData();

    public static HashMap<String, Bitmap> images = new HashMap<>();

    public final static ArrayList<Community> communities = new ArrayList<>();
    public final static ArrayList<ArrayList<Wearable>> wearables = new ArrayList<>();
    public final static HashMap<String, PetType> petTypes = new HashMap<>();

    private final static ArrayList<String> queue = new ArrayList<>();

    public static Event focusedEvent = null;

    public static void setUp(SharedPreferences sharedPrefs, MainController control, int w, int h){

        if (started) {
            return;
        }

        sharedPref = sharedPrefs;
        editor = sharedPref.edit();
        Constants.clear();
        width = w;
        height = h;
        started = true;
        myKeyboard = new Keyboard();
        INIT_TIME = System.currentTimeMillis();
        controller = control;

        loadImages();

        int index = 0;

        for (ArrayList<Wearable> LOW: Wizard.loadWearables()){ //Copy wearables
            wearables.add(new ArrayList<>());

            for (Wearable wearable: LOW){
                wearables.get(index).add(wearable);
            }

            index++;
        }

        petTypes.putAll(Wizard.loadPets()); //Copy pets
    }

    public static void loadImages() {
        String FullData = Constants.wiz.readNames();

        int mode = 0;

        for (String line: FullData.split("\n")){
            if (line.equals("ASSETS")){
                mode = 1;
            } else if (mode == 1 && line.equals("END")){
                break;
            } else {
                try { // Gross wizard of oz stuff cuz I am about to sleep
                    images.put(line, controller.findImage(line));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void clear() {
        editor.clear();
        editor.apply();
    }

    public static void addString(String code, String value) {
        editor.putString(code, value);
        editor.apply();
    }

    public static void addInt(String code, int value) {
        editor.putInt(code, value);
        editor.apply();
    }

    public static Bitmap getImage(String name){
        return images.get(name);
    }

    public static Wearable getWearable(int id, int type) {
        return wearables.get(type).get(id);
    }

    public String popQueue() {
        if (!queue.isEmpty()) {
            String toReturn = queue.get(0);
            queue.remove(0);

            return toReturn;
        } else {
            return "None";
        }
    }

    public static void addToQueue(String str) {
        String[] data = str.split("&");

        for (Community c: communities) {
            if (c.getCode().equals(data[0]) && data[1].equals("Event")) {
                queue.add(str);
            }
        }
    }
}
