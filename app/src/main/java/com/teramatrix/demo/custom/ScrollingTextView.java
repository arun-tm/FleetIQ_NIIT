package com.teramatrix.demo.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by arun.singh on 6/11/2016.
 */
public class ScrollingTextView extends TextView implements Runnable {

    private static final float DEFAULT_SPEED = 20.0f;

    private Scroller scroller;
    private float speed = DEFAULT_SPEED;
    private boolean continuousScrolling = true;

    public ScrollingTextView(Context context) {
        super(context);
        setup(context);
    }

    public ScrollingTextView(Context context, AttributeSet attributes) {
        super(context, attributes);
        setup(context);
    }

    private void setup(Context context) {
        scroller = new Scroller(context, new LinearInterpolator());
        setScroller(scroller);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (scroller.isFinished()) {
            scroll();
        }
    }

    private void scroll() {
        int viewHeight = getHeight();
        int visibleHeight = viewHeight - getPaddingBottom() - getPaddingTop();
        int lineHeight = getLineHeight();

        int offset = -1 * visibleHeight;
        int distance = visibleHeight + getLineCount() * lineHeight;
        int duration = (int) (distance * speed);

        scroller.startScroll(0, offset, 0, distance, duration);

        if (continuousScrolling) {
            post(this);
        }
    }

    @Override
    public void run() {
        if (scroller.isFinished()) {
            scroll();
        } else {
            post(this);
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setContinuousScrolling(boolean continuousScrolling) {
        this.continuousScrolling = continuousScrolling;
    }

    public boolean isContinuousScrolling() {
        return continuousScrolling;
    }
}
