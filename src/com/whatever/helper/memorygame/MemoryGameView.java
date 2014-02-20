package com.whatever.helper.memorygame;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.whatever.helper.R;

/**
 * Created by Igor on 18.02.14.
 */
public class MemoryGameView extends View {

    private Game game;
    private Context context;
    private Loop loop;

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        game.changeParameters(h, w);
    }

    public void inv() {
        invalidate();
    }

    public MemoryGameView(Context context) {
        super(context);
        this.context = context;

        prepareGame();
    }

    public MemoryGameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        prepareGame();

    }

    private void prepareGame() {
        game = new Game(context, 600, 600);
        loop = new Loop(this, game);
        loop.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getContext().getResources().getColor(R.color.helper_memory_game_bg));

        game.render(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos, yPos;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xPos = event.getX();
                yPos = event.getY();
                Log.d("Touch at: ", xPos + " " + yPos);

                game.processClick((int) xPos, (int) yPos);

//                Rect rect = new Rect();
//                int l = game.getPaddingLeft();
//                int r = game.getCurrentFieldSizePixels() + game.getPaddingLeft();
//                int t = game.getPaddingTop();
//                int b = game.getCurrentFieldSizePixels() + game.getPaddingTop();
//                rect.set(l,t,r,b);

//                invalidate(rect);
//                invalidate();
                break;
        }

        return true;
    }
}
