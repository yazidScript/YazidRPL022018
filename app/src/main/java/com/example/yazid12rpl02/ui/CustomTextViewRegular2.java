package com.example.yazid12rpl02.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class CustomTextViewRegular2 extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextViewRegular2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewRegular2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewRegular2(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Regular.ttf");
            setTypeface(tf);
        }
    }

}
