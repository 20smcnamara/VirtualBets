package com.example.virtualbets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunitySetupScene implements Scene{

    private boolean focused = true;
    private boolean good = false;
    private int activeText = -1;
    private final ArrayList<Button> buttons = new ArrayList<>();
    private final ArrayList<TextForm> textForms = new ArrayList<>();
    private final ArrayList<TextBox> labelBoxes = new ArrayList<>();
    private final ArrayList<TextBox> helpBoxes = new ArrayList<>();
    private int state = 0;
    private int returnTo = -1;
    Bitmap background;
    Context context;

    public CommunitySetupScene(Context context, Bitmap background) {
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
            args.put("txt", "Join");
            args.put("color", new Integer[] {255, 255, 125, 70});
            args.put("txtColor", new Integer[] {255, 175, 75, 35});
            Button enterButton = new SimpleButton(enterRect, 1, args);
            buttons.add(enterButton);

            Rect registerRect = new Rect( (int) (Constants.width * 0.10), (int) (Constants.height * 0.80), (int) (Constants.width * 0.90), (int) (Constants.height * 0.85));
            args = new HashMap<>();
            args.put("txt", "or create your own");
            args.put("txtColor", new Integer[] {255, 125, 60, 25});
            Button registerButton = new WeakButton(registerRect, 2, args);
            buttons.add(registerButton);


            //Code box GUI
            Rect codeRect = new Rect( (int) (Constants.width * 0.05), (int) (Constants.height * 0.20), (int) (Constants.width * 0.95), (int) (Constants.height * 0.35));
            args = new HashMap<>();
            args.put("color", new Integer[] {255, 200, 100, 59,});
            TextBox userBox = new TextBox("6 Digit Community Code:", codeRect, args);
            labelBoxes.add(userBox);

            Rect codeBoxRect = new Rect( (int) (Constants.width * 0.02), (int) (Constants.height * 0.35), (int) (Constants.width * 0.98), (int) (Constants.height * 0.45));
            args = new HashMap<>();
            args.put("Requirements", Constants.CODE_REQUIREMENTS);
            args.put("Required", true);
            TextForm txtForm = new TextForm(0, codeBoxRect, args);
            textForms.add(txtForm);

            Rect codeHintRect = new Rect( 0, (int) (Constants.height * 0.475), Constants.width, (int) (Constants.height * 0.525));
            args = new HashMap<>();
            args.put("color", new Integer[] {255, 200, 100, 50,});
            args.put("hidden", true);
            args.put("background", true);
            TextBox userHintBox = new TextBox("Codes should be 6 characters silly", codeHintRect, args);
            helpBoxes.add(userHintBox);
        }

        if (returnTo != -1) { //We can switch to another scene
            state = 0;
            int goTo = returnTo;
            good = false;
            for (TextBox textBox: helpBoxes){
                textBox.hide();
            }
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

        for (TextBox tb : helpBoxes){
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
        if (state == 1 && good) {
            if (focused) {
                for (Button button : buttons) {
                    switch (button.receiveTouch(event)) {
                        case 1:
                            EnterCode();
                            break;
                        case 2:
                            badPage();
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

    private void EnterCode(){
        int index = 0;
        boolean passed = true;

        for (TextForm textForm: textForms){
            if (!textForm.isValid()){
                helpBoxes.get(index).show();
                passed = false;
            } else {
                helpBoxes.get(index).hide();
            }
            index++;
        }

        if (!passed){
            return;
        }

        int values = Constants.sharedPref.getInt("CommunityCount", 1);

        Constants.addInt("CommunityCount", values);

        returnTo = Constants.HOME_SCENE;
    }

    private void badPage(){
        returnTo = Constants.BAD_SCENE; // No checks needed
    }

}
