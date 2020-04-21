import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.GameBoard;

/**
 * @author Ipro
 * Date  18/04/2020
 */
public class Sudoku extends Application {

    private static int minWidth = 970;
    private static int minHeight = 670;

    /* modify the method declaration to throw generic Exception (in case any of the steps fail) */
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
