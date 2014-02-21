package com.whatever.helper.memorygame;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.whatever.helper.R;

/**
 * Created by Igor on 18.02.14.
 */
public class MemoryGame extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_memory_game, container, false);

        return rootView;
    }
}
