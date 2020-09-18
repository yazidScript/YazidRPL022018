package com.example.yazid12rpl02.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditTextLight extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditTextLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditTextLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditTextLight(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
            setTypeface(tf);
        }
    }

}