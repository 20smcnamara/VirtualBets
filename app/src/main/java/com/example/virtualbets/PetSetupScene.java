package com.example.virtualbets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class PetSetupScene implements Scene{

    private boolean focused = true;
    private boolean good = false;
    private int activeText = -1;
    private final static int NEXT =  0;
    private final static int BACK =  NEXT + 1;
    private final static int RIGHT =  BACK + 1;
    private final static int LEFT = RIGHT + 1;
    private final ArrayList<Button> buttons = new ArrayList<>();
    private final ArrayList<TextForm> textForms = new ArrayList<>();
    private final ArrayList<TextBox> labelBoxes = new ArrayList<>();
    private final ArrayList<TextBox> helpBoxes = new ArrayList<>();
    private final static String[] labels = new String[] {"Pick a pet", "Pick a hat", "Pick some shoes", "Finalize"};
    private int state = 0;
    private int index = 0;
    private int returnTo = -1;
    Bitmap background;
    private String name = null;
    private PetType petType = null;
    private Wearable hat = null;
    private Wearable shoes = null;
    private Pet pet = null;
    private Rect imageRect;
    private final ArrayList<ArrayList<Bitmap>> images = new ArrayList<>();
    Context context;

    public PetSetupScene(Context context, Bitmap background) {
        this.context = context;
        this.background = background;
    }

    @Override
    public int update() { //Check for updates in state
        if (state == 0 && Constants.started){ //We can start creating the details
            state++; // Init marker

            Rect backRect = new Rect( (int) (Constants.width * 0.25), (int) (Constants.height * 0.87), (int) (Constants.width * 0.75), (int) (Constants.height * 0.98));
            HashMap<String, Object> args = new HashMap<>();
            args.put("txt", "back");
            args.put("txtColor", new Integer[] {255, 125, 63, 31});
            WeakButton backButton = new WeakButton(backRect, BACK, args);
            buttons.add(backButton);

            // Select and back buttons
            Rect enterRect = new Rect( (int) (Constants.width * 0.25), (int) (Constants.height * 0.75), (int) (Constants.width * 0.75), (int) (Constants.height * 0.85));
            args = new HashMap<>();
            args.put("txt", "Select");
            args.put("color", new Integer[] {255, 255, 125, 70});
            args.put("txtColor", new Integer[] {255, 175, 75, 35});
            SimpleButton enterButton = new SimpleButton(enterRect, NEXT, args);
            buttons.add(enterButton);

            //TODO Random pet button

            //Image nav buttons
            Rect rightA = new Rect( (int) (Constants.width * 0.86), (int) (Constants.height * 0.4), (int) (Constants.width * 0.96), (int) (Constants.height * 0.6) );
            ArrowButton rightButton = new ArrowButton(RIGHT, rightA, ArrowButton.RIGHT);
            buttons.add(rightButton);

            Rect leftA = new Rect( (int) (Constants.width * 0.06), (int) (Constants.height * 0.4), (int) (Constants.width * 0.16), (int) (Constants.height * 0.6) );
            ArrowButton leftButton = new ArrowButton(LEFT, leftA, ArrowButton.LEFT);
            leftButton.hide();
            buttons.add(leftButton);

            // Label rect
            Rect labeRect = new Rect( (int) (Constants.width * 0.05), (int) (Constants.height * 0.22), (int) (Constants.width * 0.95), (int) (Constants.height * 0.29));
            args = new HashMap<>();
            args.put("color", new Integer[] {255, 200, 100, 59,});
            TextBox labeBox = new TextBox(labels[0], labeRect, args);
            labelBoxes.add(labeBox);

            //Code box GUI
            Rect codeRect = new Rect( (int) (Constants.width * 0.05), (int) (Constants.height * 0.01), (int) (Constants.width * 0.95), (int) (Constants.height * 0.10));
            args = new HashMap<>();
            args.put("color", new Integer[] {255, 200, 100, 59,});
            TextBox userBox = new TextBox("Pick a name", codeRect, args);
            labelBoxes.add(0, userBox);

            Rect codeBoxRect = new Rect( (int) (Constants.width * 0.02), (int) (Constants.height * 0.11), (int) (Constants.width * 0.98), (int) (Constants.height * 0.20));
            args = new HashMap<>();
            args.put("Requirements", Constants.PET_NAME_REQUIREMENTS);
            args.put("Required", true);
            TextForm txtForm = new TextForm(0, codeBoxRect, args);
            textForms.add(txtForm);

            Rect codeHintRect = new Rect( 0, (int) (Constants.height * 0.21), Constants.width, (int) (Constants.height * 0.25));
            args = new HashMap<>();
            args.put("color", new Integer[] {255, 200, 100, 50,});
            args.put("hidden", true);
            args.put("background", true);
            TextBox userHintBox = new TextBox("Names must be less than 26 characters", codeHintRect, args);
            helpBoxes.add(userHintBox);

            //No item selection box
            Rect noRect = new Rect( (int) (Constants.width * 0.05), (int) (Constants.height * 0.4), (int) (Constants.width * 0.95), (int) (Constants.height * 0.6));
            args = new HashMap<>();
            args.put("color", new Integer[] {255, 255, 100, 100,});
            TextBox noBox = new TextBox("None", noRect, args);
            noBox.hide();
            labelBoxes.add(noBox);

            //Item demo
            imageRect = new Rect( (int) (Constants.width * 0.25), (int) (Constants.height * 0.3), (int) (Constants.width * 0.75), (int) (Constants.height * 0.75) );


            ArrayList<Bitmap> LOB = new ArrayList<>();

            for (PetType petType: Constants.petTypes.values()) {
                Bitmap preImage = petType.getImage();
                preImage = Bitmap.createScaledBitmap(preImage, imageRect.width(), imageRect.height(), false);
                LOB.add(preImage);
            }

            images.add(LOB);
            System.out.println("Start" + Constants.wearables.size());

            for (ArrayList<Wearable> LOW: Constants.wearables) {
                ArrayList<Bitmap> LOBB = new ArrayList<>();

                for (Wearable W: LOW) {
                    Bitmap preImage = Constants.getImage(W.getImage());
                    preImage = Bitmap.createScaledBitmap(preImage, imageRect.width(), imageRect.height(), false);
                    LOBB.add(preImage);
                }

                images.add(LOBB);
            }
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

        drawItem(canvas);

        for (Button b: buttons) {
            b.draw(canvas);
        }

        if (state == 1) {
            for (TextForm tb : textForms) {
                tb.draw(canvas);
            }
            for (TextBox tb : helpBoxes){
                tb.draw(canvas);
            }
        }

        for (TextBox tb : labelBoxes){
            tb.draw(canvas);
        }


        if (!Constants.myKeyboard.isHidden()){
            Constants.myKeyboard.draw(canvas);
        }
    }

    private void drawItem(Canvas canvas) {
        if (state > 0) {
            if (state == 1) {
                Draw.drawPNG(canvas, images.get(0).get(index), imageRect.left, imageRect.top);
            } else if (state == 4){
                pet.draw(canvas, imageRect);
            } else if (index > 0) {
                Draw.drawPNG(canvas, images.get(state - 1).get(index - 1), imageRect.left, imageRect.top);
            }
        }
    }

    public void drawBackground(Canvas canvas) { //Setup background
        if (background != null && state > 0) {
            Draw.drawPNG(canvas, background);
        }

    }

    @Override
    public void terminate() {
        System.out.println("Good bye");
    }

    @Override
    public void receiveTouch(MotionEvent event) { // Check if event was on any buttons/forms
        if (state > 0 && good) {
            if (focused) {
                for (Button button : buttons) {
                    int response = button.receiveTouch(event);

                    switch (response) {
                        case BACK:
                            back();
                            break;
                        case NEXT:
                            selectItem();
                            break;
                        case RIGHT:
                            nextItem();
                            break;
                        case LEFT:
                            prevItem();
                            break;
                        default:
                            break;
                    }
                }
                if (state == 1) {
                    for (TextForm form : textForms) {
                        int result = form.handleTouch(event);
                        if (result != -1) {
                            focused = false;
                            activeText = form.getId();
                        }
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

    private void back() {
        if (state > 1) {
            state--;
            if (state == 1) {
                labelBoxes.get(0).show();
            } if (state == 3) {
                labelBoxes.get(1).show();
                ((ArrowButton) buttons.get(RIGHT)).show();
            }
            labelBoxes.get(1).updateText(labels[state - 1]);
            index = 0;
        } else if (state == 1) {
            returnTo = Constants.LAST_SCENE;
        }
    }

    private void selectItem() {

        switch (state) {

            case 1:
                if (EnterCode()) {
                    petType = (PetType) Constants.petTypes.values().toArray()[index];
                    index = 0;
                    labelBoxes.get(0).hide();
                    helpBoxes.get(0).hide();
                    ((ArrowButton) buttons.get(RIGHT)).show();
                }
                break;

            case 2:
                if (index != 0) {
                    hat = Constants.getWearable(index - 1, Wearable.HAT);
                }
                index = 0;
                ((ArrowButton) buttons.get(RIGHT)).show();
                break;

            case 3:
                if (index != 0) {
                    shoes = Constants.getWearable(index - 1, Wearable.SHOES);
                }
                index = 0;
                ((ArrowButton) buttons.get(LEFT)).hide();
                ((ArrowButton) buttons.get(RIGHT)).hide();
                labelBoxes.get(2).hide();
                labelBoxes.get(1).hide();
                state++;
                createPet();
                return;

            case 4:
                index = 0;
                submit();
                return;

            default:
                return;
        }

        labelBoxes.get(2).show();
        ((ArrowButton) buttons.get(LEFT)).hide();
        labelBoxes.get(1).updateText(labels[state]);

        state++;
    }

    private void createPet() {
        pet = new Pet(name, petType);
        if (hat != null) {
            pet.addWearable(hat);
        }
        if (shoes != null) {
            pet.addWearable(shoes);
        }
    }

    private void submit() {
        int values = Constants.sharedPref.getInt("PetCount", 1);
        Constants.addInt("PetCount", values);

        returnTo = Constants.HOME_SCENE;
    }

    public Pet getResult() {
        return pet;
    }

    private void nextItem() {

        switch (state) {

            case 1: //Pet type picking
                if (index <  Constants.petTypes.size() - 1) {
                    index++;

                    if (index == Constants.petTypes.size() - 1) {
                        ((ArrowButton) buttons.get(RIGHT)).hide();
                    }
                }

                break;

            case 2: //Hat picking
                if (index <  Constants.wearables.get(Wearable.HAT).size()) {
                    index++;

                    if (index == Constants.wearables.get(Wearable.HAT).size()) {
                        ((ArrowButton) buttons.get(RIGHT)).hide();
                    }
                }

                break;

            case 3: //Shoes
                if (index < Constants.wearables.get(Wearable.SHOES).size()) {
                    index++;

                    if (index == Constants.wearables.get(Wearable.SHOES).size()) {
                        ((ArrowButton) buttons.get(RIGHT)).hide();
                    }
                }

                break;
        }

        if (index == 1) {
            ((ArrowButton) buttons.get(LEFT)).show();
            labelBoxes.get(2).hide();
        }
    }

    private void prevItem() {

        if (state > 0) {
            switch (state) {
                case 1: //Pet type picking
                    if (index == Constants.petTypes.size()) {
                        ((ArrowButton) (buttons.get(RIGHT))).show();
                    }

                case 2: //Hat picking
                    if (index == Constants.wearables.get(Wearable.HAT).size()) {
                        ((ArrowButton) (buttons.get(RIGHT))).show();
                    }

                case 3: //Shoes
                    if (index == Constants.wearables.get(Wearable.SHOES).size()) {
                        ((ArrowButton) (buttons.get(RIGHT))).show();
                    }
            }

            if (index > 0) {
                index--;

                if (index == 0) {
                    ((ArrowButton) buttons.get(LEFT)).hide();
                    if (state > 1) {
                        labelBoxes.get(2).show();
                    }
                }
            }
        }
    }

    private boolean EnterCode(){
        int index = 0;
        boolean passed = true;

        for (TextForm textForm: textForms){
            if (!textForm.isValid()){
                helpBoxes.get(index).show();
                labelBoxes.get(1).hide();
                passed = false;
            } else {
                helpBoxes.get(index).hide();
                labelBoxes.get(1).show();
            }
            index++;
        }

        if (!passed || textForms.isEmpty()){
            return false;
        }

        name = textForms.get(0).getString();

        return true;
    }

    private void badPage(){
        returnTo = Constants.BAD_SCENE; // No checks needed
    }

}
