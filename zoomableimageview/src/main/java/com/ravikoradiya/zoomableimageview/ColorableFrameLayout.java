package com.ravikoradiya.zoomableimageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;

class ColorableFrameLayout extends FrameLayout {

    public int getCustBackground() {
        return backgroundColor;
    }

    public void setCustBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    int backgroundColor;

    public ColorableFrameLayout(Context context) {
        super(context);
        init();
    }

    public ColorableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {

        setFocusable(true);
        setFocusableInTouchMode(true);

    }

    void setColorAlpha(int alpha) {
        setBackgroundColor(Color.argb(alpha, Color.red(backgroundColor), Color.green(backgroundColor), Color.blue(backgroundColor)));
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
//        Log.e(TAG, "dispatchKeyEventPreIme(" + event + ")");
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            KeyEvent.DispatcherState state = getKeyDispatcherState();
            if (state != null) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getRepeatCount() == 0) {
                    state.startTracking(event, this);
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_UP
                        && !event.isCanceled() && state.isTracking(event)) {
                    if (ZoomableImageView.touchImageView.getVisibility() == VISIBLE) {
                        ZoomableImageView.touchImageView.performClick();
                        return true;
                    }
                }
            }
        }

        return super.dispatchKeyEventPreIme(event);
    }
}
