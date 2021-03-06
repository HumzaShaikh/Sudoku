package com.example.sudoku2;

import java.util.Arrays;

public class Game {

    int[][] solution = new int[9][9];
    int[] mat[];
    int N = 9;
    int SRN = 3;
    int K;

    Game(Difficulty difficulty) {
        switch (difficulty) {
            case EASY -> this.K = 41;
            case MEDIUM -> this.K = 49;
            case HARD -> this.K = 56;
        }
        mat = new int[N][N];
        this.mat = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        this.fillValues();
    }

    public void fillValues() {
        fillDiagonal();
        fillRemaining(0,3);

        removeKDigits();
    }

    void fillDiagonal() {
        for (int i = 0; i < N; i=i+SRN) {
            fillBox(i,i);
        }
    }

    boolean unUsedInBox(int rowStart, int colStart, int num)
    {
        for (int i = 0; i<SRN; i++)
            for (int j = 0; j<SRN; j++)
                if (mat[rowStart+i][colStart+j]==num)
                    return false;

        return true;
    }

    void fillBox(int row, int col) {
        int num;
        for (int i=0; i<SRN; i++)
        {
            for (int j=0; j<SRN; j++)
            {
                do
                {
                    num = randomGenerator(N);
                }
                while (!unUsedInBox(row, col, num));

                mat[row+i][col+j] = num;
            }
        }
    }

    int randomGenerator(int num) {
        return (int) Math.floor((Math.random()*num+1));
    }

    boolean CheckIfSafe(int i,int j,int num)
    {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i-i%SRN, j-j%SRN, num));
    }

    // check in the row for existence
    boolean unUsedInRow(int i,int num)
    {
        for (int j = 0; j<N; j++)
            if (mat[i][j] == num)
                return false;
        return true;
    }

    boolean unUsedInCol(int j,int num)
    {
        for (int i = 0; i<N; i++)
            if (mat[i][j] == num)
                return false;
        return true;
    }

    boolean fillRemaining(int i, int j)
    {
        //  System.out.println(i+" "+j);
        if (j>=N && i<N-1)
        {
            i = i + 1;
            j = 0;
        }
        if (i>=N && j>=N)
            return true;

        if (i < SRN)
        {
            if (j < SRN)
                j = SRN;
        }
        else if (i < N-SRN)
        {
            if (j==(int)(i/SRN)*SRN)
                j =  j + SRN;
        }
        else
        {
            if (j == N-SRN)
            {
                i = i + 1;
                j = 0;
                if (i>=N)
                    return true;
            }
        }

        for (int num = 1; num<=N; num++)
        {
            if (CheckIfSafe(i, j, num))
            {
                mat[i][j] = num;
                if (fillRemaining(i, j+1))
                    return true;

                mat[i][j] = 0;
            }
        }
        return false;
    }

    public void removeKDigits()
    {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                solution[row][col] = mat[row][col];
            }
        }

        //System.out.println("in remove k digits: " + Arrays.deepToString(solution));
        int count = K;
        while (count != 0)
        {
            int cellId = randomGenerator(N*N)-1;

            // System.out.println(cellId);
            // extract coordinates i  and j
            int i = (cellId/N);
            int j = cellId%9;

            if (mat[i][j] != 0)
            {
                count--;
                mat[i][j] = 0;
            }
        }
        //System.out.println("after remove k digits: " + Arrays.deepToString(solution));
    }


}
