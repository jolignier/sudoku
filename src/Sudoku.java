import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Date  18/04/2020 <br>
 * Main application of the Sudoku who launch the main window
 * Designed with an MVC pattern
 */
public class Sudoku extends Application {

    private static int minWidth = 970;
    private static int minHeight = 670;

    /**
     * @param primaryStage the primary window to launch on application start
     * @throws Exception any exception from the mainWindow
     */
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("view/MainWindow.fxml"));
        Scene scene = new Scene(root, minWidth, minHeight);

        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(minWidth);
        primaryStage.setMinHeight(minHeight);
        primaryStage.show();
        primaryStage.getIcons().add(new Image("view/images/sudoku.png"));

    }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
