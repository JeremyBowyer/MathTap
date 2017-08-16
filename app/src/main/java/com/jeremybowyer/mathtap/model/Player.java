package com.jeremybowyer.mathtap.model;

import android.util.Log;

import com.google.gson.Gson;

public class Player {
    private String mPlayerName;
    private int mPlayerPoints = 0;
    private int mPlayerHp = 3;
    private int mSuccessfulGuesses = 0;
    private int mLevel;

    public Player(String name){
        mPlayerName = name;
    }

    public int takeHit() {
        return --mPlayerHp;
    }

    public void addSuccess() {
        mSuccessfulGuesses++;
    }

    public int addPoints(int points) {
        mPlayerPoints += points;
        mLevel = (int) Math.floor((mPlayerPoints / 3000) + 1);
        return mPlayerPoints;
    }

    public void resetStats() {
        mPlayerPoints = 0;
        mPlayerHp = 3;
        mSuccessfulGuesses = 0;
    }

    public String getJsonString() {
        Gson gS = new Gson();
        String playerString = gS.toJson(this);
        return playerString;
    }

    public int getSuccessfulGuesses() {
        return mSuccessfulGuesses;
    }

    public void setSuccessfulGuesses(int successfulGuesses) {
        mSuccessfulGuesses = successfulGuesses;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public void setPlayerName(String playerName) {
        mPlayerName = playerName;
    }

    public int getPlayerPoints() {
        return mPlayerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        mPlayerPoints = playerPoints;
    }

    public int getPlayerHp() {
        return mPlayerHp;
    }

    public void setPlayerHp(int playerHp) {
        mPlayerHp = playerHp;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

}
