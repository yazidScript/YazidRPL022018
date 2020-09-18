package com.example.yazid12rpl02.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditTextBlack extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditTextBlack(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditTextBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditTextBlack(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Black.ttf");
            setTypeface(tf);
        }
    }

}