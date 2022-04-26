package com.example.virtualbets;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class Wizard { //Not efficient don't look

    public static ArrayList<Wearable> loadWearables(){
        ArrayList<Wearable> LOW = new ArrayList<>();

        File data = new File("WizardData");
        String FullData = data.toString();
        int mode = -1;
        int id = 0;

        for (String s: FullData.split("\n")){
            if (s.equals("CLOTHES")){
                mode = 0;
                id = 0;
            }
            if (mode == 0 && s.equals("END")){
                break;
            }
            if (mode == 0){
                String[] parts = s.split("\\|"); //Split aspects

                HashSet<String> tags = new HashSet<>(); //Find tags
                if (parts[3].contains("-")) {
                    for (String tag: parts[3].split("-")){
                        if (!tag.equals("")) {
                            tags.add(tag);
                        }
                    }
                }

                switch (parts[0]) { //Make wearables
                    case "HAT":
                        LOW.add(new Wearable(id, Wearable.HAT, parts[2], Constants.getImage(parts[1]), tags));
                    case "SHOES":
                        LOW.add(new Wearable(id, Wearable.SHOES, parts[2], Constants.getImage(parts[1]), tags));
                }

                id++;
            }
        }

        return LOW;
    }

    public static ArrayList<Pet> loadPets(){
        ArrayList<Pet> LOP = new ArrayList<>();

        File data = new File("WizardData");
        String FullData = data.toString();
        int mode = -1;

        for (String line: FullData.split("\n")){
            if (line.equals("PETS")){
                mode = 1;
            }
            if (mode == 1 && line.equals("END")){
                break;
            }
            if (mode == 1) {
                String[] lineData = line.split("\\|");

                String type = lineData[0];

                Bitmap image = Constants.getImage(lineData[1]);

                ArrayList<Location> heads = new ArrayList<>();
                for (String headInfo : lineData[2].split(":")) {
                    if (headInfo.equals("")) {
                        break;
                    } else {
                        String[] locInfo = headInfo.split(",");
                        int x = Integer.parseInt(locInfo[0]);
                        int y = Integer.parseInt(locInfo[1]);
                        heads.add(new Location(x, y, image.getWidth(), image.getHeight()));
                    }
                }

                ArrayList<Location> feet = new ArrayList<>();
                for (String footInfo : lineData[2].split(":")) {
                    if (footInfo.equals("")) {
                        break;
                    } else {
                        String[] locInfo = footInfo.split(",");
                        int x = Integer.parseInt(locInfo[0]);
                        int y = Integer.parseInt(locInfo[1]);
                        int w = Integer.parseInt(locInfo[2]);
                        int h = Integer.parseInt(locInfo[2]);
                        feet.add(new Location(x, y, w, h));
                    }
                }

                new Pet(type, image, heads, feet);
            }
        }

        return LOP;
    }

}
