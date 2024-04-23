import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class MapGraph {
    private Cell[][] grid;
    private int rows;
    private int cols;

    public MapGraph(String filename) {
        try {
            // Read map from file and initialize grid
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            List<char[]> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.toCharArray());
            }
            reader.close();

            // Initialize grid
            rows = lines.size();
            cols = lines.get(0).length;
            grid = new Cell[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = new Cell(i, j, lines.get(i)[j]);
                }
            }

            connectCells();
            findShortestPath();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectCells() {
        // Connect each cell with its neighbors
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j].type != '0') {
                    // Add neighbors (up, down, left, right)
                    if (i > 0 && grid[i - 1][j].type != '0') // Up
                        grid[i][j].addNeighbor(grid[i - 1][j]);
                    if (i < rows - 1 && grid[i + 1][j].type != '0') // Down
                        grid[i][j].addNeighbor(grid[i + 1][j]);
                    if (j > 0 && grid[i][j - 1].type != '0') // Left
                        grid[i][j].addNeighbor(grid[i][j - 1]);
                    if (j < cols - 1 && grid[i][j + 1].type != '0') // Right
                        grid[i][j].addNeighbor(grid[i][j + 1]);
                }
            }
        }
    }

    public void findShortestPath() {
        // Find the starting cell 'S'
        Cell startCell = null;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].type == 'S') {
                    startCell = grid[i][j];
                    break;
                }
            }
            if (startCell != null) {
                break;
            }
        }

        if (startCell == null) {
            System.out.println("Starting cell 'S' not found in the grid.");
            return;
        }

        // Perform BFS
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> parentMap = new HashMap<>();
        Set<Cell> visited = new HashSet<>();

        queue.offer(startCell);
        visited.add(startCell);

        while (!queue.isEmpty()) {
            Cell currentCell = queue.poll();

            if (currentCell.type == 'F') {
                // Found the finish cell, reconstruct the path
                List<Cell> path = reconstructPath(parentMap, currentCell);
                printPath(path);
                return;
            }

            // Explore the neighbors in all four directions
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right
            for (int[] direction : directions) {
                int newRow = currentCell.row;
                int newCol = currentCell.col;

                // Slide in the current direction until hitting a wall or rock
                while (isValidCell(newRow + direction[0], newCol + direction[1])) {
                    newRow += direction[0];
                    newCol += direction[1];
                    if (grid[newRow][newCol].type == '0') {
                        break;
                    }
                }

                Cell neighbor = grid[newRow][newCol];
                if (!visited.contains(neighbor)) {
                    queue.offer(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, currentCell);
                }
            }
        }

        System.out.println("No path found from 'S' to 'F'.");
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols && grid[row][col].type != '0';
    }

    private List<Cell> reconstructPath(Map<Cell, Cell> parentMap, Cell endCell) {
        List<Cell> path = new ArrayList<>();
        Cell currentCell = endCell;
        while (currentCell != null) {
            path.add(0, currentCell);
            currentCell = parentMap.get(currentCell);
        }
        return path;
    }

    private void printPath(List<Cell> path) {
        System.out.println("Shortest path from 'S' to 'F':");
        for (int i = 0; i < path.size(); i++) {
            Cell cell = path.get(i);
            System.out.printf("%d. ", i + 1);
            if (i == 0) {
                System.out.printf("Start at (%d,%d)\n", cell.col + 1, cell.row + 1);
            } else if (i == path.size() - 1) {
                Cell prevCell = path.get(i - 1);
                String direction = getDirection(prevCell, cell);
                System.out.printf("Move %s to (%d,%d)\n", direction, cell.col + 1, cell.row + 1);
                System.out.println("Done!");
            } else {
                Cell prevCell = path.get(i - 1);
                String direction = getDirection(prevCell, cell);
                System.out.printf("Move %s to (%d,%d)\n", direction, cell.col + 1, cell.row + 1);
            }
        }
    }

    private String getDirection(Cell prevCell, Cell currentCell) {
        if (currentCell.row < prevCell.row) {
            return "up";
        } else if (currentCell.row > prevCell.row) {
            return "down";
        } else if (currentCell.col < prevCell.col) {
            return "left";
        } else {
            return "right";
        }
    }


}