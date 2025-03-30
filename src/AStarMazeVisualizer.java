import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;

public class AStarMazeVisualizer extends Application {

    private static final int TILE_SIZE = 60;

    private static final int[][] grid = {
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1},
            {0, 0, 0, 0, 0}
    };

    private static final int ROWS = grid.length;
    private static final int COLS = grid[0].length;
    private Rectangle[][] tiles = new Rectangle[ROWS][COLS];

    @Override
    public void start(Stage stage) {
        GridPane root = new GridPane();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setStroke(Color.GRAY);
                tile.setFill(grid[row][col] == 1 ? Color.BLACK : Color.WHITE);
                tiles[row][col] = tile;
                root.add(tile, col, row);
            }
        }

        Scene scene = new Scene(root);
        stage.setTitle("A* Maze Solver Visualizer");
        stage.setScene(scene);
        stage.show();

        runSolver();
    }

    private void runSolver() {
        AStarMazeSolver.Node start = new AStarMazeSolver.Node(0, 0);
        AStarMazeSolver.Node end = new AStarMazeSolver.Node(4, 4);

        List<AStarMazeSolver.Node> path = AStarMazeSolver.findPath(start, end);

        for (AStarMazeSolver.Node node : path) {
            if ((node.row == 0 && node.col == 0) || (node.row == 4 && node.col == 4)) {
                continue;
            }
            tiles[node.row][node.col].setFill(Color.GOLD);  // Path
        }

        tiles[0][0].setFill(Color.GREEN); // Start
        tiles[4][4].setFill(Color.RED);   // End
    }

    public static void main(String[] args) {
        launch(args);
    }
}
