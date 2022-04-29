package com.example.virtualbets;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;

public class SceneManager {

    private static int ACTIVE_SCENE;
    private int lastScene;
    private final ArrayList<Scene> scenes = new ArrayList<>();

    public SceneManager(ArrayList<Bitmap> images, Context context) {
        ACTIVE_SCENE = 0;

        scenes.add(new StartScene(images.get(0)));
        scenes.add(new LoginScene(context, images.get(0)));
        scenes.add(new RegisterScene(images.get(0)));
        scenes.add(new HomeScene(context, images.get(0)));
        scenes.add(new CommunitySetupScene(context, images.get(0)));
        scenes.add(new PetSetupScene(context, images.get(0)));
        scenes.add(new LoginScene(context, images.get(0)));
        scenes.add(new BadScene(images.get(0)));

        Draw.Init();
    }

    public void update(){
        int result = scenes.get(ACTIVE_SCENE).update();

        if (result > -1){

            if ( ( result == Constants.BAD_SCENE && ACTIVE_SCENE == Constants.BAD_SCENE ) || result == Constants.LAST_SCENE ) {
                setScene(lastScene, false);
            } else {
                setScene(result, true);
            }
            if ( result == Constants.START_SCENE ) {
                Constants.clear();
            }
        }

        for (Community community: Constants.communities) {
            for (Event e: community.getEvents()) {
                e.update();
            }
        }
    }

    public void receiveTouch(MotionEvent event){
        if (Constants.focusedEvent == null) {
            scenes.get(ACTIVE_SCENE).receiveTouch(event);
        } else {
            Constants.focusedEvent.receiveTouch(event);
        }
    }

    public void draw(Canvas canvas){
        scenes.get(ACTIVE_SCENE).draw(canvas);

        if (Constants.focusedEvent != null) {
            Constants.focusedEvent.draw(canvas);
        }
    }

    public void setScene(int scene, boolean goodScene){
        if (goodScene) {
            lastScene = ACTIVE_SCENE;
        }

        ACTIVE_SCENE = scene;
    }
}