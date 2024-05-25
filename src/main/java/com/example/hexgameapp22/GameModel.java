package com.example.hexgameapp22;

import java.util.Arrays;

public class GameModel {
    private int[][] board;
    private int size;
    private int currentPlayer;
    public int firstPlayerRow;
    public int firstPlayerCol;

    public static final int EMPTY = 0;
    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2;

    public GameModel(int size) {
        this.size = size;
        this.board = new int[size][size];
        for (int[] row : board) {
            Arrays.fill(row, EMPTY);
        }
        this.currentPlayer = PLAYER_ONE;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean makeMove(int row, int col) {
        if (board[row][col] == EMPTY) {
            board[row][col] = currentPlayer;
            if (currentPlayer == PLAYER_ONE) {
                firstPlayerRow = row;
                firstPlayerCol = col;
            }
            return true;
        }
        return false;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_ONE) ? PLAYER_TWO : PLAYER_ONE;
    }

    public int getCell(int row, int col) {
        return board[row][col];
    }

    public void reset() {
        for (int[] row : board) {
            Arrays.fill(row, EMPTY);
        }
        currentPlayer = PLAYER_ONE;
    }

    public boolean checkWinner() {
        boolean[][] visited = new boolean[size][size];
        // Check for Player 1 (Red) winning by connecting left to right
        for (int i = 0; i < size; i++) {
            if (board[i][0] == PLAYER_ONE && dfs(i, 0, PLAYER_ONE, visited)) {
                return true;
            }
        }
        // Reset visited array for the next check
        visited = new boolean[size][size];
        // Check for Player 2 (Blue) winning by connecting top to bottom
        for (int i = 0; i < size; i++) {
            if (board[0][i] == PLAYER_TWO && dfs(0, i, PLAYER_TWO, visited)) {
                return true;
            }
        }
        return false;
    }

    private boolean dfs(int row, int col, int player, boolean[][] visited) {
        if ((player == PLAYER_ONE && col == size - 1) || (player == PLAYER_TWO && row == size - 1)) {
            return true;
        }
        visited[row][col] = true;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isValid(newRow, newCol) && !visited[newRow][newCol] && board[newRow][newCol] == player) {
                if (dfs(newRow, newCol, player, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public void swapStones() {
        board[firstPlayerRow][firstPlayerCol] = PLAYER_TWO;
        switchPlayer();
        board[firstPlayerRow][firstPlayerCol] = PLAYER_ONE;
    }
}
