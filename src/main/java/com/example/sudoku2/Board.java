package com.example.sudoku2;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Board {
    public int[][] board;
    public Difficulty Difficulty;
    public boolean[][] checks = (new boolean[9][9]);

    Board(int[][] init, Difficulty difficulty) {
        this.board = init;
        this.Difficulty = difficulty;
        for (boolean[] curr: checks
             ) {
            Arrays.fill(curr,true);
        }
    }

    /*
    public boolean placeVal(int val, int row, int col) {
        if (val == 0) {
            this.board[row][col] = 0;
            return true;
        }
        if (checkValid()) {
            this.board[row][col] = val;
            return true;
        }
        return false;
    }
     */

    public void checkValid() {

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int curr = board[row][col];



                checks[row][col] = curr >= 0 && curr <= 9 && row(curr,row,col) && col(curr,row,col) && subsec(curr,row,col);
            }
        }
    }


    private boolean row(int num, int row, int col) {
        //System.out.println(num);
        if (num == 0) return true;
        //System.out.println(Arrays.toString(board[row]));
        for(int i = 0; i < 9; i++) {
           // System.out.println("current ting: " + board[row][i]);

            if (board[row][i] == num && i != col) {
              System.out.printf("\nRow false " + row + " with num: " + num);
                return false;
            }
        }
        return true;
    }

    private boolean col(int num, int row, int col) {
        if (num == 0) return true;
        for(int i = 0; i < 9; i++) {
            if (board[i][col] == num && i != row) {
                System.out.printf("\nCol false: " + col + " with num: " + num );
                return false;
            }
        }
        return true;
    }

    private boolean subsec(int val, int row, int col) {

        if (val == 0) return true;
        int subsectionRowStart = (row / 3) * 3;
        int subsectionRowEnd = subsectionRowStart + 3;

        int subsectionColStart = (col / 3) * 3;
        int subsectionColEnd = subsectionColStart + 3;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColStart; c < subsectionColEnd; c++) {
                if (board[r][c] == val && r != row && c != col) {
                  System.out.println("\nSubsec false with col: " + col + " and row: " + row + " for num: " + val);
                    return false;
                }
            }
        }
        return true;
    }

    boolean checkConstraint(int row, int col, boolean[] constraint) {
        int[] nums = {1,2,3,4,5,6,7,8,9};
        if (board [row][col] < 10 && board[row][col] > 0) {
            if (!constraint[board[row][col] - 1]) {
                constraint[board[row][col] - 1] = true;
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

    public static String print(int[][] arr) {
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
            return "";
        }
        return "";
    }
}
