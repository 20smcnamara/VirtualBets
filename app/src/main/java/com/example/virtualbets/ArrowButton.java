package com.example.virtualbets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

public class ArrowButton implements Button{

    public final static int RIGHT = 0;
    public final static int LEFT = 1;
    public final static int UP = 2;
    public final static int DOWN = 3;

    private int direction = 0;
    private int state = 0;

    private final int id;

    private final Rect box;

    private final Bitmap bigImage;
    private final Bitmap smallImage;

    private String getFile() {
        switch (direction) {
            case 1:
                return "arrowleft";

            case 2:
                return "arrowup";

            case 3:
                return "arrowdown";

            default:
                return "arrowright";
        }
    }

    public ArrowButton(int id, Rect r, int direction) {
        this.direction = direction;
        this.id = id;

        box = r;

        Rect smallBox = new Rect((int) (r.left + r.width() * 0.1), (int) (r.top + r.height() * 0.1), (int) (r.right - r.width() * 0.1), (int) (r.bottom - r.height() * 0.1));

        Bitmap ogImage = Constants.getImage(getFile());

        bigImage = Bitmap.createScaledBitmap(ogImage, r.width(), r.height(), false);
        smallImage = Bitmap.createScaledBitmap(ogImage, smallBox.width(), smallBox.height(), false);
    }

    @Override
    public void draw(Canvas canvas) {
        if (state == 0) {
            Draw.drawPNG(canvas, bigImage, box.left, box.top);
        } else if (state == 1) {
            Draw.drawPNG(canvas, smallImage, box.left, box.top);
        }
    }

    @Override
    public void update(int val) {

    }

    @Override
    public int receiveTouch(MotionEvent event) {
        if (state == -1) {
            return -1;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (box.left < event.getX() && event.getX() < box.right && box.top < event.getY() && box.bottom > event.getY()) {
                return id;
            }
        }
        return -1;
    }

    public void hide() {
        state = -1;
    }

    public void show() {
        state = 0;
    }
}
