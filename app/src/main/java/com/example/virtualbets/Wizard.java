package com.example.virtualbets;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class Wizard { //Not efficient don't look

    public static ArrayList<ArrayList<Wearable>> loadWearables(){
        ArrayList<ArrayList<Wearable>> LLOW = new ArrayList<>();
        ArrayList<Wearable> LOW = new ArrayList<>();

        String FullData = Constants.wiz.readClothes();

        int mode = -1;
        int id = 0;
        int index = 0;

        for (String s: FullData.split("\n")){
            System.out.println(s + ":" + mode);
            if (s.equals("CLOTHES")){
                mode = 0;
                id = 0;
            } else if (mode == 0 && s.equals("END")){
                LLOW.add(LOW);
                break;
            } else if (mode == 0){
                String[] parts = s.split("\\|"); //Split aspects

                HashSet<String> tags = new HashSet<>(); //Find tags
                if (parts[3].contains("-")) {
                    for (String tag: parts[3].split("-")){
                        if (!tag.equals("")) {
                            tags.add(tag);
                        }
                    }
                }

                int type = Integer.parseInt(parts[0]);

                if (type > Wearable.LAST_TYPE_ID) {
                    System.out.println("Error reading wearables. Consider updating max wearable.");
                    break;
                }

                String name = parts[2];

                String imageName = parts[1].toLowerCase(Locale.ROOT);
                Bitmap image = Constants.getImage(imageName);

                if (image == null) {
                    System.out.println("Fuck: " + imageName);
                }

                if (index != Integer.parseInt(parts[0])) {
                    LLOW.add(LOW);

                    id = 0;
                    LOW = new ArrayList<>();
                }

                LOW.add(new Wearable(id, type, name, imageName, image, tags));

                id++;
            }
        }

        return LLOW;
    }

    public static HashMap<String, PetType> loadPets(){
        HashMap<String, PetType> toReturn = new HashMap<>();

        String FullData = Constants.wiz.readPets();

        int mode = -1;

        for (String line: FullData.split("\n")){
            if (line.equals("PETS")){
                mode = 1;
            } else if (mode == 1 && line.equals("END")){
                break;
            } else if (mode == 1) {
                String[] lineData = line.split("\\|");

                String type = lineData[0];

                String shorthand = lineData[1];
                Bitmap image = Constants.getImage(shorthand);

                ArrayList<Location> heads = new ArrayList<>();
                for (String headInfo : lineData[2].split(":")) {
                    if (headInfo.isEmpty()) {
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

                toReturn.put(shorthand, new PetType(type, shorthand, heads, feet));
            }
        }

        return toReturn;
    }

}
