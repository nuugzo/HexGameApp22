package com.example.hexgameapp22;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GameController {
    private int size;
    private GameModel model;
    private GridPane hexGrid;
    private HexGameApp view;
    private boolean isGameStarted;
    private boolean isSecondPlayerFirstTurn;

    public GameController(int size, GridPane hexGrid, HexGameApp view) {
        this.size = size;
        this.model = new GameModel(size);
        this.hexGrid = hexGrid;
        this.view = view;
        this.isGameStarted = false;  // Oyun başlatılmadı
        this.isSecondPlayerFirstTurn = true;  // İkinci oyuncunun ilk hamlesi
        initializeBoard();
    }

    private void initializeBoard() {
        hexGrid.getChildren().clear();
        hexGrid.setHgap(0);
        hexGrid.setVgap(0);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                HexagonTile tile = new HexagonTile(row, col, this);
                int offsetCol = col + row;
                hexGrid.add(tile, offsetCol, row);
                if (col == 0) {
                    tile.setBorder(Color.RED);
                } else if (col == size - 1) {
                    tile.setBorder(Color.RED);
                } else if (row == 0) {
                    tile.setBorder(Color.BLUE);
                } else if (row == size - 1) {
                    tile.setBorder(Color.BLUE);
                }
            }
        }
    }

    public void setBoardSize(int newSize) {
        this.size = newSize;
        this.model = new GameModel(newSize);
        initializeBoard();
    }

    public void startGame() {
        model.reset();
        view.updateStatusLabel("Player 1's turn (Red)");
        initializeBoard();
        isGameStarted = true;  // Oyun başlatıldı
        isSecondPlayerFirstTurn = true;  // İkinci oyuncunun ilk hamlesi sıfırlanır
    }

    public void handleTileClick(HexagonTile tile) {
        if (!isGameStarted) {
            view.updateStatusLabel("Please start the game first!"); // Oyun başlamadıysa kullanıcıya mesaj göster
            return;
        }
        int row = tile.getRow();
        int col = tile.getCol();
        if (model.getCell(row, col) != GameModel.EMPTY) { // Hücre doluysa
            view.updateStatusLabel("This cell is already occupied!");
            return;
        }
        if (model.makeMove(row, col)) {
            tile.setPlayerColor(model.getCurrentPlayer());
            view.animateTilePlacement(tile);
            if (model.checkWinner()) {
                String winner = model.getCurrentPlayer() == GameModel.PLAYER_ONE ? "Player 1 (Red)" : "Player 2 (Blue)";
                view.updateStatusLabel(winner + " wins!");
                Line winningLine = new Line(0, 0, 800, 600);
                winningLine.setStroke(Color.GREEN);
                view.animateWinningLine(winningLine);
                isGameStarted = false;  // Oyun bittiyse tekrar başlatılması gerekiyor
            } else {
                model.switchPlayer();
                if (isSecondPlayerFirstTurn && model.getCurrentPlayer() == GameModel.PLAYER_TWO) {
                    view.showSwapOption();
                } else {
                    view.updateStatusLabel("Player " + (model.getCurrentPlayer() == GameModel.PLAYER_ONE ? "1 (Red)" : "2 (Blue)") + "'s turn");
                }
            }
        }
    }

    public void handleSwap() {
        if (isSecondPlayerFirstTurn && model.getCurrentPlayer() == GameModel.PLAYER_TWO) {
            int row = model.firstPlayerRow;
            int col = model.firstPlayerCol;
            model.swapStones();
            HexagonTile tile = getTileAt(row, col);
            if (tile != null) {
                tile.setPlayerColor(GameModel.PLAYER_TWO);
            }
            view.updateStatusLabel("Player 1's turn (Red)");
            isSecondPlayerFirstTurn = false;
        }
    }

    public void handleNormalMove() {
        isSecondPlayerFirstTurn = false;
        view.updateStatusLabel("Player 2's turn (Blue)");
    }

    private HexagonTile getTileAt(int row, int col) {
        for (javafx.scene.Node node : hexGrid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col + row) {
                return (HexagonTile) node;
            }
        }
        return null;
    }
}