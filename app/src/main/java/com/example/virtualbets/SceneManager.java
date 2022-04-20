package com.example.virtualbets;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;

public class SceneManager {

    private static int ACTIVE_SCENE;
    private final ArrayList<Scene> scenes = new ArrayList<>();

    public SceneManager(ArrayList<Bitmap> images) {
        ACTIVE_SCENE = 0;
        scenes.add(new StartScene(images.get(0)));
        Draw.Init();
    }

    public void update(){
        int result = scenes.get(ACTIVE_SCENE).update();

        if (result > -1){
            setScene(result);
        }
    }

    public void receiveTouch(MotionEvent event){
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }

    public void draw(Canvas canvas){
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }

    public void setScene(int scene){
        ACTIVE_SCENE = scene;
    }
}