package com.example.virtualbets;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.util.ArrayList;


public class MainController extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private SceneManager manager;

    public MainController(Context context){
        super(context);
        getHolder().addCallback(this);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        SharedPreferences prefs = context.getSharedPreferences("Data", 0);
        Constants.setUp(prefs, width, height);

        //Load images
        ArrayList<Bitmap> LOB = new ArrayList<>();

        try {
            Bitmap startImage = BitmapFactory.decodeResource(getResources(), R.drawable.startscreen);
            startImage = Bitmap.createScaledBitmap(startImage, Constants.width, Constants.height, false);
            LOB.add(startImage);

        } catch (Exception e){
            e.printStackTrace();
        }

        manager = new SceneManager(LOB);
        thread = new MainThread(getHolder(),this);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        manager.receiveTouch(event);
        return true;
    }

    public void update(){
        manager.update();
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        manager.draw(canvas);
    }

    public void setScene(int scene){
        manager.setScene(scene);
    }
}