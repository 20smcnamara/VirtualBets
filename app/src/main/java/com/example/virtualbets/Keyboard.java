package com.example.virtualbets;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

public class Keyboard {

    private int keyWidth;
    private int keyHeight;
    private int outlinePX;
    private int baseY;
    private int spacingX;
    private int spacingY;
    private boolean good = false;
    private boolean submitted = true;
    private boolean isHidden = true;
    private StringBuilder builder = new StringBuilder();
    private final static String SYMBOLS = "1234567890qwertyuiopasdfghjkl;zxcvbnm!&=. 11111111111111111111111111111111111111111111111111111111111111";
    private int[] rowLevels = new int[5];
    private int[] colLevels = new int[12];
    private final Integer[] backColor = new Integer[] {200, 200, 200, 200};
    private static final Integer[] keyColor = new Integer[] {255, 0, 0, 0};
    private static final Integer[] keyBackgroundColor = new Integer[] {255, 255, 255, 255};
    private static final ArrayList<Key> keys = new ArrayList<>();
    private final Rect backgroundRect;

    private static class Key {

        private final char c;
        private final Rect r;
        private final Rect o;

        private Key(char c, Rect r, Rect o){
            this.c = c;
            this.r = r;
            this.o = o;
        }

        private char handleMotionEvent(MotionEvent event){
            if (r.left < event.getX() && event.getX() < r.right &&
                    r.top < event.getY() && r.bottom > event.getY()) {
                return c;
            }
            return 0;
        }

        private void draw(Canvas canvas){
            Draw.drawRoundRect(canvas, o, keyColor);
            Draw.drawRoundRect(canvas, r, keyBackgroundColor);
            if (c == 'G'){
                Draw.drawText(canvas, r, keyColor, "Enter");
            } else if (c == ' '){
                Draw.drawText(canvas, r, keyColor, "Space");
            } else if (c == 'B'){
                Draw.drawText(canvas, r, keyColor, "Back");
            } else {
                Draw.drawText(canvas, r, keyColor, String.valueOf(c));
            }
        }
    }

    public Keyboard() {
        int index = 0;

        keyWidth = Constants.width / 16;
        keyHeight = Constants.height / 18;
        outlinePX = Constants.width / 128;
        spacingX = (int) (keyWidth * 0.5);
        spacingY = (int) (keyHeight * 0.5);
        baseY = Constants.height / 2;

        outsideLoop:
        for (int i = 0; i < 5; i++) {
            rowLevels[i] = baseY + (keyHeight + spacingY) * (i + 1);
            for (int j = 0; j < 10; j++){
                if (i == 0){
                    colLevels[j] = (keyWidth + spacingX) * (j + 1);
                }
                if (i == 4){
                    keys.add(new Key(' ',
                            new Rect((int) (keyWidth * 5.5), rowLevels[i] - keyHeight / 2,
                                    keyWidth * 11, rowLevels[i] + keyHeight / 2),
                            new Rect((int) ((keyWidth * 5.5) - outlinePX), rowLevels[i] - keyHeight / 2 - outlinePX,
                                    (keyWidth * 11) + outlinePX, rowLevels[i] + keyHeight / 2 + outlinePX)));

                    keys.add(new Key('G',
                            new Rect((int) (keyWidth * 11.5), rowLevels[i] - keyHeight / 2,
                                    (int) (keyWidth * 15.5), rowLevels[i] + keyHeight / 2),
                            new Rect( (int) (keyWidth * 11.5) - outlinePX, rowLevels[i] - keyHeight / 2 - outlinePX,
                                    (int) (keyWidth * 15.5) + outlinePX, rowLevels[i] + keyHeight / 2 + outlinePX)));

                    keys.add(new Key('B',
                            new Rect((keyWidth), rowLevels[i] - keyHeight / 2,
                                     (keyWidth * 5), rowLevels[i] + keyHeight / 2),
                            new Rect((keyWidth) - outlinePX, rowLevels[i] - keyHeight / 2 - outlinePX,
                                    (keyWidth * 5) + outlinePX, rowLevels[i] + keyHeight / 2 + outlinePX)));
                    break outsideLoop;
                } else{
                    keys.add(new Key(SYMBOLS.charAt(index),
                            new Rect(colLevels[j] - keyWidth / 2, rowLevels[i] - keyHeight /2,
                                    colLevels[j] + keyWidth / 2, rowLevels[i] + keyHeight / 2),
                            new Rect(colLevels[j] - keyWidth / 2 - outlinePX, rowLevels[i] - keyHeight /2 - outlinePX,
                                    colLevels[j] + keyWidth / 2 + outlinePX, rowLevels[i] + keyHeight / 2 + outlinePX)));
                }
                index++;
            }
        }

        backgroundRect = new Rect(0, baseY, Constants.width, Constants.height);
    }

    public void draw(Canvas canvas){
        if (!isHidden){
            Draw.drawRect(canvas, backgroundRect, backColor);

            for (Key key: keys){
                key.draw(canvas);
            }
        }
    }

    public void focus() {
        isHidden = false;
        good = false;
        submitted = false;
    }

    public void resetAndFocus() {
        builder = new StringBuilder();
        focus();
    }

    public void Submit() {
        isHidden = true;
        submitted = true;
        good = false;
    }

    public String getResult(){
        return builder.toString();
    }

    public boolean isSubmitted(){
        return submitted;
    }

    public boolean isHidden(){
        return isHidden;
    }

    public void handleMotionEvent(MotionEvent event){ //TODO not loop through all of them
        if (event.getAction() == MotionEvent.ACTION_UP && good) {
            if (event.getY() < baseY){
                Submit();
            }
            for (Key key : keys) {
                char response = key.handleMotionEvent(event);
                if (response != 0) {
                    if (response == 'G') {
                        Submit();
                    } else if (response == 'B') {
                        if (builder.length() > 0) {
                            builder.deleteCharAt(builder.length() - 1);
                        }
                    } else {
                        builder.append(response);
                    }
                    break;
                }
            }
        }
        if (!good) {
            good = true;
        }
    }
}
