package com.example.virtualbets;


public class WizardData {

    private static String pets;
    private static String names;
    private static String clothes;
    private static String fullData;

    private static final String[] AssetNames = new String[] { "telon",
                                                                "steve",
                                                                "arrowright",
                                                                "arrowleft",
                                                                "arrowup",
                                                                "arrowdown",
                                                                "basichat",
                                                                "wpihat",
                                                                "wpishoe",
                                                                "basicshoe",
                                                            };
    private static final String[] Clothes = new String[] { "0|basicHat|Basic Hat|+",
                                                            "0|wpiHat|WPI Hat|WPI-",
                                                            "1|wpiShoe|WPI Shoe|WPI-",
                                                            "1|basicShoe|Basic Shoe|WPI-"};
    private static final String[] Pets = new String[] { "Tongus Elongus|telon|270,1,10,10:|250,357,17,15:20,355,17,15",
                                                            "Steve|steve|80,40,40,40:|136,365,132,60:" };


    private final StringBuilder builder = new StringBuilder();

    public WizardData() {
        loadAssets();
        loadClothes();
        loadPets();
        loadFull();
    }

    private void loadFull() {
        builder.setLength(0);

        builder.append(names);
        builder.append(pets);
        builder.append(clothes);

        fullData = builder.toString();
    }

    private void loadPets() {
        builder.setLength(0);

        builder.append("PETS\n");

        for (String pet: Pets) {
            builder.append(pet);
            builder.append("\n");
        }

        builder.append("END");

        pets = builder.toString();
    }

    private void loadClothes() {
        builder.setLength(0);

        builder.append("CLOTHES\n");

        for (String clothing: Clothes) {
            builder.append(clothing);
            builder.append("\n");
        }

        builder.append("END");

        clothes = builder.toString();
    }

    private void loadAssets() {
        builder.setLength(0);

        builder.append("ASSETS\n");

        for (String name: AssetNames) {
            builder.append(name);
            builder.append("\n");
        }

        builder.append("END");

        names = builder.toString();
    }

    public String readAll() {
        return fullData;
    }

    public String readNames() {
        return names;
    }

    public String readPets() {
        return pets;
    }

    public String readClothes() {
        return clothes;
    }
}
