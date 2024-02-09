import java.util.*;
import java.io.*;

public class Maze {
    private static final String TRIED = "X";
    private static final String PATH = "O";
    private int numberRows, numberColumns;
    private String[][] grid;

    /**
     * Loads a maze from a specified textfile
     * 
     * @param filename
     */
    public Maze(String filename) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(filename));
        numberRows = scan.nextInt();
        numberColumns = scan.nextInt();

        grid = new String[numberRows][numberColumns];
        for (int i = 0; i < numberRows; i++) {
            for (int j = 0; j < numberColumns; j++) {
                grid[i][j] = scan.nextInt() == 1 ? " " : "█";
            }
        }
    }

    /**
     * Marks the specified position in the maze as TRIED
     * 
     * @param row - the index of the row to try
     * @param col - the index of the columnn to try
     */
    public void tryPosition(int row, int col) {
        grid[row][col] = TRIED;
    }

    /**
     * Returns the number of rows in thes maze
     * 
     * @return
     */
    public int getRows() {
        return grid.length;
    }

    /**
     * Returns the number of columns in thes maze
     * 
     * @return
     */
    public int getColumns() {
        return grid[0].length;
    }

    /**
     * Marks a given position in the maze as part of the PATH
     * 
     * @param row    - the index of the row to mark as PATH
     * @param column - the index of the column to mark as PATH
     */
    public void markPath(int row, int col) {
        grid[row][col] = PATH;
    }

    /**
     * Determines if a specific location is valid, that is one that is on the grid,
     * not blocked, and has not been TRIED
     * 
     * @param row    - the row to be checked
     * @param column - the column to be checked
     * @return true if the location is valid
     */
    public boolean validPosition(int row, int column) {
        boolean result = false;
        if (row >= 0 && row < grid.length && column >= 0 && column < grid[row].length) {
            if (grid[row][column] == " ") {
                result = true;
            }
        }
        return result;
    }

    public String toString() {
        String result = "\n";
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                result += grid[row][column] + "";
            }
            result += "\n";
        }
        return result;
    }
}