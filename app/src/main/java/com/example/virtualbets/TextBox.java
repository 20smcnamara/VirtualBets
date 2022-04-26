package com.example.virtualbets;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.HashMap;

public class TextBox {

    private int steps = 4;
    private int maxDelay = 5;
    private int delay = maxDelay;
    private int step = 0;
    private Boolean hidden = false;
    private Boolean background = false;
    private Integer growth = 0;
    private Integer inc;
    private String text = "";
    private Integer[] color = {255, 0, 0, 0};
    private Integer[] backgroundColor = {255, 0, 0, 0};
    private Integer[] shadow = {255, 0, 0, 0, 0, 0, 0};
    private Rect rect;

    public TextBox(String text, Rect rect, HashMap<String, Object> args){
        this.text = text;
        this.rect = rect;

        if (args.containsKey("background")){
            background = true;
        }
        if (args.containsKey("backgroundColor")){
            background = true;
            backgroundColor = (Integer[]) args.get("background");
        }
        if (args.containsKey("color")){
            color = (Integer[]) args.get("color");
        }
        if (args.containsKey("shadow")){
            shadow = (Integer[]) args.get("shadow");
        }
        if (args.containsKey("growth")){
            growth = (Integer) args.get("growth");
        }
        if (args.containsKey("hidden")){
            hidden = (Boolean) args.get("hidden");
        }
        if (args.containsKey("inc")){
            inc = (Integer) args.get("inc");
        } else {
            inc = rect.height() / 128;
        }
    }

    public void draw(Canvas canvas){
        if (hidden){
            return;
        }

        if (growth != 0) {
            delay--;

            if (delay == 0) {
                if (step == steps || step == -steps) {
                    growth *= -1;
                }
                step += growth;
                rect = new Rect(rect.left - inc * growth, rect.top - inc * growth,
                        rect.right + inc * growth, rect.bottom + inc * growth);
                delay = maxDelay;
            }
        }

        if (shadow[4] > 0){
            Draw.drawText(canvas, rect, shadow, text);
            Rect innerRect = new Rect(rect.left + shadow[4] + shadow[5], rect.top + shadow[4] + shadow[6], rect.right - shadow[4] + shadow[5], rect.bottom - shadow[4] + shadow[6]);
            Draw.drawText(canvas, innerRect, color, text);
        } else {
            if (background){
                Draw.drawRect(canvas, rect, backgroundColor);
            }
            Draw.drawText(canvas, rect, color, text);
        }
    }

    public void toggleVisibility() {
        hidden = !hidden;
    }

    public void show(){
        hidden = false;
    }

    public void hide(){
        hidden = true;
    }
}
