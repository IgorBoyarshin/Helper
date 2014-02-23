package com.whatever.helper.mathbirds;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.whatever.helper.R;

/**
 * Created by Igor on 21.02.14.
 */
public class MathBirds extends Fragment implements View.OnClickListener {
    private MathBirdsView mathBirdsView;
    private LinearLayout buttons;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_math_birds, container, false);

        prepareViews(rootView);

        return rootView;
    }

    private void prepareViews(View v) {
        mathBirdsView = (MathBirdsView) v.findViewById(R.id.helper_mathbirds_sky);
        View view = (View) v.findViewById(R.id.helper_mathbirds_sky);
        mathBirdsView.receiveView(view);
//        LinearLayout buttons = (LinearLayout) v.findViewById(R.id.helper_mathbirds_buttons);
//        int buttonsH = buttons.getHeight();
//        LinearLayout container = (LinearLayout) v.findViewById(R.id.helper_mathbirds_container);
//        int height =  container.getHeight();
//        int width = container.getWidth();
////        Log.d("Cow", width + " " + height + " " + buttonsH);
////        Log.d("Cow2", v.getWidth() + " " + v.getHeight());
//        view.setLayoutParams(new LinearLayout.LayoutParams(600, 700));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.helper_mathbirds_button_0:

                break;
        }
    }
}
