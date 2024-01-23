import java.util.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/*
 * This class is a ShapeMaker which inserts an ellispe of random dimension
 * and color onto the screen.  The Two center points are on a horizontal
 * line, so the ellipse appears square with the display box.
 */

public class RecMaker extends ShapeMaker {
    // Create the object. Send the display context to the base class
    public RecMaker(DisplayContext c) {
        super(c);
    }

    // Generate an ellipse that lies within the box chosen by the base
    // class. We must divide each dimension by 2 since the jfx Ellipse
    // constructor wants a radius rather than a diameter, and move the
    // corner dimensions to the center, also to satisfy the constructor.
    void generate() {
        // Get the dimensions and convert to radii
        double width = dimX();
        double height = dimY();

        // Construct the ellispe centered so that the upper left corner is
        // the point given by base class cornerX() and cornerY()
        Rectangle el = new Rectangle(cornerX() + width, cornerY() + height,
                width, height);

        // Set the fill color.
        el.setFill(fillColor());
        context.insert(el);
    }
}
