package com.example.virtualbets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeScene implements Scene{

    private final ArrayList<Button> buttons = new ArrayList<>();
    private final ArrayList<TextBox> textBoxes = new ArrayList<>();
    private int state = 0;
    private int returnTo = -1;
    Bitmap background;

    public HomeScene(Bitmap background) {
        this.background = background;
    }

    @Override
    public int update() { //Check for updates in state
        nextScene:
        if (state == 0 && Constants.started){ //We can start creating the details
            state++; // Init marker

            if (!Constants.sharedPref.contains("CommunityCount")){
                returnTo = Constants.COMMUNITY_SETUP_SCENE;
                break nextScene;
            } else {

            }
        }

        if (returnTo != -1) { //We can switch to another scene
            state = 0;
            return returnTo;
        } else {
            return -1;
        }
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
