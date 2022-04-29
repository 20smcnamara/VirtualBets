package com.example.virtualbets;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.HashMap;

public class WeakButton implements Button{

    public final int id;
    public String txt = "";
    public Integer[] txtColor = {255, 125, 125, 125};


    private final Rect box;

    public WeakButton(Rect r, int di){
        id = di;
        box = r;
    }

    public WeakButton(Rect r, int di, HashMap<String, Object> args){
        id = di;
        box = r;

        if (args.containsKey("txt")) {
            txt = (String) args.get("txt");
        }

        if (args.containsKey("txtColor")) {
            txtColor = (Integer[]) args.get("txtColor");
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Draw.drawUnderlinedText(canvas, box, txtColor, txt);
    }

    @Override
    public void update(int val) {

    }

    public int receiveTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (box.left < event.getX() && event.getX() < box.right && box.top < event.getY() && box.bottom > event.getY()) {
                return id;
            }
        }
        return -1;
    }
}
