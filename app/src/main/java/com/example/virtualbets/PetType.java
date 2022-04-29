package com.example.virtualbets;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class PetType {

    private final String typeName;
    private final String image;

    private final ArrayList<Location> heads;
    private final ArrayList<Location> feet;

    public PetType(String type, String image, ArrayList<Location> heads, ArrayList<Location> shoes){
        typeName = type;
        this.image = image;
        this.heads = heads;
        this.feet = shoes;
    }

    public String getTypeName() {
        return typeName;
    }

    public String imageId() {
        return image;
    }

    public Bitmap getImage() {
        return Constants.getImage(image);
    }

    public ArrayList<Location> getShoes() {
        return feet;
    }

    public ArrayList<Location> getHeads() {
        return heads;
    }
}
