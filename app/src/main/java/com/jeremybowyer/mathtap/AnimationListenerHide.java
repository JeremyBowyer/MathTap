package com.jeremybowyer.mathtap;

import android.view.View;
import android.view.animation.Animation;

public class AnimationListenerHide implements Animation.AnimationListener {
    View view;
    public void setView(View view) {
        this.view = view;
    }
    public void onAnimationEnd(Animation animation) {
        view.setVisibility(View.INVISIBLE);
    }
    public void onAnimationRepeat(Animation animation) {
    }
    public void onAnimationStart(Animation animation) {
    }
}

