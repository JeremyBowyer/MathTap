package com.jeremybowyer.mathtap.model;

import android.view.View;
import android.view.animation.Animation;

public class AnimationListenerHide implements Animation.AnimationListener {
    View view;

    public void setView(View view) {
        this.view = view;
    }
    public void onAnimationEnd(Animation animation) {
        view.setVisibility(View.INVISIBLE);
        view.setAlpha(0);
        view.setScaleX(0);
        view.setScaleY(0);
    }
    public void onAnimationRepeat(Animation animation) {
    }
    public void onAnimationStart(Animation animation) {
    }
}

