package com.whatever.helper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Igor on 04.02.14.
 */
public class Calculator extends Fragment implements View.OnClickListener {

    private String display = "0";
    private float result = 0.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_calculator, container, false);

        prepareViews(rootView);

        return rootView;
    }

    private void loopThroughChildren(View view) {
        int i = 0;
        //while (view.getCh)
    }

    private void prepareViews(View v) {
        Button button;
        button = (Button) v.findViewById(R.id.helper_calculator_button_0);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_1);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_2);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_3);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_4);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_5);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_6);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_7);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_8);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_9);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_clear);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_delete);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_div);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_mul);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_plus);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_min);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_dot);
        button.setOnClickListener(this);
        button = (Button) v.findViewById(R.id.helper_calculator_button_equ);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        TextView textView = (TextView) getActivity().findViewById(R.id.helper_calculator_display);

        switch (id) {
            case (R.id.helper_calculator_button_clear):
                display = "0";
                break;
            case (R.id.helper_calculator_button_delete):

                break;
            case (R.id.helper_calculator_button_0):
                if (!display.equals("0")) {
                    display = display + '0';
                }
                break;
            case (R.id.helper_calculator_button_1):
                if (display.equals("0")) {
                    display = "1";
                } else {
                    display = display + '1';
                }
                break;
            case (R.id.helper_calculator_button_2):
                if (display.equals("0")) {
                    display = "2";
                } else {
                    display = display + '2';
                }
                break;
            case (R.id.helper_calculator_button_3):
                if (display.equals("0")) {
                    display = "3";
                } else {
                    display = display + '3';
                }
                break;
            case (R.id.helper_calculator_button_4):
                if (display.equals("0")) {
                    display = "4";
                } else {
                    display = display + '4';
                }
                break;
            case (R.id.helper_calculator_button_5):
                if (display.equals("0")) {
                    display = "5";
                } else {
                    display = display + '5';
                }
                break;
            case (R.id.helper_calculator_button_6):
                if (display.equals("0")) {
                    display = "6";
                } else {
                    display = display + '6';
                }
                break;
            case (R.id.helper_calculator_button_7):
                if (display.equals("0")) {
                    display = "7";
                } else {
                    display = display + '7';
                }
                break;
            case (R.id.helper_calculator_button_8):
                if (display.equals("0")) {
                    display = "8";
                } else {
                    display = display + '8';
                }
                break;
            case (R.id.helper_calculator_button_9):
                if (display.equals("0")) {
                    display = "9";
                } else {
                    display = display + '9';
                }
                break;
            case (R.id.helper_calculator_button_dot):
                display = display + '.';
                break;
            case (R.id.helper_calculator_button_mul):
                display = display + '*';
                break;
            case (R.id.helper_calculator_button_div):
                display = display + '/';
                break;
            case (R.id.helper_calculator_button_plus):
                display = display + '+';
                break;
            case (R.id.helper_calculator_button_min):
                display = display + '-';
                break;
            case (R.id.helper_calculator_button_equ):
                display = processString(display);
                break;
        }

        textView.setText(display);
    }

    private String processString(String string) {
        String res = "";

        if (isThereErrors(string)) {
            res = "ERROR";
        } else {
            string = ',' + string + ',';

            string = processMulDiv(string);

            res = string.substring(1, string.length() - 1);

            res = processPlusMin(res);
        }

        return res;
    }

    private boolean containsSign(String string) {
        //boolean result = false;

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c == '*' || c == '/' || c == '-' || c == '+') {
                return true;
            }
        }

        return false;
    }

    private String processPlusMin(String string) {


        while (containsSign(string)) {
            string = string + '+';

            float first = getFirstNumber(string);
            string = string.substring(getFirstOccurrenceOfSign(string), string.length());
            char sign = string.charAt(0);
            string = string.substring(1, string.length());
            float second = getFirstNumber(string);
            string = string.substring(getFirstOccurrenceOfSign(string), string.length());

            float result = 0.0f;
            switch (sign) {
                case '+':
                    result = first + second;
                    break;
                case '-':
                    result = first - second;
                    break;
                default:
                    return "error";
            }

            string = result + string;

            string = string.substring(0, string.length() - 1);
        }


        return string;
    }

    private int getFirstOccurrenceOfSign(String string) {
        int result = -1;
        int i = 0;

        while (i < string.length()) {
            char c = string.charAt(i);
            if (c == '*' || c == '/' || c == '-' || c == '+') {
                result = i;
                return result;
            }

            i++;
        }

        return result;
    }

    private float getFirstNumber(String string) {
        float result = 0.0f;
        boolean dotOccurred = false;
        int counterForDot = 1;
        int i = 0;

        while (isNumberOrDot(string.charAt(i))) {
            if (dotOccurred) {
                counterForDot *= 10;
            }
            if (string.charAt(i) == '.') {
                dotOccurred = true;
            } else {
                result = result * 10 + getNumberFromChar(string.charAt(i));
            }

            i++;
        }
        result /= counterForDot;

        return result;
    }

    private String processMulDiv(String string) {

        int i = 0;
        int lastSign = 0; // ','
        float left = 0.0f;
        float right = 0.0f;
        boolean dotOccurred = false;
        int counterForDot = 1;
        int j = 0;
        int a = 0;
        float ans = 0.0f;

        while (!((string.indexOf('/') == -1) && (string.indexOf('*') == -1))) {
            i = 0;
            lastSign = 0; // ','

            while ((string.charAt(i) != '*') && (string.charAt(i) != '/')) {
                if (!isNumberOrDot(string.charAt(i))) {
                    lastSign = i;
                }

                i++;
            }

            left = 0;
            right = 0;
            dotOccurred = false;
            counterForDot = 1;

            j = lastSign;
            for (int aa = j + 1; aa < i; aa++) {
                if (dotOccurred) {
                    counterForDot *= 10;
                }

                if (string.charAt(aa) == '.') {
                    dotOccurred = true;
                } else {
                    left = left * 10 + getNumberFromChar(string.charAt(aa));
                }
            }
            if (dotOccurred) {
                left /= counterForDot;
            }

            dotOccurred = false;
            counterForDot = 1;
            a = i + 1;
            while (isNumberOrDot(string.charAt(a))) {
                if (dotOccurred) {
                    counterForDot *= 10;
                }

                if (string.charAt(a) == '.') {
                    dotOccurred = true;
                } else {
                    right = right * 10 + getNumberFromChar(string.charAt(a));
                }

                a++;
            }
            if (dotOccurred) {
                right /= counterForDot;
            }

            ans = 0.0f;
            if (string.charAt(i) == '*') {
                ans = left * right;
            } else if (string.charAt(i) == '/') {
                ans = left / right;
            }

            String l = string.substring(0, j + 1);
            String r = string.substring(a, string.length());
            string = l + ans + r;
        }

        return string;
    }

    private boolean isNumberOrDot(char c) {
        return (isNumber(c) || c == '.');
    }

    private boolean isNumber(char c) {
        int n = (int) c;
        if ((n >= (int) '0') && (n <= (int) '9')) {
            return true;
        }
        return false;
    }

    private int getNumberFromChar(char c) {
        int res = -1;
        switch (c) {
            case '0':
                res = 0;
                break;
            case '1':
                res = 1;
                break;
            case '2':
                res = 2;
                break;
            case '3':
                res = 3;
                break;
            case '4':
                res = 4;
                break;
            case '5':
                res = 5;
                break;
            case '6':
                res = 6;
                break;
            case '7':
                res = 7;
                break;
            case '8':
                res = 8;
                break;
            case '9':
                res = 9;
                break;
        }
        return res;
    }

    private boolean isThereErrors(String string) {
        return false;
    }
}