import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AStarMazeVisualizer extends Application {

    private static final int ROWS = 15;
    private static final int COLS = 20;
    private static final int TILE_SIZE = 30;

    private Rectangle[][] tiles = new Rectangle[ROWS][COLS];
    private boolean[][] isWall = new boolean[ROWS][COLS];
    private AStarMazeSolver.Node[][] grid = new AStarMazeSolver.Node[ROWS][COLS];

    private AStarMazeSolver.Node startNode = null;
    private AStarMazeSolver.Node endNode = null;

    private enum ClickState { SET_START, SET_END, TOGGLE_WALL }
    private ClickState currentClickState = ClickState.SET_START;

    @Override
    public void start(Stage stage) {
        GridPane root = new GridPane();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                AStarMazeSolver.Node node = new AStarMazeSolver.Node(row, col);
                grid[row][col] = node;

                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setStroke(Color.LIGHTGRAY);
                tile.setFill(Color.WHITE);
                tiles[row][col] = tile;

                int finalRow = row;
                int finalCol = col;
                tile.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        switch (currentClickState) {
                            case SET_START -> {
                                if (startNode != null) tiles[startNode.row][startNode.col].setFill(Color.WHITE);
                                startNode = grid[finalRow][finalCol];
                                tile.setFill(Color.GREEN);
                                currentClickState = ClickState.SET_END;
                            }
                            case SET_END -> {
                                if (endNode != null) tiles[endNode.row][endNode.col].setFill(Color.WHITE);
                                endNode = grid[finalRow][finalCol];
                                tile.setFill(Color.RED);
                                currentClickState = ClickState.TOGGLE_WALL;
                            }
                            case TOGGLE_WALL -> {
                                isWall[finalRow][finalCol] = !isWall[finalRow][finalCol];
                                tile.setFill(isWall[finalRow][finalCol] ? Color.BLACK : Color.WHITE);
                            }
                        }
                    }
                });

                root.add(tile, col, row);
            }
        }

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                runAnimatedAStar();
            } else if (e.getCode() == KeyCode.R) {
                resetGrid();
            } else if (e.getCode() == KeyCode.C) {
                clearPathOnly();
            }
        });

        stage.setTitle("A* Maze Solver Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    private void runAnimatedAStar() {
        if (startNode == null || endNode == null) return;

        clearPathOnly();

        List<AStarMazeSolver.Node> visited = new ArrayList<>();
        List<AStarMazeSolver.Node> path = AStarMazeSolver.findPath(grid, startNode, endNode, isWall, visited);

        Timeline timeline = new Timeline();
        int delay = 20;

        for (int i = 0; i < visited.size(); i++) {
            AStarMazeSolver.Node node = visited.get(i);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * delay), e -> {
                if (!node.equals(startNode) && !node.equals(endNode))
                    tiles[node.row][node.col].setFill(Color.LIGHTBLUE);
            }));
        }

        // ✅ Add final animation to highlight the actual shortest path
        int finalDelay = visited.size() * delay + 200; // Slight delay after animation
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(finalDelay), e -> {
            for (AStarMazeSolver.Node node : path) {
                if (!node.equals(startNode) && !node.equals(endNode)) {
                    tiles[node.row][node.col].setFill(Color.GOLD); // final path
                }
            }
            tiles[startNode.row][startNode.col].setFill(Color.GREEN);
            tiles[endNode.row][endNode.col].setFill(Color.RED);
        }));

        timeline.play();
    }

    private void resetGrid() {
        startNode = null;
        endNode = null;
        currentClickState = ClickState.SET_START;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                tiles[row][col].setFill(Color.WHITE);
                isWall[row][col] = false;
            }
        }
    }

    private void clearPathOnly() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (!isWall[row][col] && !grid[row][col].equals(startNode) && !grid[row][col].equals(endNode)) {
                    tiles[row][col].setFill(Color.WHITE);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
