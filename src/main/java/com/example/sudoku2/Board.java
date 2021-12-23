package com.example.sudoku2;

import java.io.LineNumberInputStream;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Board {
    public int[][] board;
    public Difficulty Difficulty;


    Board(int[][] init, Difficulty difficulty) {
        this.board = init;
        this.Difficulty = difficulty;
    }

    public void placeVal(int val, int x, int y) {
        int temp = this.board[x][y];
        this.board[x][y] = val;
        if (!checkValid(x,y)) this.board[x][y] = temp;
    }

    public boolean checkValid(int x, int y) {
        if (board[x][y] < 0 || board[x][y] > 9) return false;
        return row(x) && col(y) && subsec(x,y);

    }
    private boolean row(int x) {
        boolean[] constraint = new boolean[9];
        return IntStream.range(0,9).allMatch(column -> checkConstraint(x, column, constraint));
    }

    private boolean col(int y) {
        boolean[] constraint = new boolean[9];
        return IntStream.range(0,9).allMatch(row -> checkConstraint(y, row, constraint));
    }

    private boolean subsec(int x, int y) {
        boolean[] constraint = new boolean[9];
        int subsectionRowStart = (y / 3) * 3;
        int subsectionRowEnd = subsectionRowStart + 3;

        int subsectionColStart = (x / 3) * 3;
        int subsectionColEnd = subsectionColStart + 3;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColStart; c < subsectionColEnd; c++) {
                if (!checkConstraint(r,c,constraint)) return false;
            }
        }
        return true;
    }

    boolean checkConstraint(int y, int x, boolean[] constraint) {
        int[] nums = {1,2,3,4,5,6,7,8,9};
        if (board [x][y] < 10 && board[x][y] > 0) {
            if (!constraint[board[x][y] - 1]) {
                constraint[board[x][y] - 1] = true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void solve() {

    }
}
