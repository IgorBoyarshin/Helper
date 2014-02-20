package com.whatever.helper.memorygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import com.whatever.helper.R;

import java.util.Random;

/**
 * Created by Igor on 19.02.14.
 */
public class Game {
    private Context context;
    private int h, w;
    private final int paddingSmall;
    private int paddingLeft;
    private int paddingTop;

    private Field field;
    private int currentFieldSize;
    private int currentFieldSizePixels;

    private int currentState;
    private static final int STATE_OFF = 0;
    private static final int STATE_LOAD_NEXT_LEVEL = 1;
    private static final int STATE_PAUSE_BEFORE_LEVEL = 2;
    private static final int STATE_SHOWING = 3;
    private static final int STATE_GAME = 4;
    private static final int STATE_RESULTS_SHOWING = 5;

    private boolean lost;
    private boolean won;

    private long delayStart;
    private final long delayBeforeLevel;
    private final long delayShowing;
    private final long delayResultsShowing;

    private boolean touchEnabled;

    private final int map1Size = 5;
    private final int map1Amount = 13;
    private final boolean map1[][];

    private final int map2Size = 5;
    private final int map2Amount = 8;
    private final boolean map2[][] = new boolean[map2Size][map2Size];

    private int curLevel;
    private boolean currentMap[][];
    private int currentMapAmount;
    private boolean playerCanPressThisTile[][];
    private int currentMapGuesses;
    private int currentMapCorrect;


    public Game(Context context, int windowHeight, int windowWidth) {
        this.context = context;
        h = windowHeight;
        w = windowWidth;
        paddingSmall = 50;
        delayResultsShowing = 3000;
        delayBeforeLevel = 1000;
        delayShowing = 3000;
        delayStart = -1;
        curLevel = 0;
        map1 = new boolean[map1Size][map1Size];


        // TODO: make levels generator

        prepareLevels();

        if (h >= w) {
            currentFieldSizePixels = w - paddingSmall * 2;
            paddingLeft = paddingSmall;
            paddingTop = (h - currentFieldSizePixels) / 2;
        } else {
            currentFieldSizePixels = h - paddingSmall * 2;
            paddingTop = paddingSmall;
            paddingLeft = (w - currentFieldSizePixels) / 2;
        }

//        field = new Field(context, 4, 100, paddingLeft, paddingTop, map1); // WRONG
    }

    private void prepareLevels() {
        boolean t = true;
        for (int i = 0; i < map1Size; i++) {
            for (int j = 0; j < map1Size; j++) {
                map1[i][j] = t;
                t = !t;
            }
        }
        currentState = STATE_OFF;
    }

    private void flipAll() {
        for (int i = 0; i < currentFieldSize; i++) {
            for (int j = 0; j < currentFieldSize; j++) {
                field.flip(i, j);
            }
        }
    }

    private boolean[][] createNewLevel(int size, int amount) {
        boolean map[][] = new boolean[size][size];

        int history[][] = new int[amount][2];
        int curH = 0;
        boolean matched = false;

        Random r = new Random();
        int i = 0;
        while (i < amount) {
            i++;
            int x = Math.abs(r.nextInt()) % size;
            int y = Math.abs(r.nextInt()) % size;

            int j = 0;
            while (j < curH) {
                if ((history[j][0] == x) && (history[j][1] == y)) {
                    matched = true;
                }
                j++;
            }

            if (matched) {
                i--;
                continue;
            } else {
                map[x][y] = true;
                history[i - 1][0] = x;
                history[i - 1][1] = y;
                curH++;
            }
            matched = false;
        }

        return map;
    }

    private void loadNextLevel() {
        curLevel++;

        currentMap = createNewLevel(5, 5);
        currentMapAmount = 5;
        currentFieldSize = 5;

        playerCanPressThisTile = new boolean[currentFieldSize][currentFieldSize];
        for (int i = 0; i < currentFieldSize; i++) {
            for (int j = 0; j < currentFieldSize; j++) {
                playerCanPressThisTile[i][j] = true;
            }
        }
        currentMapGuesses = 0;
        currentMapCorrect = 0;
        won = false;
        lost = false;
        field = new Field(context, currentFieldSize, currentFieldSizePixels, paddingLeft, paddingTop, currentMap);
    }

    private void reloadLevel() {
        for (int i = 0; i < currentFieldSize; i++) {
            for (int j = 0; j < currentFieldSize; j++) {
                playerCanPressThisTile[i][j] = true;
            }
        }
        currentMapGuesses = 0;
        currentMapCorrect = 0;
        won = false;
        lost = false;
        field = new Field(context, currentFieldSize, currentFieldSizePixels, paddingLeft, paddingTop, currentMap);
    }

