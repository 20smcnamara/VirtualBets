package com.example.virtualbets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

public class StartScene implements Scene{

    private final ArrayList<Button> buttons = new ArrayList<>();
    private final ArrayList<TextBox> textBoxes = new ArrayList<>();
    private int state = 0;
    private int returnTo = -1;
    Bitmap background;

    public StartScene(Bitmap background) {
        this.background = background;
    }

    @Override
    public int update() { //Check for updates in state
        if (state == 0 && Constants.started){ //We can start creating the details
            state++; // Init marker

            // Buttons
            Rect enterRect = new Rect( (int) (Constants.width * 0.25), (int) (Constants.height * 0.65), (int) (Constants.width * 0.75), (int) (Constants.height * 0.75));
            HashMap<String, Object> args = new HashMap<>();
            args.put("txt", "Enter");
            args.put("color", new Integer[] {255, 255, 125, 70});
            args.put("txtColor", new Integer[] {255, 175, 75, 35});
            Button enterButton = new SimpleButton(enterRect, 1, args);
            buttons.add(enterButton);

            Rect registerRect = new Rect( (int) (Constants.width * 0.25), (int) (Constants.height * 0.80), (int) (Constants.width * 0.75), (int) (Constants.height * 0.90));
            args = new HashMap<>();
            System.out.println(registerRect);
            args.put("txt", "Register");
            args.put("color", new Integer[] {255, 255, 125, 70});
            args.put("txtColor", new Integer[] {255, 175, 75, 35});
            Button registerButton = new SimpleButton(registerRect, 2, args);
            buttons.add(registerButton);

            //Text
            Rect welcomeRect = new Rect( (int) (Constants.width * 0.10), (int) (Constants.height * 0.15), (int) (Constants.width * 0.90), (int) (Constants.height * 0.35));
            args = new HashMap<>();
            args.put("shadow", new Integer[] {255, 200, 10, 10, 2, 2, 8});
            args.put("color", new Integer[] {255, 255, 100, 100,});
            args.put("growth", 1);
            TextBox welcomeBox = new TextBox("Welcome", welcomeRect, args);
            textBoxes.add(welcomeBox);
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
        String msg = "haha you touched me silly";
        if (Math.floor(Math.random() * 100000) % 42069 == 0){ //Idk why it is what it is, but it is. Isn't it?
            msg = getSecretMsg();
        }

        System.out.println(msg);
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

    private String getSecretMsg(){ //If you really want to know change the random statement don't try to read this
        String letters = "abvdyfdiofjmdskaerheavf cIsorfeafnt";
        int index = 18;
        int len = 2;
        String msg = letters.substring(index, index + len);
        index = 4;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 23;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 25;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 23;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 6;
        len = 2;
        msg += letters.substring(index, index + len);
        index = 3;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 23;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 33;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 8;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 34;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 23;
        len = 2;
        msg += letters.substring(index, index + len);
        index = 27;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 33;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 26;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 30;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 33;
        len = 1;
        msg += letters.substring(index, index + len);
        index = 34;
        len = 1;
        msg += letters.substring(index, index + len);
        return msg;
    }
}
