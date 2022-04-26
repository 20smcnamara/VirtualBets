package com.example.virtualbets;

public class Location {

    private final int x;
    private final int y;

    private float ratioX;
    private float ratioY;
    private int direction = 0;

    public Location(int x, int y, int width, int height) {
        this.ratioX = 1f / width * x;
        this.ratioY = 1f / height * y;
        this.x = x;
        this.y = y;
    }

    public float getRatioX(){
        return ratioX;
    }

    public float getRatioY() {
        return ratioY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDir() {
        return direction;
    }

    public void flip() {
        direction = 1 - direction;
    }
}
