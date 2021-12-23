package com.example.sudoku2;


import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class makeBoardTest {
    //Testing stringFromIntArr and intArrFromString methods in Board.java class

    int[][] intBase = {
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
    };

    int[][] invalid = new int[][] {{-1}};

    String baseString =
                    "123456789"
            +       "456789123"
            +       "789123456"
            +       "234567891"
            +       "567891234"
            +       "891234567"
            +       "345678912"
            +       "678912345"
            +       "912345678";

    String invalidString = "";

    private void test(String str, int[][] expected, boolean checkExp, int curr) {
        int[][] act = Board.intArrFromString(str);
        boolean checkAct = Arrays.deepEquals(expected, act);

        assertSame(checkExp,checkAct,"Expected " +
                Arrays.deepToString(expected) + " but got " + Arrays.deepToString(act)
                + " on case " + curr);
    }

    private void test(int[][] arr, String expected, int curr) {
        String act = Board.stringFromIntArr(arr);
        assertEquals(expected,act,"Expected " +
                expected + " but got " + act + " on case " + curr);
    }

    @Test
    public void simple() {
        test(new int[][] {{ -1 }},"Invalid",1);

        String testString = "";
        for (int i = 0; i < 80; i++) {
            testString += '1';
        }
        int[][] testArr = intBase;

        test(testString, new int[][] {{ -1 }}, true,2);

        test(testString + "11", invalid, true, 3);

        testString.replace('1','x');
        String expString = "";
        for (int i = 0; i < 81; i++) {
            expString += 'x';
        }
        test(intBase, expString,4);


        for (int i = 0; i < 81; i++) {
            int col = i / 9;
            int row = i % 9;
            testArr[col][row] = Character.getNumericValue(baseString.charAt(i));
        }

        test(testArr, baseString, 5);
        test(baseString, testArr, true, 6);
    }

    @Test
    public void complex() {
        String testString1 = "123456789"
        +   "xxxxxxxxx"
        +   "7x9x2x4x6"
        +   "x3x5x7x9x"
        +   "567xxx234"
        +   "xxx234xxx"
        +   "34xx78xx2"
        +   "xx89xx34x"
        +   "9123x5678";

        int[][] testArr1 = {
                {1,2,3,4,5,6,7,8,9},
                {0,0,0,0,0,0,0,0,0},
                {7,0,9,0,2,0,4,0,6},
                {0,3,0,5,0,7,0,9,0},
                {5,6,7,0,0,0,2,3,4},
                {0,0,0,2,3,4,0,0,0},
                {3,4,0,0,7,8,0,0,2},
                {0,0,8,9,0,0,3,4,0},
                {9,1,2,3,0,5,6,7,8}
        };
        test(testArr1, testString1, 1);
        test(testString1, testArr1, true, 2);

    }


}
