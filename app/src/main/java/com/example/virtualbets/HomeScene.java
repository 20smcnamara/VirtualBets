package com.example.virtualbets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeScene implements Scene{

    private int state = 0;
    private int returnTo = -1;

    private final ArrayList<Button> buttons = new ArrayList<>();
    private final ArrayList<TextBox> textBoxes = new ArrayList<>();

    private AccountInfo accountInfo;

    Bitmap background;

    public HomeScene(Context context, Bitmap background) {
        this.background = background;
    }

    @Override
    public int update() { //Check for updates in state
        nextScene:
        if (state == 0 && Constants.started){ //We can start creating the details
            state++; // Init marker

            if (!Constants.sharedPref.contains("CommunityCount")) {
                returnTo = Constants.COMMUNITY_SETUP_SCENE;
                break nextScene;
            } else if (!Constants.sharedPref.contains("PetCount")) {
                returnTo = Constants.PET_SETUP_SCENE;
                break nextScene;
            }

            if (returnTo != -1) {
                ArrayList<Community> communities = new ArrayList<>();

                for (int i = 0; i < Constants.sharedPref.getInt("CommunityCount", 0); i++) {
                    String Cname = "Community" + i;
                    int j = Constants.sharedPref.getInt(Cname, -1); // Turn users community index into constants community index

                    if (j != 0) {
                        communities.add(Constants.communities.get(j));
                    }
                }

                ArrayList<Pet> pets = new ArrayList<>();

                for (int i = 0; i < Constants.sharedPref.getInt("PetCount", 0); i++) {
                    String Pname = "Pet" + i;
                    String j = Constants.sharedPref.getString(Pname, "PetNotFound"); // Turn users community index into constants community index

                    if (!j.equals("PetNotFound")) {
                        // 1 Name, 2 Type, 3 HatId, 4 ShoeId
                        String[] data = j.split(":");
                        Pet p = new Pet(data[0], Constants.petTypes.get(data[1]));

                        for (int k = 0; k < 2; k++){
                            int id = Integer.parseInt(data[2 + k]);
                            p.addWearable(Constants.getWearable(id, k));
                        }
                    }
                }

                accountInfo = new AccountInfo(pets, communities);
            }
        }

        if (returnTo != -1) { //We can switch to another scene
            state = 0;
            int goTo = returnTo;
            returnTo = -1;
            return goTo;
        }

        return -1;
    }

    @Override
    public void draw(Canvas canvas) { //Draw background then features
        drawBackground(canvas);

        for (Button b: buttons){
            b.draw(canvas);
        }

        for (TextBox tb: textBoxes){
            tb.draw(canvas);
        }
    }

    public void drawBackground(Canvas canvas) { //Setup background
        if (background != null && state > 0) {
            Draw.drawPNG(canvas, background);
        }

        if (state == 1) {
            for (Button b: buttons) {
                b.draw(canvas);
            }
        }

    }

    @Override
    public void terminate() {
        System.out.println("Good bye");
    }

    @Override
    public void receiveTouch(MotionEvent event) { // Check if event was on any buttons/forms
        if (state == 1) {
            for (Button button: buttons) {
                switch (button.receiveTouch(event)) {
                    case 1:
                        LoginOut();
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void LoginOut(){
        returnTo = Constants.START_SCENE; // TODO add checks
    }
}
