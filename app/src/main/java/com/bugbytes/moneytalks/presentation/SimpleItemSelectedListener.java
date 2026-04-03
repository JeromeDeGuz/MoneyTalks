package com.bugbytes.moneytalks.presentation;

import android.view.View;
import android.widget.AdapterView;

public class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener
{
    public interface OnItemSelectedCallback
    {
        void onItemSelected(int position);
    }

    private final OnItemSelectedCallback callback;

    //SimpleItemSelectedListener: Constructor to store the callback that runs when an item is selected. Takes in @param callback.
    public SimpleItemSelectedListener(OnItemSelectedCallback callback)
    {
        this.callback = callback;
    }

    //onItemSelected: It forwards the selected position to the callback. Takes in @param parent, view, position, and id. Returns nothing.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        callback.onItemSelected(position);
    }

    //onNothingSelected: Mandatory override that performs no action. Takes in @param parent. Returns nothing.
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // Do nothing
    }
}