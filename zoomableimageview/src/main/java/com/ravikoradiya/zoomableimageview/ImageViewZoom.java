package com.ravikoradiya.zoomableimageview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

class ImageViewZoom extends AppCompatImageView {

    private ZoomableImageView zoomableImageView;
    /**
     * Hold a reference to the current animator, so that it can be canceled mid-way.
     */
    private Animator mCurrentAnimator;
    private long mShortAnimationDuration;

    public ImageViewZoom(ZoomableImageView zoomableImageView, Context context) {
        super(context);
        this.zoomableImageView = zoomableImageView;
    }

    public ImageViewZoom(ZoomableImageView zoomableImageView, Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zoomableImageView = zoomableImageView;
    }

    public ImageViewZoom(ZoomableImageView zoomableImageView, Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.zoomableImageView = zoomableImageView;
    }

    public void setHeight(float f) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = (int) f;
        setLayoutParams(layoutParams);
        invalidate();
    }

    public void setPaddingBottom(float f) {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), (int) f);
        invalidate();
    }

    public void setPaddingTop(float f) {
        setPadding(getPaddingLeft(), (int) f, getPaddingRight(), getPaddingBottom());
        invalidate();
    }

    public void setPaddingLeft(float f) {
        setPadding((int) f, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        invalidate();
    }

    public void setPaddingRight(float f) {
        setPadding(getPaddingLeft(), getPaddingTop(), (int) f, getPaddingBottom());
        invalidate();
    }

    public void setWidth(float f) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = (int) f;
        setLayoutParams(layoutParams);
        invalidate();
    }

    public void zoomImageFromThumb(final ImageView imageView) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        final float height;
        final float width;

        final Drawable drawable = imageView.getDrawable();

//            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        setImageDrawable(drawable);
        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail, and the
        // final bounds are the global visible rectangle of the container view. Also
        // set the container view's offset as the origin for the bounds, since that's
        // the origin for the positioning animation properties (X, Y).
        imageView.getGlobalVisibleRect(startBounds);
        ZoomableImageView.rootLayout.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float f = (((((float) startBounds.width()) / ((float) finalBounds.width())) * ((float) finalBounds.width())) - ((float) startBounds.width())) / 2.0f;
        startBounds.left = (int) (((float) startBounds.left) - f);
        startBounds.right = (int) (f + ((float) startBounds.right));
        f = (((((float) startBounds.height()) / ((float) finalBounds.height())) * ((float) finalBounds.height())) - ((float) startBounds.height())) / 2.0f;
        startBounds.top = (int) (((float) startBounds.top) - f);
        startBounds.bottom = (int) (f + ((float) startBounds.bottom));


        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail.
        setVisibility(View.VISIBLE);
        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
        // the zoomed-in view (the default is the center of the view).
        setPivotX(0f);
        setPivotY(0f);

        float drawableHeight = (float) drawable.getIntrinsicHeight();
        float drawableWidth = (float) drawable.getIntrinsicWidth();

//        if (getScaleType() != ScaleType.CENTER_CROP) {
//            if (startBounds.width() / startBounds.height() > drawableWidth / drawableHeight) {
//                drawableWidth = startBounds.width();
//                drawableHeight = (drawableHeight * drawableWidth) / drawableWidth;
//            } else {
//                drawableHeight = startBounds.height();
//                drawableWidth = (drawableWidth * drawableHeight) / drawableHeight;
//            }
//        }

        if ((((float) ZoomableImageView.rootLayout.getWidth()) * 1f) /
                ((float) ZoomableImageView.rootLayout.getHeight()) >
                (drawableWidth * 1f) / drawableHeight) {
            height = ZoomableImageView.rootLayout.getHeight();
            width = (drawableWidth * height) / drawableHeight;
        } else {
            width = ZoomableImageView.rootLayout.getWidth();
            height = (drawableHeight * width) / drawableWidth;
        }

        int top = 0;
        int bottom = 0;
        if (imageView.getHeight() > startBounds.height()) {
            if (startBounds.top == 0) {
                top = imageView.getHeight() - startBounds.height();
            }
            if (startBounds.bottom >= finalBounds.height()) {
                bottom = imageView.getHeight() - startBounds.height();
            }
        }

        final AnimatorSet set = new AnimatorSet();

        set.play(ObjectAnimator.ofFloat(ImageViewZoom.this, View.X,
                startBounds.left,
                finalBounds.left + ((ZoomableImageView.rootLayout.getWidth() - width) / 2)))
                .with(ObjectAnimator.ofFloat(ImageViewZoom.this, View.Y,
                        startBounds.top,
                        finalBounds.top + ((ZoomableImageView.rootLayout.getHeight() - height) / 2)))
                .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "width",
                        startBounds.width(), (float) width))
                .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "height",
                        startBounds.height(), (float) height))
                .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "paddingTop",
                        -top, 0.0f))
                .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "paddingBottom",
                        -bottom, 0.0f))
                .with(ObjectAnimator.ofInt(ZoomableImageView.rootLayout, "colorAlpha", 0, 255));

        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ZoomableImageView.touchImageView.setImageDrawable(getDrawable());
                ZoomableImageView.touchImageView.setScaleType(ScaleType.FIT_CENTER);
                ZoomableImageView.touchImageView.resetZoom();
                ZoomableImageView.touchImageView.setVisibility(VISIBLE);
                ImageViewZoom.this.setVisibility(GONE);
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                imageView.setVisibility(VISIBLE);
                mCurrentAnimator = null;
            }
        });
        set.start();
        imageView.setVisibility(INVISIBLE);
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
        // and show the thumbnail instead of the expanded image.
