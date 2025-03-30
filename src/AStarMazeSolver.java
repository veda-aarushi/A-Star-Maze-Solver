import java.util.*;

public class AStarMazeSolver {
    private static final int[][] grid = {
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1},
            {0, 0, 0, 0, 0}
    };

    private static final int ROWS = grid.length;
    private static final int COLS = grid[0].length;

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    public static class Node {
        public int row, col, gCost, hCost, fCost;
        public Node parent;

        public Node(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void calculateCosts(Node end, int gCostFromStart) {
            this.gCost = gCostFromStart;
            this.hCost = Math.abs(row - end.row) + Math.abs(col - end.col); // Manhattan Distance
            this.fCost = gCost + hCost;
        }
    }

    public static List<Node> findPath(Node start, Node end) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        boolean[][] closedList = new boolean[ROWS][COLS];

        start.calculateCosts(end, 0);
        openList.add(start);

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            closedList[current.row][current.col] = true;

            if (current.row == end.row && current.col == end.col) {
                return reconstructPath(current);
            }

            for (int[] direction : DIRECTIONS) {
                int newRow = current.row + direction[0];
                int newCol = current.col + direction[1];

                if (isValid(newRow, newCol, closedList)) {
                    Node neighbor = new Node(newRow, newCol);
                    int newGCost = current.gCost + 1;

                    neighbor.calculateCosts(end, newGCost);
                    neighbor.parent = current;
                    openList.add(neighbor);
                }
            }
        }
        return Collections.emptyList();
    }

    private static boolean isValid(int row, int col, boolean[][] closedList) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS &&
                grid[row][col] == 0 && !closedList[row][col];
    }

    private static List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        Node start = new Node(0, 0);
        Node end = new Node(4, 4);

        List<Node> path = findPath(start, end);

        if (!path.isEmpty()) {
            System.out.println("Path found:");
            for (Node node : path) {
                System.out.println("(" + node.row + ", " + node.col + ")");
            }
        } else {
            System.out.println("No path found.");
        }
    }
}
