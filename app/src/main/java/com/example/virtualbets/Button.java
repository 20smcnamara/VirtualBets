package com.example.virtualbets;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Button {
    void draw(Canvas canvas);
    void update(int val);
    int receiveTouch(MotionEvent event);
}