//        final float startScaleFinal = startScale;
        final int finalBottom = bottom;
        final int finalTop = top;
        ZoomableImageView.touchImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                ZoomableImageView.touchImageView.setVisibility(GONE);
                ImageViewZoom.this.setVisibility(VISIBLE);

                AnimatorSet set = new AnimatorSet();

                if (!ZoomableImageView.touchImageView.isZoomed()) {
                    set.play(ObjectAnimator.ofFloat(ImageViewZoom.this, View.X, ImageViewZoom.this.getX(), (float) startBounds.left))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, View.Y, ImageViewZoom.this.getY(), (float) startBounds.top))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "width", (float) ImageViewZoom.this.getWidth(), (float) startBounds.width()))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "height", (float) ImageViewZoom.this.getHeight(), (float) startBounds.height()))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "paddingTop", ImageViewZoom.this.getPaddingTop(), (float) (-finalTop)))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "paddingBottom", ImageViewZoom.this.getPaddingBottom(), (float) (-finalBottom)))
                            .with(ObjectAnimator.ofInt(ZoomableImageView.rootLayout, "colorAlpha", 255, 0));

                } else {

                    RectF rectF = ZoomableImageView.touchImageView.getZoomedRect();

                    float top = -rectF.top * ZoomableImageView.touchImageView.getImageHeight();
                    float bottom = -ZoomableImageView.touchImageView.getImageHeight() + rectF.bottom * ZoomableImageView.touchImageView.getImageHeight();
                    float left = -rectF.left * ZoomableImageView.touchImageView.getImageWidth();
                    float right = -ZoomableImageView.touchImageView.getImageWidth() + rectF.right * ZoomableImageView.touchImageView.getImageWidth();

                    float height = ZoomableImageView.touchImageView.getImageHeight();
                    float width = ZoomableImageView.touchImageView.getImageWidth();

                    if (height < ZoomableImageView.touchImageView.getHeight() && width > ZoomableImageView.touchImageView.getWidth()) {
                        top = bottom = (ZoomableImageView.touchImageView.getHeight() - height) / 2;
                    }

                    if (height > ZoomableImageView.touchImageView.getHeight() && width < ZoomableImageView.touchImageView.getWidth()) {
                        left = right = (ZoomableImageView.touchImageView.getWidth() - width) / 2;
                    }

                    set.play(ObjectAnimator.ofFloat(ImageViewZoom.this, View.X, Math.min(0, ImageViewZoom.this.getX()), (float) startBounds.left))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, View.Y, Math.min(0, ImageViewZoom.this.getY()), (float) startBounds.top))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "width", ZoomableImageView.touchImageView.getWidth(), (float) startBounds.width()))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "height", ZoomableImageView.touchImageView.getHeight(), (float) startBounds.height()))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "paddingTop", top, -finalTop))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "paddingBottom", bottom, -finalBottom))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "paddingLeft", left, 0))
                            .with(ObjectAnimator.ofFloat(ImageViewZoom.this, "paddingRight", right, 0))
                            .with(ObjectAnimator.ofInt(ZoomableImageView.rootLayout, "colorAlpha", 255, 0));
                }
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ImageViewZoom.this.setVisibility(View.GONE);
                        imageView.setVisibility(VISIBLE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        ImageViewZoom.this.setVisibility(View.GONE);
                        imageView.setVisibility(VISIBLE);
                        mCurrentAnimator = null;
                    }
                });

                imageView.setVisibility(INVISIBLE);
                ImageViewZoom.this.setVisibility(View.VISIBLE);
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    public void setmShortAnimationDuration(long mShortAnimationDuration) {
        this.mShortAnimationDuration = mShortAnimationDuration;
    }
}
