import java.util.*;

public class AStarMazeSolver {

    public static class Node {
        public int row, col, gCost, hCost, fCost;
        public Node parent;

        public Node(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void calculateCosts(Node end, int gCostFromStart) {
            this.gCost = gCostFromStart;
            this.hCost = Math.abs(row - end.row) + Math.abs(col - end.col);
            this.fCost = gCost + hCost;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) return false;
            Node other = (Node) obj;
            return this.row == other.row && this.col == other.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    public static List<Node> findPath(Node[][] grid, Node start, Node end, boolean[][] isWall, List<Node> visitedNodes) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        boolean[][] closedList = new boolean[grid.length][grid[0].length];

        start.calculateCosts(end, 0);
        openList.add(start);

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            closedList[current.row][current.col] = true;
            visitedNodes.add(current);

            if (current.equals(end)) {
                return reconstructPath(current);
            }

            for (int[] dir : new int[][]{{-1,0}, {1,0}, {0,-1}, {0,1}}) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (isValid(newRow, newCol, grid.length, grid[0].length, closedList, isWall)) {
                    Node neighbor = new Node(newRow, newCol);
                    neighbor.calculateCosts(end, current.gCost + 1);
                    neighbor.parent = current;

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private static boolean isValid(int row, int col, int rows, int cols, boolean[][] closedList, boolean[][] isWall) {
        return row >= 0 && row < rows && col >= 0 && col < cols && !closedList[row][col] && !isWall[row][col];
    }

    private static List<Node> reconstructPath(Node end) {
        List<Node> path = new ArrayList<>();
        Node current = end;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
