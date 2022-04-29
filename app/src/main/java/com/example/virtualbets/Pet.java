package com.example.virtualbets;

import android.graphics.Bitmap;
import android.graphics.Canvas;

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

        ArrayList<Location> heads = type.getHeads();
        ArrayList<Location> feet = type.getShoes();

        Draw.drawPNG(canvas, picture, x - picture.getWidth() / 2, y - picture.getHeight() / 2);

        for (Wearable wearable: wearing){
            switch (wearable.getType()){
                case Wearable.HAT:
                    for (Location loc: heads){
                        wearable.draw(canvas, loc, 1f);
                    }
                case Wearable.SHOES:
                    for (Location loc: feet){
                        wearable.draw(canvas, loc, 1f);
                    }
            }
        }
    }
}
