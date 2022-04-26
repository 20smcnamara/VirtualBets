package com.example.virtualbets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Draw {

    public static Integer[] GREEN = {255, 0, 255, 0};
    public static Integer[] RED = {255, 255, 0, 0};
    public static Integer[] BLUE = {255, 0, 0, 255};
    public static Integer[] CYAN = {255, 0, 255, 255};
    public static Integer[] BACK = {255, 75, 75, 75};
    public static Integer[] BLACK = {255, 0, 0, 0};
    public static Integer[] WHITE = {255, 255, 255, 255};

    private static Paint paint;

    public static void Init(){
        paint = new Paint();
    }

    public static int RGBtoInt(Integer[] start){ // Note no transparency
        return Color.rgb(start[1], start[2], start[3]);
    }

    public static void drawRect(Canvas canvas, Rect rect, Integer[] color){
        if (color.length == 3) {
            paint.setARGB(255, color[0], color[1], color[2]);
        } else if (color.length == 4){
            paint.setARGB(color[0], color[1], color[2], color[3]);
        }

        canvas.drawRect(rect, paint);
    }

    public static void drawRoundRect(Canvas canvas, Rect rect, Integer[] color){
        paint.setColor(RGBtoInt(color));
        RectF r = new RectF(rect.left, rect.top, rect.right, rect.bottom);
        canvas.drawRoundRect(r, rect.height() / 6f, rect.height() / 6f, paint);
    }

    public static void drawPNG(Canvas canvas, Bitmap b){
        canvas.drawBitmap(b, 0, 0, paint);
    }

    public static void drawText(Canvas canvas, Rect rect, Integer[] color, String txt){
        paint.setColor(RGBtoInt(color));
        paint.setAlpha(color[0]);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(rect.height() * 0.8f);

        Rect bounds = new Rect();
        paint.getTextBounds(txt, 0, txt.length(), bounds);
        float diff = bounds.width() - rect.width();
        float size = paint.getTextSize();
        while (diff > 0) {
            paint.setTextSize(paint.getTextSize() - 2);
            paint.getTextBounds(txt, 0, txt.length(), bounds);
            diff = bounds.width() - rect.width();
        }

        canvas.drawText(txt, rect.left + rect.width() / 2.0f, rect.bottom - (rect.height() / 4f), paint);
        paint.setTextSize(size);
    }

    public static void drawUnderlinedText(Canvas canvas, Rect rect, Integer[] color, String txt){
        paint.setUnderlineText(true);
        drawText(canvas, rect, color, txt);
        paint.setUnderlineText(false);
    }

}
