package com.whatever.helper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Igor on 06.02.14.
 */
public class Function extends Fragment implements View.OnClickListener {

    private FunctionView functionView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_function, container, false);

        prepareViews(rootView);

        return rootView;
    }

    private void prepareViews(View v) {
        functionView = (FunctionView) v.findViewById(R.id.helper_function_drawing_area);
        Button button = (Button) v.findViewById(R.id.helper_function_button_draw);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.helper_function_button_draw:

                break;
        }
    }

    public void render(View view) {

        //EditText editText = (EditText) findViewById(R.id.helper_graphics_function);
        //String func = editText.getText().toString();



    }


}