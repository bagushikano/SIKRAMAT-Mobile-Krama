package com.bagushikano.sikedatmobile.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class CloseKeyboard {
    public static void CloseKeyboard(View view, Context context) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
