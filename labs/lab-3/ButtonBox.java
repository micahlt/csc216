import java.util.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;

class ButtonBox implements EventHandler<ActionEvent> {
    // The push button.
    private Button genCirc;

    // Button thing.
    ShapeMaker[] em = new ShapeMaker[4];

    // Create the object
    public ButtonBox(DisplayContext c) {
        // Create and place the shape creation button.
        genCirc = new Button("Shape");
        genCirc.setLayoutX(30.0);
        genCirc.setLayoutY(10.0);
        genCirc.setOnAction(this);
        c.insert(genCirc);

        // Create the circle maker.
        em[0] = new RecMaker(c);
        em[1] = new EllipseMaker(c);
        em[2] = new TriMaker(c);
        em[3] = new CenterGrow(c);
    }

    // This is run when the button is pushded
    public void handle(ActionEvent event) {
        Random rand = new Random();
        int index = rand.nextInt(em.length);
        em[index].generate();
    }
}
