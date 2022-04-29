package com.example.virtualbets;

import android.graphics.Rect;

public class Location {

    private final Rect rect;
    private int direction = 0;

    public Location(int x, int y, int width, int height) {
        this.rect = new Rect(x, y, x + width, y + height);
    }

    public int getX() {
        return rect.left;
    }

    public int getY() {
        return rect.top;
    }

    public Rect getRect() {
        return rect;
    }

    public float getXRatio(int x) {
        return 1f * rect.width() / x;
    }

    public float getYRatio(int y) {
        return 1f * rect.height() / y;
    }

    public int getDir() {
        return direction;
    }

    public void flip() {
        direction = 1 - direction;
    }
}
