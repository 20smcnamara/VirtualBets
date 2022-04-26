package com.example.virtualbets;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashSet;

public class Pet {

    private String name = "";
    private String type;

    private final HashSet<Integer> typesWorn = new HashSet<>();
    private final ArrayList<Wearable> wearing = new ArrayList<>();

    private final ArrayList<Location> hats;
    private final ArrayList<Location> shoes;

    private final Bitmap picture;

    public Pet(String type, Bitmap image, ArrayList<Location> heads, ArrayList<Location> feet){
        this.type = type;
        this.picture = image;
        hats = heads;
        shoes = feet;
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
        Draw.drawPNG(canvas, picture, x - picture.getWidth() / 2, y - picture.getHeight() / 2);

        for (Wearable wearable: wearing){
            switch (wearable.getType()){
                case Wearable.HAT:
                    for (Location loc: hats){
                        wearable.draw(canvas, loc, 1f);
                    }
                case Wearable.SHOES:
                    for (Location loc: shoes){
                        wearable.draw(canvas, loc, 1f);
                    }
            }
        }
    }
}
