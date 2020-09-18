package com.example.yazid12rpl02.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class CustomTextViewMedium extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextViewMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewMedium(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
            setTypeface(tf);
        }
    }

}
