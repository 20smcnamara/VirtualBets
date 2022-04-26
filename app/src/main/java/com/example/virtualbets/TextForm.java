package com.example.virtualbets;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.VolumeShaper;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class TextForm {

    private boolean good = false;
    private Boolean selected = false;
    private Boolean valid = false;
    private Boolean required = true;
    private Boolean secure = false;
    private boolean duel = false;

    private final int id;
    private final static int delay = 30;
    private int minDuel = 0;
    private int maxDuel = 255;
    private int min = 31;
    private int max = 127;
    private int maxLength = 255;
    private int minLength = 0;
    public int issues = 0;
    private String string = "";
    private String secureString = "";
    private String requisites = "";
    private StringBuilder secBuilder = new StringBuilder();
    private final HashSet<Integer> blockedInts = new HashSet<>();
    private final Rect rect;

    public TextForm(int id, Rect rect){
        this.id = id;
        this.rect = rect;
    }

    public TextForm(int id, Rect rect, HashMap<String, Object> args){
        this.id = id;
        this.rect = rect;

        if (args.containsKey("Required")){
            this.required = (Boolean) args.get("Required");
        }

        if (args.containsKey("Requirements")){
            requisites = (String) args.get("Requirements");
            parseRequirements();
        }

        if (args.containsKey("secure")){
            secure = (Boolean) args.get("secure");
        }
    }

    public void setString(String string) {
        if (string.length() > 0) {
            this.string = string;

            secBuilder = new StringBuilder();
            if (string.length() > 1) {
                for (int i = 0; i < string.length() - 1; i++) {
                    secBuilder.append("*");
                }
            }
            secBuilder.append(string.substring(string.length() - 1));

            secureString = secBuilder.toString();
        } else {
            this.string = "";
            this.secureString = "";
        }
    }

    public void fill(String string){
        setString(string);
        selected = false;
        good = false;
    }

    private boolean charValid(char c, int min, int max){
        return (int) c > min && (int) c < max; //Ascii value range is valid
    }

    private boolean blocked(char c, Set<Integer> blocked){
        return blocked.contains((int) c);
    }

    private void parseRequirements(){

        if (requisites.contains("noSpace")){
            min = 32;
        }

        if (requisites.contains("LETTERS")) {
            min = 64;
            max = 91;
            minDuel = 96;
            maxDuel = 123;
            duel = true;
        }

        if (requisites.contains("NUMS")) {
            min = 47;
            max = 58;
        }

        if (requisites.contains("LEN")) {
            int index = requisites.indexOf("LEN");
            StringBuilder sb = new StringBuilder();
            while (requisites.charAt(index + "LEN".length()) != '-') {
                sb.append(requisites.charAt(index + "LEN".length()));
                index++;
            }
            maxLength = Integer.parseInt(sb.toString());
        }

        if (requisites.contains("len")) {
            int index = requisites.indexOf("len");
            StringBuilder sb = new StringBuilder();
            while (requisites.charAt(index + "len".length()) != '-') {
                sb.append(requisites.charAt(index + "len".length()));
                index++;
            }
            minLength = Integer.parseInt(sb.toString());
        }

        int blocked = requisites.indexOf("blocked") ;
        if (blocked != -1){
            blocked += "blocked".length();
            int range = 1;
            String numBlocked = requisites.substring(blocked, range);
            while (!numBlocked.contains("-")){
                numBlocked = requisites.substring(blocked, ++range);
            }

            int numBlockedInt = Integer.parseInt(numBlocked);
            int index = blocked + numBlocked.length() + 1;
            StringBuilder currVal = new StringBuilder();

            while (numBlockedInt > 0){
                char currLetter = requisites.charAt(index);
                if (currLetter == '-') {
                    Integer newNum = Integer.parseInt(currVal.toString());
                    currVal = new StringBuilder();
                    blockedInts.add(newNum);
                    numBlockedInt -= 1;
                } else {
                    currVal.append(String.valueOf(currLetter));
                }
                index++;
            }
        }
    }

    private void updateValid(){
        //Begin count of how many issues in box
        if ((!hasEntry() && required) || (minLength > string.length()) || (maxLength < string.length())){
            issues = 1;
            valid = false;
            return;
        }

        issues = 0;
        if (requisites != null && !requisites.isEmpty()) {
            for (int i = 0; i < string.length(); i++){
                char curr = string.charAt(i);
                if ((duel && !charValid(curr, minDuel, maxDuel)) ||
                        !charValid(curr, min, max) ||
                        blocked(curr, blockedInts)){
                    issues++;
                }
            }
        }

        valid = issues <= 0;
    }

    public boolean hasEntry(){
        return string.length() > 0;
    }

    public Boolean isValid() {
        updateValid();
        return valid;
    }

    public void reset(){
        string = "";
    }

    public int handleTouch(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (good && !selected) {
                if (rect.left < event.getX() && event.getX() < rect.right &&
                        rect.top < event.getY() && rect.bottom > event.getY()) {
                    selected = true;
                    Constants.myKeyboard.resetAndFocus();
                    return id;
                }
            }
        } else if (!good) {
            good = true;
        }
        return -1;
    }

    public void draw(Canvas canvas){
        Draw.drawRoundRect(canvas, rect, Draw.WHITE);

        if (!secure){
            Draw.drawText(canvas, rect, Draw.BLACK, string);
        } else {
            Draw.drawText(canvas, rect, Draw.BLACK, secureString);
        }
    }

    public int getId(){
        return id;
    }
}