    private void update() {
        switch (currentState) {
            case STATE_OFF:
                currentState++;
                break;
            case STATE_LOAD_NEXT_LEVEL:
                if (lost) {
                    reloadLevel();
                } else {
                    loadNextLevel();
                }
                currentState++;
                break;
            case STATE_PAUSE_BEFORE_LEVEL:
                if (delayStart == -1) { // Code for not started
                    delayStart = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - delayStart > delayBeforeLevel) {
                    currentState++;
                    delayStart = -1; // For later purposes
                }
                break;
            case STATE_SHOWING:
                if (delayStart == -1) {
                    flipAll();
                    delayStart = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - delayStart > delayShowing) {
                    if (delayStart != -2) {
                        flipAll();
                        delayStart = -2;
                    }
                    if (!field.isFlipping()) { // When upper flipping finished
                        delayStart = -1; // For later purposes
                        currentState++;

                        touchEnabled = true; // TODO: md move to the game state
                    }
                }
                break;
            case STATE_GAME:
                if (currentMapGuesses == currentMapAmount) {
                    touchEnabled = false;
                    if (!field.isFlipping()) {
                        if (currentMapCorrect == currentMapGuesses) {
                            won = true;
                            currentState++;
                        } else {
                            if (delayStart == -3) {
                                lost = true;
                                currentState++;
                                delayStart = -1; // For later purposes
                            } else if (delayStart == -1) {
                                delayStart = -3;
//                                flipLeftTiles();
                            }
                        }
                    }
                }
                break;
            case STATE_RESULTS_SHOWING:
                if (delayStart == -1) {
                    delayStart = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - delayStart > delayResultsShowing) {
                    delayStart = -1; // For later purposes
                    currentState = STATE_LOAD_NEXT_LEVEL;
                }
                break;
        }
    }

    private void flipLeftTiles() {
//        field.flip(0, 0);

        for (int i = 0; i < currentFieldSize; i++) {
            for (int j = 0; j < currentFieldSize; j++) {
                if ((currentMap[i][j]) && (playerCanPressThisTile[i][j])) {
                    field.flip(j, i);
                }
            }
        }
    }

    private void renderLostScreen(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(context.getResources().getColor(R.color.helper_memory_game_lost));
        canvas.drawRect(paddingLeft + 100, paddingTop + 100,
                paddingLeft + currentFieldSizePixels - 100, paddingTop + currentFieldSizePixels - 100, p);
    }

    private void renderWonScreen(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(context.getResources().getColor(R.color.helper_memory_game_won));
        canvas.drawRect(paddingLeft + 150, paddingTop + 150,
                paddingLeft + currentFieldSizePixels - 150, paddingTop + currentFieldSizePixels - 150, p);
    }

    public void render(Canvas canvas) {
        update();
        // Background
        Paint bg = new Paint();
        bg.setColor(context.getResources().getColor(R.color.helper_memory_game_bg));
        canvas.drawRect(0, 0, w, h, bg);

        // Field
        if (field != null) {
            field.draw(canvas);
        }

        // Displaying the result
        if (lost) {
            renderLostScreen(canvas);
        } else if (won) {
            renderWonScreen(canvas);
        }
    }

    public void processClick(int x, int y) {
        Log.d("pppp", "" + withinField(x, y));
        Log.d("tttt", "" + touchEnabled);

        if (touchEnabled) {
            if (withinField(x, y)) {
                int xWithinTile = (x - paddingLeft) % (currentFieldSizePixels / currentFieldSize);
                int yWithinTile = (y - paddingTop) % (currentFieldSizePixels / currentFieldSize);

                int tileX = (x - xWithinTile - paddingLeft) / (currentFieldSizePixels / currentFieldSize);
                int tileY = (y - yWithinTile - paddingTop) / (currentFieldSizePixels / currentFieldSize);

                Log.d("[Helper Memory Game] Clicked at tile: ", " " + tileX + " " + tileY); // index of the tile

                if (indexWithinFieldBounds(tileX, tileY)) { // Check just in case out calculation were wrong
                    if (currentMapGuesses < currentMapAmount) {
                        if (playerCanPressThisTile[tileX][tileY]) {
                            field.flip(tileX, tileY);
                            currentMapGuesses++;
                            playerCanPressThisTile[tileX][tileY] = false;

                            if (field.containsCard(tileX, tileY)) {
                                currentMapCorrect++;

                            }
                        }
                    }
                }
            }
        }
    }

    private boolean indexWithinFieldBounds(int x, int y) {
        return ((x < currentFieldSize) && (y < currentFieldSize) && (x >= 0) && (y >= 0));
    }

    private boolean withinField(int x, int y) {
        return ((x > paddingLeft) && (x < paddingLeft + currentFieldSizePixels)
                && (y > paddingTop) && (y < paddingTop + currentFieldSizePixels));
    }

    public void changeParameters(int newH, int newW) {
        h = newH;
        w = newW;

        if (h >= w) {
            currentFieldSizePixels = w - paddingSmall * 2;
            paddingLeft = paddingSmall;
            paddingTop = (h - currentFieldSizePixels) / 2;
        } else {
            currentFieldSizePixels = h - paddingSmall * 2;
            paddingTop = paddingSmall;
            paddingLeft = (w - currentFieldSizePixels) / 2;
        }

        if (field != null) {
            field.changeParameters(currentFieldSizePixels, paddingLeft, paddingTop);
        }
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public int getCurrentFieldSizePixels() {
        return currentFieldSizePixels;
    }
}
