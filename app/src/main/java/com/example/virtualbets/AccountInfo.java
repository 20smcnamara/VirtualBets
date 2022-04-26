package com.example.virtualbets;

import java.util.ArrayList;

public class AccountInfo {

    private String username;
    private int communityCount;
    private int priorityCommunity;
    private ArrayList<Pet> pets = new ArrayList<>();
    private ArrayList<Community> communities = new ArrayList<>();

    public AccountInfo(ArrayList<Pet> pets, ArrayList<Community> communities){
        username = Constants.sharedPref.getString("user", "NAN");
        communityCount = Constants.sharedPref.getInt("communityCount", 0);
        priorityCommunity = Constants.sharedPref.getInt("priorityCommunity", 0);

        if (pets.size() != communityCount || communityCount != communities.size()){
            System.out.println("Error memory tampering found");
        }
    }
}
