package com.whatever.helper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Igor on 17.02.14.
 */
public class Main extends Fragment implements View.OnClickListener {
    // reloading app
    private static final String MAIN_INPUT_TEXT = "com.whatever.helper.MAIN_INPUT_TEXT";

    // commands
    private static final String COMMAND_CHANGE_COLOR0 = "color0";
    private static final String COMMAND_CHANGE_COLOR1 = "color1";
    private static final String COMMAND_CHANGE_COLOR2 = "color2";

    public Main() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_main, container, false);

        prepareViews(rootView);

        return rootView;
    }

    private void prepareViews(View v) {
        Button button = (Button) v.findViewById(R.id.helper_main_input_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id) {
            case (R.id.helper_main_input_button):
                EditText editText = (EditText) getActivity().findViewById(R.id.helper_main_input_edittext);
                String command = editText.getText().toString();
                executeCommand(command);
                break;
        }
    }

    public void executeCommand(String command) {
        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.helper_main_bg);

        if (command.equals(COMMAND_CHANGE_COLOR1)) {
            linearLayout.setBackgroundResource(R.color.test1);
        } else if(command.equals(COMMAND_CHANGE_COLOR2)) {
            linearLayout.setBackgroundResource(R.color.test2);
        } else if (command.equals(COMMAND_CHANGE_COLOR0)) {
            linearLayout.setBackgroundResource(R.color.helper_main_bg);
        } else {
            // alert 'no such command'
        }
    }
}
