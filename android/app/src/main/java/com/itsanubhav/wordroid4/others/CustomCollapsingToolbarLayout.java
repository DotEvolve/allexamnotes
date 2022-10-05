package com.itsanubhav.wordroid4.others;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.lang.reflect.Field;

public class CustomCollapsingToolbarLayout extends CollapsingToolbarLayout {

    public CustomCollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public CustomCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            Field fs = this.getClass().getSuperclass().getDeclaredField("mLastInsets");
            fs.setAccessible(true);
            WindowInsetsCompat mLastInsets = (WindowInsetsCompat) fs.get(this);
            final int mode = MeasureSpec.getMode(heightMeasureSpec);
            int topInset = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
            if (mode == MeasureSpec.UNSPECIFIED && topInset > 0) {
                // fix the bottom empty padding
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        getMeasuredHeight() - topInset, MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}