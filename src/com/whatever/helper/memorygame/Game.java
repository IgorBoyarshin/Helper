package com.whatever.helper.memorygame;

import android.content.Context;
import android.graphics.*;
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
    private int currentMapSize;
    private int currentFieldSizePixels;

    private int currentState;
    private static final int STATE_OFF = 0;
    private static final int STATE_LOAD_NEXT_LEVEL = 1;
    private static final int STATE_PAUSE_BEFORE_LEVEL = 2;
    private static final int STATE_SHOWING = 3;
    private static final int STATE_GAME = 4;
    private static final int STATE_RESULTS_SHOWING = 5;

    private final int levels[][];
    private final int numOfLevels;
    private final int maxMapSize;
    private int winsInARow;

    private boolean lost;
    private boolean won;

    private long delayStart;
    private final long delayBeforeLevel;
    private final long delayShowing;
    private final long delayResultsShowing;

    private boolean touchEnabled;

    private int curLevel;
    private int curDifficultyLevel;
    private boolean currentMap[][];
    private int currentMapAmount;
    private boolean playerCanPressThisTile[][];
    private int currentMapGuesses;
    private int currentMapCorrect;

    private final float VERSION;


    private Bitmap richard;

    public Game(Context context, int windowHeight, int windowWidth) {
        VERSION = 1.1f;

        richard = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.richard);

        this.context = context;
        h = windowHeight;
        w = windowWidth;
        paddingSmall = 30;
        delayResultsShowing = 5000;
        delayBeforeLevel = 1000;
        delayShowing = 3000;
        delayStart = -1;
        curDifficultyLevel = 0;
        curLevel = 0;
        winsInARow = 0;
        currentState = STATE_OFF;
        field = null;
        lost = false;
        won = false;
        touchEnabled = false;

        numOfLevels = 12;
        maxMapSize = 7;
        levels = new int[numOfLevels][2];
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
        int size = 4;
        int amount = 4;
        boolean trigger = true;

        for (int i = 0; i < numOfLevels; i++) {
            levels[i][0] = size;
            levels[i][1] = amount;

            amount += 1;
            if (trigger) {
                if (size < maxMapSize) {
                    size += 1;
                }
            }

            trigger = !trigger;
        }
    }

    private void update() {
        switch (currentState) {
            case STATE_OFF:
                currentState++;
                // See processClick()
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

                        touchEnabled = true; //  MB MOVE TO THE NEXT STATE..
                    }
                }
                break;
            case STATE_GAME:
                if (currentMapGuesses == currentMapAmount) {
                    touchEnabled = false;
                    if (!field.isFlipping()) {
                        if (currentMapCorrect == currentMapGuesses) {
                            won = true;
                            winsInARow++;
                            currentState++;
                        } else {
                            if (delayStart == -3) {
                                lost = true;
                                winsInARow = 0;
                                currentState++;
                                delayStart = -1; // For later purposes
                            } else if (delayStart == -1) {
                                delayStart = -3;
                                flipLeftTiles();
                            }
                        }
                    }
                }
                break;
            case STATE_RESULTS_SHOWING:
                if (delayStart == -1) {
//                    showMatrix();
                    delayStart = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - delayStart > delayResultsShowing) {
                    delayStart = -1; // For later purposes
                    currentState = STATE_LOAD_NEXT_LEVEL;
                }
                break;
        }
    }

    private void flipAll() {
        for (int i = 0; i < currentMapSize; i++) {
            for (int j = 0; j < currentMapSize; j++) {
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
            int x = Math.abs(r.nextInt(size)); // play around
            int y = Math.abs(r.nextInt(size));

            int j = 0;
            while (j < curH) {
                if ((history[j][0] == x) && (history[j][1] == y)) {
                    matched = true;
                }
                j++;
            }

            if (matched) {
                i--;
                matched = false;
//                continue;
            } else {
                map[x][y] = true;
                history[i - 1][0] = x;
                history[i - 1][1] = y;
                curH++;
            }
        }

        return map;
    }

    private void loadNextLevel() {
        currentMapAmount = levels[curDifficultyLevel][1];
        currentMapSize = levels[curDifficultyLevel][0];
        currentMap = createNewLevel(currentMapSize, currentMapAmount);
        if (curDifficultyLevel < numOfLevels - 1) {
            curDifficultyLevel++;
        }
        curLevel++;

        playerCanPressThisTile = new boolean[currentMapSize][currentMapSize];
        for (int i = 0; i < currentMapSize; i++) {
            for (int j = 0; j < currentMapSize; j++) {
                playerCanPressThisTile[i][j] = true;
            }
        }
        currentMapGuesses = 0;
        currentMapCorrect = 0;
        won = false;
        lost = false;
        field = new Field(context, currentMapSize, currentFieldSizePixels, paddingLeft, paddingTop, currentMap);
    }

    private void reloadLevel() {
        curLevel++;
        for (int i = 0; i < currentMapSize; i++) {
            for (int j = 0; j < currentMapSize; j++) {
                playerCanPressThisTile[i][j] = true;
            }
        }
        currentMap = createNewLevel(currentMapSize, currentMapAmount);
        currentMapGuesses = 0;
        currentMapCorrect = 0;
        won = false;
        lost = false;
        // TODO: Mb just implement clearing current field
        field = new Field(context, currentMapSize, currentFieldSizePixels, paddingLeft, paddingTop, currentMap);
    }

    private void showMatrix() {
        for (int i = 0; i < currentMapSize; i++) {
            String s = "";
            for (int j = 0; j < currentMapSize; j++) {
                if (currentMap[i][j]) {
                    s = s + " 1";
                } else {
                    s = s + " 0";
                }
            }
            Log.d("Matrix Map:", s);
        }

        for (int i = 0; i < currentMapSize; i++) {
            String s = "";
            for (int j = 0; j < currentMapSize; j++) {
                if (playerCanPressThisTile[i][j]) {
                    s = s + " 1";
                } else {
                    s = s + " 0";
                }
            }
            Log.d("Matrix Press:", s);
        }
    }

    private void flipLeftTiles() {
        for (int i = 0; i < currentMapSize; i++) {
            for (int j = 0; j < currentMapSize; j++) {
                if ((currentMap[i][j]) && (playerCanPressThisTile[i][j])) {
                    field.flip(j, i);
                }
            }
        }
    }

    private void renderWelcomeScreen(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(context.getResources().getColor(R.color.helper_memory_game_welcome));

        canvas.drawRect(paddingLeft, paddingTop,
                paddingLeft + currentFieldSizePixels, paddingTop + currentFieldSizePixels, p);

        Paint text = new Paint();
        text.setColor(Color.BLACK);
        text.setTextSize(80.0f);

        canvas.drawText("Welcome to the", paddingLeft + 200, paddingTop + 180, text);
        canvas.drawText("Memory Game v" + VERSION, paddingLeft + 150, paddingTop + 270, text);
        canvas.drawText("Tap to start the game!", paddingLeft + 100, paddingTop + 550, text);
    }

    private void renderLostScreen(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(context.getResources().getColor(R.color.helper_memory_game_lost_e));
//        p.setColor(Color.WHITE);

        canvas.drawRect(paddingLeft, paddingTop,
                paddingLeft + currentFieldSizePixels, paddingTop + currentFieldSizePixels, p);

        Paint text = new Paint();
//        text.setColor(Color.WHITE);
        text.setColor(Color.RED);
        text.setTextSize(80.0f);
        canvas.drawText("Try again! You can do it!", paddingLeft + 60, paddingTop + 80, text);
        canvas.drawText("Your difficulty is " + curDifficultyLevel + "!", paddingLeft + 10, paddingTop + 210, text);
        canvas.drawText("You played " + curLevel + " levels!", paddingLeft + 10, paddingTop + 300, text);
//        canvas.drawText("You won " + winsInARow + " games in a row!", paddingLeft + 10, paddingTop + 250, text);

//        canvas.drawBitmap(richard, paddingLeft + currentFieldSizePixels/2 - 80, paddingTop + currentFieldSizePixels - 200, null);
    }

    private void renderWonScreen(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(context.getResources().getColor(R.color.helper_memory_game_won));
//        canvas.drawRect(paddingLeft + 150, paddingTop + 150,
//                paddingLeft + currentFieldSizePixels - 150, paddingTop + currentFieldSizePixels - 150, p);
        canvas.drawRect(paddingLeft, paddingTop,
                paddingLeft + currentFieldSizePixels, paddingTop + currentFieldSizePixels, p);

        Paint text = new Paint();
        text.setColor(Color.BLACK);
        text.setTextSize(80.0f);
        canvas.drawText("You won! Amazing play!", paddingLeft + 90, paddingTop + 80, text);
        canvas.drawText("Your difficulty is " + curDifficultyLevel + "!", paddingLeft + 10, paddingTop + 210, text);
        canvas.drawText("You played " + curLevel + " levels!", paddingLeft + 10, paddingTop + 300, text);
        canvas.drawText("You won " + winsInARow + " games in a row!", paddingLeft + 10, paddingTop + 390, text);
    }

    public void render(Canvas canvas) {
        update();
        // Background
        Paint bg = new Paint();
        bg.setColor(context.getResources().getColor(R.color.helper_memory_game_bg));
        canvas.drawRect(0, 0, w, h, bg);

        // Richard

        canvas.drawBitmap(richard, 50, 50, null);
        Paint text = new Paint();
        text.setColor(Color.BLACK);
        text.setTextSize(40.0f);
        canvas.drawText("May the force", 50, 220, text);
        canvas.drawText("Be with you!", 50, 270, text);

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

        if (currentState == STATE_OFF) {
//            renderWelcomeScreen(canvas);
        }
    }

    public void processClick(int x, int y) {
//        if (currentState == STATE_OFF) {
//            currentState++;
//        }
        if (touchEnabled) {
            if (withinField(x, y)) {
                int xWithinTile = (x - paddingLeft) % (currentFieldSizePixels / currentMapSize);
                int yWithinTile = (y - paddingTop) % (currentFieldSizePixels / currentMapSize);

                int tileX = (x - xWithinTile - paddingLeft) / (currentFieldSizePixels / currentMapSize);
                int tileY = (y - yWithinTile - paddingTop) / (currentFieldSizePixels / currentMapSize);

                Log.d("[Helper Memory Game] Clicked at tile: ", " " + tileX + " " + tileY); // index of the tile

                if (indexWithinFieldBounds(tileX, tileY)) { // Check just in case out calculation were wrong
                    if (currentMapGuesses < currentMapAmount) {
                        if (playerCanPressThisTile[tileY][tileX]) {
                            field.flip(tileX, tileY);
                            currentMapGuesses++;
                            playerCanPressThisTile[tileY][tileX] = false;

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
        return ((x < currentMapSize) && (y < currentMapSize) && (x >= 0) && (y >= 0));
    }

    private boolean withinField(int x, int y) {
        return ((x > paddingLeft) && (x < paddingLeft + currentFieldSizePixels)
                && (y > paddingTop) && (y < paddingTop + currentFieldSizePixels));
    }

    public void changeParameters(int newH, int newW) {
        Log.d("DebugLevel:", "ChangeParameters() is called");

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
