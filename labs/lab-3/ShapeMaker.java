import java.util.*;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.event.*;

abstract public class ShapeMaker
{
    // The min and max size of the shapes.
    public static final int MINSIZE = 20;
    public static final int MAXSIZE = 100;

    // Clearance at the top.
    public static final int CLRTOP = 40;
    
    // The location and size of the area where the object 
    protected int locx, locy, height, width;

    // This is useful
    protected static Random rand = new Random();

    // Construct the shape maker given x and y limits.
    DisplayContext context;
    public ShapeMaker(DisplayContext dc) {
        context = dc;
    }

    // Set a position and size at random.  These can be changed, though.
    protected int cornerX() { return rand.nextInt(context.WIDTH - MAXSIZE); }
    protected int cornerY() {
        return rand.nextInt(context.HEIGHT - MAXSIZE - CLRTOP) + CLRTOP;
    }
    protected int dimX() {
        return rand.nextInt(MAXSIZE - MINSIZE) + MINSIZE;
    }
    protected int dimY() {
        return rand.nextInt(MAXSIZE - MINSIZE) + MINSIZE;
    }

    // Generate a color at random
    protected Color fillColor()
    {
        return Color.rgb(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
    }

    // Insert a shape of the specified type;
    abstract void generate();
}
