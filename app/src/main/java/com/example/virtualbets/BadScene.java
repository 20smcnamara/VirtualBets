package com.example.virtualbets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class BadScene implements Scene{

    private final ArrayList<Button> buttons = new ArrayList<>();
    private final ArrayList<TextBox> textBoxes = new ArrayList<>();
    private int state = 0;
    private int returnTo = -1;
    Bitmap background;

    public BadScene(Bitmap background) {
        this.background = background;
    }

    @Override
    public int update() { //Check for updates in state
        if (state == 0 && Constants.started){ //We can start creating the details
            state++; // Init marker

            // Buttons
            Rect enterRect = new Rect( (int) (Constants.width * 0.25), (int) (Constants.height * 0.65), (int) (Constants.width * 0.75), (int) (Constants.height * 0.75));
            HashMap<String, Object> args = new HashMap<>();
            args.put("txt", "Go Back");
            args.put("color", new Integer[] {255, 255, 125, 70});
            args.put("txtColor", new Integer[] {255, 175, 75, 35});
            Button enterButton = new SimpleButton(enterRect, 1, args);
            buttons.add(enterButton);

            //Text
            Rect welcomeRect = new Rect( (int) (Constants.width * 0.10), (int) (Constants.height * 0.15), (int) (Constants.width * 0.90), (int) (Constants.height * 0.35));
            args = new HashMap<>();
            args.put("shadow", new Integer[] {255, 200, 10, 10, 2, 2, 8});
            args.put("color", new Integer[] {255, 255, 100, 100,});
            TextBox welcomeBox = new TextBox("Not implemented yet", welcomeRect, args);
            textBoxes.add(welcomeBox);
        }

        if (returnTo != -1) { //We can switch to another scene
            state = 0;
            return Constants.BAD_SCENE;
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
        } else if (Constants.started) {
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
                        tryLogin();
                        break;
                    case 2:
                        gotoRegister();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void tryLogin(){
        returnTo = Constants.LOGIN_SCENE;
    }

    private void gotoRegister(){
        returnTo = Constants.REGISTER_SCENE;
    }
}
