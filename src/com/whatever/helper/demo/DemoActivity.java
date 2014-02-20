package com.whatever.helper.demo;

import android.app.Activity;
import android.os.Bundle;
import com.whatever.helper.R;

/**
 * Created by Igor on 06.02.14.
 */
public class DemoActivity extends Activity {

    private DemoView demoView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_function);
        demoView = (DemoView) findViewById(R.id.helper_demo_view);
    }
}