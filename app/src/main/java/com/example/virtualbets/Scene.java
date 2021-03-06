package com.example.virtualbets;


import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {

    public int update();
    public void draw(Canvas canvas);
    public void terminate();
    public void receiveTouch(MotionEvent event);
}