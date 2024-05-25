package com.example.hexgameapp22;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class HexagonTile extends StackPane {
    private int row;
    private int col;
    private GameController controller;
    private Polygon hexagon;
    private static final double HEX_SIZE = 15.0;

    public HexagonTile(int row, int col, GameController controller) {
        this.row = row;
        this.col = col;
        this.controller = controller;
        this.hexagon = createHexagon();
        this.getChildren().add(hexagon);
        this.setOnMouseClicked(e -> controller.handleTileClick(this));
    }

    public int getRow() {

        return row;
    }

    public int getCol() {

        return col;
    }

    private Polygon createHexagon() {
        double height = Math.sqrt(3) * HEX_SIZE;
        Polygon hex = new Polygon();
        hex.getPoints().addAll(
                0.0, HEX_SIZE,
                HEX_SIZE * 0.5, 0.0,
                HEX_SIZE * 1.5, 0.0,
                2.0 * HEX_SIZE, HEX_SIZE,
                HEX_SIZE * 1.5, 2.0 * HEX_SIZE,
                HEX_SIZE * 0.5, 2.0 * HEX_SIZE
        );
        hex.setFill(Color.WHITE);
        hex.setStroke(Color.BLACK);
        return hex;
    }

    public void setPlayerColor(int player) {
        if (player == GameModel.PLAYER_ONE) {
            hexagon.setFill(Color.RED);
        } else if (player == GameModel.PLAYER_TWO) {
            hexagon.setFill(Color.BLUE);
        }
    }

    public void setBorder(Color color) {

        hexagon.setStroke(color);
    }
}