package com.example.virtualbets;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.HashMap;

public class SimpleButton implements Button{

    public final int id;
    public int style = 1;
    public String txt = "";
    public Integer[] color = {255, 50, 50, 50};
    public Integer[] txtColor = {255, 125, 125, 125};


    private final Rect box;

    public SimpleButton(Rect r, int di){
        id = di;
        box = r;
    }

    public SimpleButton(Rect r, int di, HashMap<String, Object> args){
        id = di;
        box = r;

        if (args.containsKey("style")) {
            Integer result = (Integer) args.get("style");
            if (result != null){
                style = result;
            }
        }

        if (args.containsKey("txt")) {
            txt = (String) args.get("txt");
        }

        if (args.containsKey("txtColor")) {
            txtColor = (Integer[]) args.get("txtColor");
        }

        if (args.containsKey("color")) {
            color = (Integer[]) args.get("color");
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (style == 0) {
            Draw.drawRect(canvas, box, color);
            Draw.drawText(canvas, box, txtColor, txt);
        } else if (style == 1){
            Draw.drawRoundRect(canvas, box, color);
            Draw.drawText(canvas, box, txtColor, txt);
        } else {
            System.out.println("Error drawing button: " + id);
        }
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
