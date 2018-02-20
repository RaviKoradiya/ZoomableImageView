package com.ravikoradiya.zoomableimageview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class ZoomableImageView extends AppCompatImageView {

    private static final String TAG = "ImageViewZoom";

    static ColorableFrameLayout rootLayout;
    static ImageViewZoom imageViewZoom;
    static TouchImageView touchImageView;
    Context mContext;
    private ViewGroup viewGroup;

    int placeholderId;

    String imageUrl;
    int backgroundColor;

    /**
     * The system "short" animation time duration, in milliseconds. This duration is ideal for
     * subtle animations or animations that occur very frequently.
     */
    private int mShortAnimationDuration;
    private ColorStateList colorStateList;

    public ZoomableImageView(Context context) {
        super(context);
        init(null);
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ZoomableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAnimationSpeed(int speed) {
        mShortAnimationDuration = speed;
    }


    void init(AttributeSet attrs) {

        mContext = getContext();

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        if (attrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.ZoomableImageView, 0, 0);
            try {
                mShortAnimationDuration = ta.getInteger(R.styleable.ZoomableImageView_animation_speed, mShortAnimationDuration);
                placeholderId = ta.getResourceId(R.styleable.ZoomableImageView_placeholder_id, -1);
                imageUrl = ta.getString(R.styleable.ZoomableImageView_image_url);
                colorStateList = ta.getColorStateList(R.styleable.ZoomableImageView_background_color);
                if (colorStateList != null) {
                    backgroundColor = colorStateList.getDefaultColor();
                } else {
                    backgroundColor = ta.getColor(R.styleable.ZoomableImageView_background_color, Color.BLACK);
                }
            } finally {
                ta.recycle();
            }
        }

        post(new Runnable() {
            @Override
            public void run() {

                if (placeholderId != -1)
                    setImageResource(placeholderId);

                if (imageUrl != null) {
                    Glide.with(getContext())
                            .load(imageUrl)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation
                                        glideAnimation) {
                                    setImageBitmap(resource); // Possibly runOnUiThread()
                                }
                            });
                }

                inflateRootLayout();
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ZoomableImageView.this.getDrawable() != null) {
                    if (rootLayout == null) {
                        inflateRootLayout();
                    } else if (rootLayout.getParent() != null) {
                        ((ViewGroup) rootLayout.getParent()).removeView(rootLayout);
                    }
                    rootLayout.setCustBackgroundColor(backgroundColor);
                    viewGroup.addView(rootLayout, -1, -1);
                    imageViewZoom.zoomImageFromThumb(ZoomableImageView.this);

                    rootLayout.requestFocus(FOCUS_DOWN);
                }
            }
        });
    }

    void inflateRootLayout() {
        rootLayout = new ColorableFrameLayout(mContext);
        imageViewZoom = new ImageViewZoom(this, mContext);
        imageViewZoom.setmShortAnimationDuration(mShortAnimationDuration);
        imageViewZoom.setScaleType(getScaleType());
        touchImageView = new TouchImageView(mContext);
        touchImageView.setVisibility(GONE);
        rootLayout.addView(touchImageView, -1, -1);
        rootLayout.addView(imageViewZoom, -1, -1);
        viewGroup = getRoot(getParent());
        viewGroup.addView(rootLayout);
    }

    ViewGroup getRoot(ViewParent viewParent) {
        ViewGroup viewGroup = (ViewGroup) viewParent;
        if (null != viewGroup && viewGroup.getId() == android.R.id.content)
            return viewGroup;

        return getRoot(viewGroup.getParent());
    }

}

