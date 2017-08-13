package com.jeremybowyer.mathtap.model;

import android.graphics.Color;

import com.jeremybowyer.mathtap.R;

import java.util.Random;

public class ThemeWheel {
    // Fields (Member Variables) - Properties about the object
    private int[] mColors = {
            R.style.LightBlue, // light blue
            R.style.DarkBlue, // dark blue
            R.style.Mauve, // mauve
            R.style.Red, // red
            R.style.Orange, // orange
            R.style.Lavender, // lavender
            R.style.Purple, // purple
            R.style.Aqua, // aqua
            R.style.Green, // green
            R.style.Mustard, // mustard
            R.style.DarkGrey, // dark gray
            R.style.Pink, // pink
            R.style.LightGrey  // light gray
    };

    // Methods - Actions the object can take

    public int getThemeId() {
        int theme;
        // Randomly select a color
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(mColors.length);
        theme = mColors[randomNumber];;

        return theme;
    }
}