package com.example.sudoku2;

import org.junit.jupiter.api.Test;

public class placeValTest {

    int[][] base = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
    };


    public void test(int val, int x, int y, int[][] board, int[][] expected, int curr) {
        Board B = new Board(board, Difficulty.EASY);
        boolean check;
        B.placeVal(val, x, y);
    }

    @Test
    public void simple() {

    }
}
