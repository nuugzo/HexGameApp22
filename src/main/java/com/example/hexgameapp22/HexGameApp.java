package com.example.hexgameapp22;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;

public class HexGameApp extends Application {
    private static final int DEFAULT_SIZE = 11;
    private GameController controller;
    private BorderPane root;
    private GridPane hexGrid;
    private Label statusLabel;
    private StackPane gamePane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        gamePane = new StackPane();
        hexGrid = new GridPane();
        gamePane.getChildren().add(hexGrid);

        controller = new GameController(DEFAULT_SIZE, hexGrid, this);

        statusLabel = new Label("Player 1's turn (Red)");
        VBox controls = new VBox(10);
        controls.getChildren().addAll(createSizeSelector(), createStartButton(), createInfoButton(), statusLabel);

        root.setCenter(gamePane);
        root.setRight(controls);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Hex Game");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private HBox createSizeSelector() {
        HBox sizeSelector = new HBox(10);
        sizeSelector.getChildren().add(new Label("Select grid size:"));

        ToggleGroup sizeGroup = new ToggleGroup();
        RadioButton size5 = new RadioButton("5x5");
        size5.setToggleGroup(sizeGroup);
        RadioButton size11 = new RadioButton("11x11");
        size11.setToggleGroup(sizeGroup);
        RadioButton size17 = new RadioButton("17x17");
        size17.setToggleGroup(sizeGroup);
        size11.setSelected(true);

        sizeSelector.getChildren().addAll(size5, size11, size17);

        sizeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                String sizeText = ((RadioButton) newToggle).getText();
                int newSize = Integer.parseInt(sizeText.split("x")[0]);
                controller.setBoardSize(newSize);
            }
        });

        return sizeSelector;
    }

    private Button createStartButton() {
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> controller.startGame());
        return startButton;
    }

    private Button createInfoButton() {
        Button infoButton = new Button("Info");
        infoButton.setOnAction(e -> showGameInfo());
        return infoButton;
    }

    private void showGameInfo() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Rules");
        alert.setHeaderText(null);
        alert.setContentText("Each player has an allocated color, conventionally Red and Blue.\n"
                + "Players take turns placing a stone of their color on a single cell within the overall playing board.\n"
                + "The goal for each player is to form a connected path of their own stones linking the opposing sides of the board marked by their colors before their opponent connects his or her sides in a similar fashion.\n"
                + "The first player to complete his or her connection wins the game.\n"
                + "The four corner hexagons each belong to both adjacent sides.");

        alert.showAndWait();
    }

    public void updateStatusLabel(String text) {
        statusLabel.setText(text);
    }

    public void animateTilePlacement(HexagonTile tile) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), tile);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    public void animateWinningLine(Line line) {
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(1));
        pathTransition.setPath(line);
        pathTransition.setNode(line);
        pathTransition.play();
    }

    public void showSwapOption() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Swap Option");
        alert.setHeaderText("Second Player's Choice");
        alert.setContentText("Do you want to swap your stone with the first player's stone?");

        ButtonType swapButton = new ButtonType("Swap");
        ButtonType normalButton = new ButtonType("Normal Move", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(swapButton, normalButton);

        alert.showAndWait().ifPresent(type -> {
            if (type == swapButton) {
                controller.handleSwap();
            } else {
                controller.handleNormalMove();
            }
        });
    }
}
