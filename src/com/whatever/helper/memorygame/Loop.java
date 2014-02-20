package com.whatever.helper.memorygame;

import android.view.View;

/**
 * Created by Igor on 19.02.14.
 */
public class Loop extends Thread {

    private View view;
    private Game game;

    public Loop(View view, Game game) {
        this.view = view;
        this.game = game;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            Log.d("-----------", "Here");


            int l = game.getPaddingLeft();
            int r = game.getCurrentFieldSizePixels() + game.getPaddingLeft();
            int t = game.getPaddingTop();
            int b = game.getCurrentFieldSizePixels() + game.getPaddingTop();

            view.postInvalidate(l,t,r,b);
        }
    }
}
