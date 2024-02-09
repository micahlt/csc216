import java.util.*;

public class MazeSolver {
    private Maze maze;

    public MazeSolver(Maze maze) {
        this.maze = maze;
    }

    public boolean traverse() {
        boolean done = false;
        int row, column;
        Position pos = new Position();
        Deque<Position> stack = new LinkedList<Position>();
        stack.push(pos);

        while (!done && !stack.isEmpty()) {
            pos = stack.pop();
            maze.tryPosition(pos.getx(), pos.gety());
            if (pos.getx() == maze.getRows() - 1 && pos.gety() == maze.getColumns() - 1) {
                done = true;
            } else {
                push_new_pos(pos.getx() - 1, pos.gety(), stack);
                push_new_pos(pos.getx() + 1, pos.gety(), stack);
                push_new_pos(pos.getx(), pos.gety() - 1, stack);
                push_new_pos(pos.getx(), pos.gety() + 1, stack);
            }
        }
        return done;
    }

    private void push_new_pos(int x, int y, Deque<Position> stack) {
        Position npos = new Position();
        npos.setx(x);
        npos.sety(y);
        if (maze.validPosition(x, y)) {
            stack.push(npos);
        }
    }
}
