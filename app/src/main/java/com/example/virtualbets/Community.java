package com.example.virtualbets;

import java.util.ArrayList;
import java.util.Random;

public class Community {

    String code;

    ArrayList<Wearable> communityWearables;
    ArrayList<Pet> communityPets;
    ArrayList<String> users = new ArrayList<>();
    ArrayList<String> admin = new ArrayList<>();

    public Community(String admin){
        this.admin.add(admin);
        this.users.add(admin);
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++){
            stringBuilder.append(r.nextInt(26) + 65);
        }
        code = stringBuilder.toString();

        communityWearables = Wizard.loadWearables();
        communityPets = Wizard.loadPets();
    }

    public void addMember(String user){
        users.add(user);
    }

}
