import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.*;
import javafx.event.*;

public class ButtonShape extends Application
{
    public void start(Stage primaryStage)
    {
        DisplayContext dcx = new DisplayContext(primaryStage);
    }
    public static void main(String[] args) {
        launch();
    }
}
