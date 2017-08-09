package com.jeremybowyer.mathtap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Equation {

    private String mEquation;
    private String mAnswer;
    private ArrayList<String> ops = new ArrayList<String>();

    Random r = new Random();

    public Equation(int level) {
        if(level == 1) {
            ops = new ArrayList<String>(Arrays.asList("+", "-"));
            int opIndex = r.nextInt(ops.size());
            String op = ops.get(opIndex);
            int num1 = r.nextInt(100 - 0);
            int num2 = r.nextInt(100 - 0);

            switch (op) {
                case "+":
                    mAnswer = Integer.toString(num1 + num2);
                    break;
                case "-":
                    mAnswer = Integer.toString(num1 - num2);
                    break;
            }
            mEquation = num1 + " " + op + " " + num2;
        }
    }

    public boolean isAnswer(String answer) {
        return mAnswer == answer;
    }

    public String getWrongAnswer() {
        int deviation = 0;
        do {
            deviation = r.nextInt(21) - 10;
        } while (deviation == 0);

        int wrongAnswerInt = Integer.parseInt(mAnswer) + deviation;
        String wrongAnswer = Integer.toString(wrongAnswerInt);

        return wrongAnswer;
    }

    public String getEquation() {
        return mEquation;
    }

    public void setEquation(String equation) {
        mEquation = equation;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }
}
