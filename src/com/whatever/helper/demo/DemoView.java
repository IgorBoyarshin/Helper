package com.whatever.helper.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DemoView extends View {

    Bitmap bitmap;
    private final long timeStart = System.nanoTime();
    private final long timePlaying = 1*1000*1000*1000;
    private int x = 0;

    public DemoView(Context context) {
        super(context);
    }

    public DemoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void update() {
        x+=1;
    }

    private void render() {
        while (System.nanoTime() - timeStart < timePlaying) {
            update();
            invalidate();
        }
    }

    private void drawFrame(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);

        canvas.drawRect(x, 100, 1+20, 200, paint);
    }

    private void generateFrames() {

    }

    private void drawNextFrame(Canvas canvas) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawFrame(canvas);

//        drawNextFrame(canvas);
    }
}