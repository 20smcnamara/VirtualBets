package com.example.virtualbets;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.HashSet;
import java.util.Locale;

public class Wearable {

    private final int type;
    private final int id;

    public final static int HAT = 0;
    public final static int SHOES = 1;
    public final static int LAST_TYPE_ID = SHOES;

    private final String name;
    private final String imageName;
    private final Bitmap image;
    private final HashSet<String> tags;

    public Wearable(int id, int type, String name, String shortHand, Bitmap image, HashSet<String> tags){
        this.id = id;
        this.type = type;
        this.name = name;
        this.tags = tags;
        this.image = image;
        this.imageName = shortHand;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public void draw(Canvas canvas, Location loc, float scale){
        Bitmap newMap = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), false);
        if (loc.getDir() == 1) {
            newMap = Draw.flipBitmap(newMap, true, false);
        }
        Draw.drawPNG(canvas, newMap, loc.getX() - newMap.getWidth() / 2, loc.getY() - newMap.getHeight() / 2);
    }

    public void draw(Canvas canvas, Location loc){
        Bitmap toDraw = image;
        if (loc.getDir() == 1) {
            toDraw = Draw.flipBitmap(image, true, false);
        }
        Draw.drawPNG(canvas, toDraw, loc.getX() - image.getWidth() / 2, loc.getY() - image.getHeight() / 2);
    }

    public String getImage() {
        return imageName.toLowerCase(Locale.ROOT);
    }
}
