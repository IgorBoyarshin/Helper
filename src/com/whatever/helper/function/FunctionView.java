package com.whatever.helper.function;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.whatever.helper.R;

/**
 * Created by Igor on 06.02.14.
 */
public class FunctionView extends View {

    Bitmap bitmap;

    public FunctionView(Context context) {
        super(context);
    }

    public FunctionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private static final int numOfDots = 101;
    private static final float scaleX = 5.0f;
    private static final float scaleY = 0.2f;
    private static final int dotSize = 6;

    private void drawFunc(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getContext().getResources().getColor(R.color.helper_function_drawing_area_dots));

        int[][] dots = new int[numOfDots][2];
        int x = -50;
        for (int i = 0; i < numOfDots; i++) {
            dots[i][0] = (int) (x * scaleX) + canvas.getWidth() / 2;
            dots[i][1] = -(int) (x * x * scaleY) + canvas.getHeight() / 2;
            x += 1;
        }

        for (int i = 0; i < numOfDots; i++) {
            //canvas.drawPoint(dots[i][0], dots[i][1], paint);
            canvas.drawRect(dots[i][0] - dotSize / 2, dots[i][1] - dotSize / 2, dots[i][0] + dotSize / 2, dots[i][1] + dotSize / 2, paint);
        }
    }

    private void drawAxes(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getContext().getResources().getColor(R.color.helper_function_drawing_area_axes));

        //canvas.drawRect(canvas.getWidth()/4, canvas.getHeight()/4, canvas.getWidth()/4*3, canvas.getHeight()/4*3, paint);
        canvas.drawLine(canvas.getWidth() / 2, canvas.getHeight(), canvas.getWidth() / 2, 0, paint);
        canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2, paint);

        canvas.drawRect(30, 30, 35, 35, paint);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getContext().getResources().getColor(R.color.helper_function_drawing_area_bg));

        drawAxes(canvas);
        drawFunc(canvas);

    }
}