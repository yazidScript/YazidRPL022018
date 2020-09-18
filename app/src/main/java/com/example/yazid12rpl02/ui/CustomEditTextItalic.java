package com.example.yazid12rpl02.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditTextItalic extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditTextItalic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditTextItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditTextItalic(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Italic.ttf");
            setTypeface(tf);
        }
    }

}