package com.jeremybowyer.mathtap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Equation {

    private String mEquation;
    private String mAnswer;
    private int mDeviationRange;
    private ArrayList<String> ops = new ArrayList<String>();

    Random r = new Random();

    public Equation(int level) {
        int num1;
        int num2;
        int num3;
        String op1;
        String op2;
        String op;

        if(level == 1) {
            
            ops = new ArrayList<String>(Arrays.asList("+", "-"));
            op = ops.get(r.nextInt(ops.size()));
            num1 = r.nextInt(101);
            num2 = r.nextInt(101);

            switch (op) {
                case "+":
                    mAnswer = Integer.toString(num1 + num2);
                    break;
                case "-":
                    mAnswer = Integer.toString(num1 - num2);
                    break;
            }
            mEquation = num1 + " " + op + " " + num2;
            mDeviationRange = 21;
            
        } else if(level == 2) {

            num1 = r.nextInt(41) - 20;
            num2 = r.nextInt(41) - 20;
            mAnswer = Integer.toString(num1 * num2);
            mEquation = num1 + " * " + num2;
            mDeviationRange = 21;

        } else if(level == 3) {

            num1 = r.nextInt(11);
            num2 = r.nextInt(11) + 1;
            num1 = num1 * num2;
            mAnswer = Integer.toString(num1 / num2);
            mEquation = num1 + " / " + num2;
            mDeviationRange = 21;

        } else if(level == 4) {

            ops = new ArrayList<String>(Arrays.asList("+", "-"));
            op1 = ops.get(r.nextInt(ops.size()));
            op2 = ops.get(r.nextInt(ops.size()));
            num1 = r.nextInt(101);
            num2 = r.nextInt(101);
            num3 = r.nextInt(101);

            int answer1 = 0;

            switch (op1) {
                case "+":
                    answer1 = num1 + num2;
                    break;
                case "-":
                    answer1 = num1 - num2;
                    break;
            }
            switch(op2) {
                case "+":
                    mAnswer = Integer.toString(answer1 + num3);
                    break;
                case "-":
                    mAnswer = Integer.toString(answer1 - num3);
                    break;
            }
            mEquation = num1 + " " + op1 + " " + num2 + " " + op2 + " " + num3;
            mDeviationRange = 21;


        } else if (level >= 5) {

            num1 = r.nextInt(21) - 10;
            do {
                num3 = r.nextInt(11) - 5;
            } while (num3 == 0);
            num2 = num3 * 2;
            mAnswer = Integer.toString(num1 * num2 / num3);
            mEquation = num1 + " * " + num2 + " / " + num3;
            mDeviationRange = 21;

        }
    }

    public boolean isAnswer(String answer) {
        return mAnswer == answer;
    }

    public String getWrongAnswer() {
        int deviation = 0;
        do {
            deviation = r.nextInt(mDeviationRange) - ((mDeviationRange-1) / 2);
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
