package com.whatever.helper.memorygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.whatever.helper.R;

/**
 * Created by Igor on 19.02.14.
 */
public class Field {
    private Context context;
    private Tile tiles[][];
    private final int fieldSize;
    private int fieldSizePixels;
    private int tileSize;
    private int marginLeft;
    private int marginTop;
    private final int borderSize = 8;
    private boolean map[][];

    private int lastFlipX;
    private int lastFlipY;

    public Field(Context context, final int fieldSize, int fieldSizePixels, int marginLeft, int marginTop, boolean map[][]) {
        this.context = context;
        this.fieldSizePixels = fieldSizePixels;
        this.fieldSize = fieldSize;
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
        tiles = new Tile[fieldSize][fieldSize];
        tileSize = (fieldSizePixels - borderSize * (fieldSize + 1)) / fieldSize;
        this.map = map;

        prepareField();
    }

    public boolean isFlipping() { // Is any bastard flipping now??
        return isFlipping(lastFlipX, lastFlipY);
    }

    public boolean isFlipping(int x, int y) {
        return tiles[y][x].isFlipping();
    }

    public boolean containsCard(int x, int y) {
        return tiles[y][x].containsCard();
    }

    public void flip(int x, int y) {
        tiles[y][x].flip();
        lastFlipX = x;
        lastFlipY = y;
    }

    private void prepareField() {
        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++) {
                if (map[y][x]) {
                    tiles[y][x] = new Tile(context, 0, 0, 0, true);
                } else {
                    tiles[y][x] = new Tile(context, 0, 0, 0, false);
                }
            }
        }
    }

    public void changeParameters(int newFieldSizePixels, int newMarginLeft, int newMarginTop) {
        fieldSizePixels = newFieldSizePixels;
        marginLeft = newMarginLeft;
        marginTop = newMarginTop;
        tileSize = (fieldSizePixels - borderSize * (fieldSize + 1)) / fieldSize;
    }

    public void draw(Canvas canvas) {
        Paint desk = new Paint();
        desk.setColor(context.getResources().getColor(R.color.helper_memory_game_desk));
        canvas.drawRect(marginLeft, marginTop, fieldSizePixels + marginLeft - 1, fieldSizePixels + marginTop - 1, desk);
//
//        for (int i = 1; i < fieldSize; i++) {
//            canvas.drawLine(marginLeft + tileSize*(i-1));
//        }

        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++) {
                tiles[y][x].draw(canvas,
                        (x + 1) * borderSize + x * tileSize + marginLeft,
                        (y + 1) * borderSize + y * tileSize + marginTop, tileSize);
            }
        }

    }
}
