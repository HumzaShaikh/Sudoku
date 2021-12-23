package com.example.sudoku2;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Board {
    public int[][] board;
    public Difficulty Difficulty;


    Board(int[][] init, Difficulty difficulty) {
        this.board = init;
        this.Difficulty = difficulty;
    }

    public boolean placeVal(int val, int y, int x) {
        int temp = this.board[x][y];
        this.board[x][y] = val;
        if (!checkValid(x,y)) {
            this.board[x][y] = temp;
            return false;
        }
        return true;
    }

    public boolean checkValid(int x, int y) {
        if (board.length != 9) return false;
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

    public static String stringFromIntArr(int[][] arr) {
        int[] concatArr = new int[81];
        if (arr.length != 9) return "Invalid";
        for (int i = 0; i < 81; i++) {
            int col = i / 9;
            int row = i % 9;
            concatArr[i] = arr[col][row];
        }
        char[] charArr = new char[81];
        for (int i = 0; i < 81; i ++) {
            if (concatArr[i] == 0) charArr[i] = 'x';
            else charArr[i] = Character.forDigit(concatArr[i],10);
        }
        return String.valueOf(charArr);

    }

    public static int[][] intArrFromString(String str) {
        System.out.println(Arrays.toString(str.toCharArray()));
        int[][] invalid = new int[][] {{ -1 }};
        int[][] rtn = new int[9][9];
        if (str.length() != 81) return invalid;
        str = str.replace('x', '0');
        char[] charArr = str.toCharArray();
        for (int i = 0; i < 81; i++) {
            if (charArr[i] == 'x') charArr[i] = 0;
            if (Character.getNumericValue(charArr[i]) < 0 || Character.getNumericValue(charArr[i]) > 9) {
                return invalid;
            }
            int col = i / 9;
            int row = i % 9;

            rtn[col][row] = Character.getNumericValue(charArr[i]);
        }
        return rtn;
    }

    public void solve() {

    }

    public static void print(int[][] arr) {
        String initBord = "=============================\n";
        String midBord = "-----------------------------";
        String temp = initBord;
        if (arr.length != 9) System.out.println("Invalid board");
        else {
            for (int col = 1; col < 10; col++) {
                temp += "||";
                for (int row = 1; row < 10; row++) {
                    if (arr[col - 1][row - 1] == 0) temp += " " + '_';
                    else temp += " " + arr[col - 1][row - 1];
                    if (row % 9 == 0) temp += " ||";
                    else if (row % 3 == 0) {
                        temp += " | ";
                    }
                }

                System.out.println(temp);
                temp = "";
                if (col % 9 == 0) System.out.println(initBord);
                else if (col % 3 == 0) System.out.println(midBord);
            }
            System.out.println(temp);
        }
    }
}
