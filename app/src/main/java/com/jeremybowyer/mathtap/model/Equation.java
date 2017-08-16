package com.jeremybowyer.mathtap.model;

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
            int num1 = r.nextInt(101);
            int num2 = r.nextInt(101);

            switch (op) {
                case "+":
                    mAnswer = Integer.toString(num1 + num2);
                    break;
                case "-":
                    mAnswer = Integer.toString(num1 - num2);
                    break;
            }
            mEquation = num1 + " " + op + " " + num2;
        } else if(level == 2) {
            ops = new ArrayList<String>(Arrays.asList("+", "-"));
            String op1 = ops.get(r.nextInt(ops.size()));
            String op2 = ops.get(r.nextInt(ops.size()));
            int num1 = r.nextInt(101);
            int num2 = r.nextInt(101);
            int num3 = r.nextInt(101);

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
