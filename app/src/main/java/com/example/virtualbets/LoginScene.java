package com.example.virtualbets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginScene implements Scene{

    private boolean focused = true;
    private boolean good = false;
    private int activeText = -1;
    private final static int HINT = 2;
    private final ArrayList<Button> buttons = new ArrayList<>();
    private final ArrayList<TextForm> textForms = new ArrayList<>();
    private final ArrayList<TextBox> labelBoxes = new ArrayList<>();
    //private final ArrayList<TextBox> helpBoxes = new ArrayList<>();
    private int state = 0;
    private int returnTo = -1;
    Bitmap background;
    Context context;

    public LoginScene(Context context, Bitmap background) {
        this.context = context;
        this.background = background;
    }

    @Override
    public int update() { //Check for updates in state
        if (state == 0 && Constants.started){ //We can start creating the details
            state++; // Init marker

            // Login and register buttons
            Rect enterRect = new Rect( (int) (Constants.width * 0.25), (int) (Constants.height * 0.65), (int) (Constants.width * 0.75), (int) (Constants.height * 0.75));
            HashMap<String, Object> args = new HashMap<>();
            args.put("txt", "Login");
            args.put("color", new Integer[] {255, 255, 125, 70});
            args.put("txtColor", new Integer[] {255, 175, 75, 35});
            Button enterButton = new SimpleButton(enterRect, 1, args);
            buttons.add(enterButton);

            Rect registerRect = new Rect( (int) (Constants.width * 0.20), (int) (Constants.height * 0.80), (int) (Constants.width * 0.80), (int) (Constants.height * 0.85));
            args = new HashMap<>();
            args.put("txt", "Register");
            args.put("txtColor", new Integer[] {255, 125, 60, 25});
            Button registerButton = new WeakButton(registerRect, 2, args);
            buttons.add(registerButton);


            //Username GUI
            Rect userRect = new Rect( (int) (Constants.width * 0.10), (int) (Constants.height * 0.05), (int) (Constants.width * 0.90), (int) (Constants.height * 0.15));
            args = new HashMap<>();
            args.put("color", new Integer[] {255, 200, 100, 59,});
            TextBox userBox = new TextBox("Username:", userRect, args);
            labelBoxes.add(userBox);

            Rect userNameRect = new Rect( (int) (Constants.width * 0.05), (int) (Constants.height * 0.15), (int) (Constants.width * 0.95), (int) (Constants.height * 0.25));
            args = new HashMap<>();
            args.put("Requirements", Constants.USERNAME_REQUIREMENTS);
            args.put("Required", true);
            TextForm txtForm = new TextForm(0, userNameRect, args);
            textForms.add(txtForm);

            //Password GUI
            Rect passLabelRect = new Rect( (int) (Constants.width * 0.10), (int) (Constants.height * 0.32), (int) (Constants.width * 0.90), (int) (Constants.height * 0.42));
            args = new HashMap<>();
            args.put("color", new Integer[] {255, 200, 100, 59,});
            TextBox passBox = new TextBox("Password:", passLabelRect, args);
            labelBoxes.add(passBox);

            Rect passRect = new Rect( (int) (Constants.width * 0.05), (int) (Constants.height * 0.425), (int) (Constants.width * 0.95), (int) (Constants.height * 0.525));
            args = new HashMap<>();
            args.put("Requirements", Constants.PASSWORD_REQUIREMENTS);
            args.put("Required", true);
            args.put("secure", true);
            TextForm passForm = new TextForm(1, passRect, args);
            textForms.add(passForm);

            //Hint box
            Rect passHintRect = new Rect( 0, (int) (Constants.height * 0.90), Constants.width, (int) (Constants.height * 0.95));
            args = new HashMap<>();
            args.put("color", new Integer[] {255, 255, 0, 0,});
            args.put("hidden", true);
            args.put("background", true);
            TextBox passHintBox = new TextBox("Please Enter a username and password", passHintRect, args);
            labelBoxes.add(passHintBox);
        }

        if (returnTo != -1) { //We can switch to another scene
            state = 0;
            int goTo = returnTo;
            good = false;
            labelBoxes.get(HINT).hide();
            returnTo = -1;
            return goTo;
        } else {
            return -1;
        }
    }

    @Override
    public void draw(Canvas canvas) { //Draw background then features
        drawBackground(canvas);

        for (Button b : buttons) {
            b.draw(canvas);
        }

        for (TextForm tb : textForms) {
            tb.draw(canvas);
        }

        for (TextBox tb : labelBoxes){
            tb.draw(canvas);
        }

        if (!Constants.myKeyboard.isHidden()){
            Constants.myKeyboard.draw(canvas);
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
        System.out.println(good + " : " + state + " : " + focused);
        if (state == 1 && good) {
            if (focused) {
                for (Button button : buttons) {
                    switch (button.receiveTouch(event)) {
                        case 1:
                            Login();
                            break;
                        case 2:
                            gotoRegister();
                            break;
                        default:
                            break;
                    }
                }
                for (TextForm form : textForms) {
                    int result = form.handleTouch(event);
                    if (result != -1) {
                        focused = false;
                        activeText = form.getId();
                    }
                }
            } else if (!Constants.myKeyboard.isHidden() && !focused) {
                Constants.myKeyboard.handleMotionEvent(event);
                textForms.get(activeText).setString(Constants.myKeyboard.getResult());
                if (Constants.myKeyboard.isSubmitted()) {
                    textForms.get(activeText).fill(Constants.myKeyboard.getResult());
                    textForms.get(activeText).isValid();
                    focused = true;
                }
            }
        }
        if (!good){
            good = true;
        }
    }

    private void Login(){
        for (TextForm textForm: textForms){
            if (!textForm.isValid()){
                labelBoxes.get(HINT).show();
                return;
            }
        }

        labelBoxes.get(HINT).hide();

        returnTo = Constants.HOME_SCENE; // TODO add checks
    }

    private void gotoRegister(){
        returnTo = Constants.REGISTER_SCENE; // No checks needed
    }

}
