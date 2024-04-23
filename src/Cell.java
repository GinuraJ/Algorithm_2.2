import java.util.ArrayList;
import java.util.List;

class Cell {
    int row;
    int col;
    char type;
    List<Cell> neighbors;

    public Cell(int row, int col, char type) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(Cell neighbor) {
        this.neighbors.add(neighbor);
    }
}