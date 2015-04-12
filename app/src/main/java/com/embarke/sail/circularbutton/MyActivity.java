package com.embarke.sail.circularbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;


/**
 * Circular progress drawable demonstration
 *
 * @author Saul Diaz <sefford@gmail.com>
 */
public class MyActivity extends Activity implements View.OnTouchListener{

    // Views
    ImageView ivDrawable;

    CircularProgressDrawable drawable;

    Animator currentAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivDrawable = (ImageView) findViewById(R.id.iv_drawable);

        drawable = new CircularProgressDrawable.Builder()
                .setRingWidth(getResources().getDimensionPixelSize(R.dimen.drawable_ring_size))
                .setOutlineColor(getResources().getColor(R.color.cpb_blue))
                .setRingColor(getResources().getColor(R.color.cpb_blue_green))
                .setCenterColor(getResources().getColor(R.color.cpb_blue))
                .setFillColor(getResources().getColor(R.color.cpb_blue_green))
                .create();
        ivDrawable.setImageDrawable(drawable);
        ivDrawable.setOnTouchListener(this);
    }

    /**
     * Style 2 animation will fill the outer ring while applying a color effect from red to green
     *
     * @return Animation
     */
    private Animator prepareStyle2Animation() {
        AnimatorSet animation = new AnimatorSet();

        final ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.PROGRESS_PROPERTY,
                0f, 1f);
        progressAnimation.setDuration(1500);
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        Animator innerCircleAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY, 0f, 1f);
        innerCircleAnimation.setDuration(500);
        innerCircleAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                drawable.setProgress(0f);
                drawable.setCircleScale(0f);
            }
        });
        animation.playSequentially(progressAnimation, innerCircleAnimation);
        return animation;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            currentAnimation = prepareStyle2Animation();
            currentAnimation.start();
        }
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            if (currentAnimation != null) {
                currentAnimation.cancel();
                drawable.setProgress(0f);
                drawable.setCircleScale(0f);
            }
        }
        //view.performClick();
        return true;
    }
}
