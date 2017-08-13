package com.jeremybowyer.mathtap.model;

import android.view.View;
import android.view.animation.Animation;

public class AnimationListenerShow implements Animation.AnimationListener {
    View view;

    public void setView(View view) {
        this.view = view;
    }
    public void onAnimationEnd(Animation animation) {
    }
    public void onAnimationRepeat(Animation animation) {
    }
    public void onAnimationStart(Animation animation) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(1);
        view.setScaleX(1);
        view.setScaleY(1);
    }
}

