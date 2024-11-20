package com.example.volumecalculatorapp;

import android.view.View;
import android.widget.AdapterView;

public class SimpleSpinnerListener implements AdapterView.OnItemSelectedListener {

    private final Runnable action;

    public SimpleSpinnerListener(Runnable action) {
        this.action = action;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        action.run();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No-op
    }
}
