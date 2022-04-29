package com.example.virtualbets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashSet;

public class Pet {

    private String name;
    private PetType type;

    private final HashSet<Integer> typesWorn = new HashSet<>();
    private final ArrayList<Wearable> wearing = new ArrayList<>();


    public Pet(String name, PetType type){
        this.name = name;
        this.type = type;
    }

    public void addWearable(Wearable wearable){
        if (typesWorn.contains(wearable.getType())){
            for (Wearable w: wearing){
                if (w.getType() == wearable.getType()){
                    wearing.remove(w);
                }
            }
        }
        wearing.add(wearable);
    }

    public void removeWearable(int type){
        for (Wearable w: wearing){
            if (w.getType() == type){
                wearing.remove(w);
            }
        }
    }

    public void removeWearable(Wearable wearable){
        if (typesWorn.contains(wearable.getType())){
            removeWearable(wearable.getType());
        }
    }

    public void draw(Canvas canvas, int x, int y){
        Bitmap picture = type.getImage();
        draw(canvas, x, y, picture);
    }

    public void draw(Canvas canvas, int x, int y, Bitmap picture){

        ArrayList<Location> heads = type.getHeads();
        ArrayList<Location> feet = type.getShoes();

        Draw.drawPNG(canvas, picture, x, y);

        for (Wearable wearable: wearing){
            switch (wearable.getType()){
                case Wearable.HAT:
                    for (Location loc: heads){
                        float xRat = loc.getXRatio(picture.getWidth());
                        float yRat = loc.getYRatio(picture.getHeight());

                        int nx = (int) (x + loc.getX() * xRat);
                        int ny = (int) (y + loc.getY() * yRat);
                        int nw = (int) ((loc.getRect().width() - nx) * xRat);
                        int nh = (int) ((loc.getRect().height() - ny) * yRat);

                        Rect rect = new Rect( nx, (int) (ny - (nh * 0.80)), nx + nw, (int) (ny + (nh * 0.2)));

                        wearable.draw(canvas, rect, loc.getDir());
                    }
                case Wearable.SHOES:
                    for (Location loc: feet){
                        float xRat = loc.getXRatio(picture.getWidth());
                        float yRat = loc.getYRatio(picture.getHeight());

                        int nx = (int) (x + loc.getX() * xRat);
                        int ny = (int) (y + loc.getY() * yRat);
                        int nw = (int) ((loc.getRect().width() - nx) * xRat);
                        int nh = (int) ((loc.getRect().height() - ny) * yRat);

                        Rect rect = new Rect( nx, (int) y + picture.getHeight() - ny - nh, nx + nw, y + picture.getHeight() - ny);

                        wearable.draw(canvas, rect, loc.getDir());
                    }
            }
        }
    }

    public void draw(Canvas canvas, Rect imageRect) {
        Bitmap scaledPicture = type.getImage();
        scaledPicture = Bitmap.createScaledBitmap(scaledPicture, imageRect.width(), imageRect.height(), false);

        draw(canvas, imageRect.left, imageRect.top, scaledPicture);
    }
}
