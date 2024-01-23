import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.event.*;

public class DisplayContext
{
    // Size of box
    public static final int HEIGHT = 800;
    public static final int WIDTH = 600;

    // Clearance at the top of the area.
    public static final int TOPCLEAR = 20;
    
    // Stuff you need to display stuff.
    private Group root;
    private Scene scene;

    // The push button.
    private ButtonBox genCirc;

    public DisplayContext(Stage primaryStage)
    {
        // Create a group to hold our shapes.
        root = new Group();

        // Create the shape-maker object.
        genCirc = new ButtonBox(this);

        // Create the scene and display the group holding the circles.
        scene = new Scene(root, WIDTH, HEIGHT, Color.WHITE);
        primaryStage.setTitle("Shapes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //
    void insert(Node n) {
        root.getChildren().add(n);
    }
}
