package com.septianfujianto.inventorymini.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.septianfujianto.inventorymini.App;

/**
 * Created by Septian A. Fujianto on 2/12/2017.
 */

public class InstantAutoComplete extends AutoCompleteTextView {

    public InstantAutoComplete(Context context) {
        super(context);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            try {
                performFiltering(getText(), 0);
            } catch (Exception e) {
                e.printStackTrace();

                if (e.getMessage() != null) {
                    Toast.makeText(App.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
