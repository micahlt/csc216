import java.util.*;
import java.io.*;

public class MazeTester {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the name of the file containing the maze: ");
        String filename = scan.nextLine();

        Maze labyrinth = new Maze(filename);

        System.out.println(labyrinth.toString());

        MazeSolver solver = new MazeSolver(labyrinth);
        if (solver.traverse()) {
            System.out.println("The maze was solved successfully!");
        } else {
            System.out.println("There is no solution");
        }
        System.out.println(labyrinth.toString());
    }
}