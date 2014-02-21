package com.whatever.helper.mathbirds;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.whatever.helper.R;

/**
 * Created by Igor on 21.02.14.
 */
public class MathBirdsView extends View {
    private View view;

    public MathBirdsView(Context context) {
        super(context);
    }
    public MathBirdsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void receiveView(View view) {
        this.view = view;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        View parent = (View) this.getParent();
//        View view = (View) parent.findViewById(R.id.helper_mathbirds_sky);
        LinearLayout buttons = (LinearLayout) parent.findViewById(R.id.helper_mathbirds_buttons);
        int buttonsH = buttons.getHeight();
        LinearLayout container = (LinearLayout) parent.findViewById(R.id.helper_mathbirds_container);
        int height =  container.getHeight();
        int width = container.getWidth();
        Log.d("Cow", width + " " + height + " " + buttonsH);
        Log.d("Cow2", this.getWidth() + " " + this.getHeight());
        view.setLayoutParams(new LinearLayout.LayoutParams(600, 700));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint bg = new Paint();
        bg.setColor(getContext().getResources().getColor(R.color.helper_mathbirds_bg));
        canvas.drawRect(0,0, getWidth(), getHeight(), bg);

        Paint p = new Paint();
        p.setColor(Color.RED);
        canvas.drawRect(100, 100, 300, 400, p);
    }
}
