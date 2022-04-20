package com.example.virtualbets;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends  Thread {
    public static final int MAX_FPS = 60;
    private double averageFPS;
    private final SurfaceHolder surfaceHolder;
    private final MainController controller;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, MainController mainController) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.controller = mainController;
    }

    public void run() {
        try {
            int frameCount = 0;
            double startTime;
            double loopTimeMillis;
            double waitTimeToHitTarget;
            double totalTime = 0; // Seconds
            double targetTime = 1000.0 / MAX_FPS;
            while (running) {

                startTime = System.nanoTime();
                canvas = null;

                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        this.controller.update();
                        this.controller.draw(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                loopTimeMillis = (System.nanoTime() - startTime) / Constants.NanoToMilli;
                waitTimeToHitTarget = targetTime - loopTimeMillis;

                try {
                    if (waitTimeToHitTarget > 0) {
                        sleep((long) waitTimeToHitTarget);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                totalTime += (System.nanoTime() - startTime) / Constants.NanoToBase;
                frameCount++;

                if (frameCount == MAX_FPS) {
                    averageFPS = MAX_FPS / totalTime;
                    if ( (int) averageFPS < MAX_FPS / 2 ){
                        System.out.println(averageFPS +  " : " + totalTime);
                    }
                    frameCount = 0;
                    totalTime = 0;
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}