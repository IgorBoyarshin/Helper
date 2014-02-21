package com.whatever.helper.memorygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.whatever.helper.R;

/**
 * Created by Igor on 19.02.14.
 */
public class Tile {
    private Context context;

    //private int x, y;
    //private int size;
    private final boolean doesContain;

    private boolean flippedFaceUp;
    private int flipProgress;
    private boolean flippedOverHalf;
    private final int flipSpeed;
    private boolean flipping;

    public Tile(Context context, int x, int y, int size, boolean filled) {
        doesContain = filled;
        this.context = context;
        //this.x = x;
        //this.y = y;
        //this.size = size;

        flipping = false;
        flipProgress = 0;
        flipSpeed = 6;
        flippedFaceUp = false;
        flippedOverHalf = false;
    }

    public void changeParameters(int newX, int newY, int newSize) {
        //x = newX;
        //y = newY;
        //size = newSize;
    }

    public boolean containsCard() {
        return doesContain;
    }

    public boolean isFlipping() {
        return flipping;
    }

    public void draw(Canvas canvas, int x, int y, int size) {
        Paint paint = new Paint();

        if (flippedFaceUp) {
            if (doesContain) {
                paint.setColor(context.getResources().getColor(R.color.helper_memory_game_card));
            } else {
                paint.setColor(context.getResources().getColor(R.color.helper_memory_game_no_card));
            }
        } else {
            paint.setColor(context.getResources().getColor(R.color.helper_memory_game_tile));
        }

        if (flipping) {
            flipProgress += flipSpeed;

            if (flipProgress > size) {
                flipping = false;
                canvas.drawRect(x, y, x + size, y + size, paint);
            } else if (flipProgress >= size/2) {
                if (!flippedOverHalf) {
                    flippedFaceUp = !flippedFaceUp;
                    flippedOverHalf = true;
                }

                canvas.drawRect(x + size - flipProgress, y, x + flipProgress, y + size, paint);
            } else {
                canvas.drawRect(x + flipProgress, y, x + size - flipProgress, y + size, paint);
            }
        } else {
            canvas.drawRect(x, y, x + size, y + size, paint);
        }


    }

    public void flip() {
        flipping = true;
        flipProgress = 0;
        flippedOverHalf = false;
    }
}
