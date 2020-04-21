package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;
import model.GameBoard;
import model.Point;
import model.Stopwatch;
import view.language.LanguageManager;
import java.net.URL;
import java.util.*;


public class MainWindowController implements Initializable {

/*  *********************************************************************
    *******************   LABELS AND CHOICEBOXES   **********************
    *********************************************************************
 */
    @FXML
    private ComboBox<String> difficultyChoice;
    @FXML
    private Label difficultyLabel;

    @FXML
    private ComboBox<Pair<String,String>> languageChoice;
    @FXML
    private Label languageLabel;

    @FXML
    private ComboBox<String> themeChoice;
    @FXML
    private Label themeLabel;

    @FXML
    private ToggleButton toggleError;
    @FXML
    private Label errorLabel;

    @FXML
    private Label timerLabel;

/*  *********************************************************************
    **********************   NUMBER BUTTONS   ***************************
    *********************************************************************
 */
    @FXML
    private Button nb1;
    @FXML
    private Button nb2;
    @FXML
    private Button nb3;
    @FXML
    private Button nb4;
    @FXML
    private Button nb5;
    @FXML
    private Button nb6;
    @FXML
    private Button nb7;
    @FXML
    private Button nb8;
    @FXML
    private Button nb9;

/*  *********************************************************************
    **********************   CONTROL BUTTONS   **************************
    *********************************************************************
 */
    @FXML
    private Button newGameButton;

    @FXML
    private ToggleButton togglePencil;
    @FXML
    private ToggleButton toggleEraser;

    @FXML
    private Button hint;
    @FXML
    private ToggleButton togglePause;


    private int icon_size = 60;

    private ImageView idea_base_icon = new ImageView("view/images/idea.png");

    private ImageView idea_white_icon = new ImageView("view/images/idea_white.png");

/*  *********************************************************************
    **********************   SUDOKU GRID GUI   **************************
    *********************************************************************
 */
    @FXML
    private GridPane root;

    @FXML
    private GridPane sudokuGrid;
    private Node selectedCell = null;

/*  *********************************************************************
    **********************   EVENT LISTENERS   **************************
    *********************************************************************
 */

    private ChangeListener<Pair<String,String>> languageChoiceListener;
    private ChangeListener<String> difficultyChoiceListener;
    private ChangeListener<String> themeChoiceListener;

/*  *********************************************************************
    **********************   FUNCTIONALITIES   **************************
    *********************************************************************
 */
    private LanguageManager languageManager;
    private ArrayList<Button> numberButtons = new ArrayList<>();

    private GameBoard board ;
    private Stopwatch time;

    private final String errorStyle = "-fx-background-color: rgba(255,0,0,0.5)";

    private boolean isPlaying = false;

/*  *********************************************************************
    ****************************   METHODS   ****************************
    *********************************************************************
 */

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create and bind event listeners to each ComboBox
        this.createEventListeners();
        languageChoice.getSelectionModel().selectedItemProperty().addListener(languageChoiceListener);
        difficultyChoice.getSelectionModel().selectedItemProperty().addListener(difficultyChoiceListener);
        themeChoice.getSelectionModel().selectedItemProperty().addListener(themeChoiceListener);

        // Define the languageManager with the default language
        languageManager = new LanguageManager("fr");
        this.applyLanguage(languageManager);

        // Modify ChoiceBox Display to show flags with language name
        this.modifyChoiceBoxDisplay();

        // Initialize button's icon size
        idea_base_icon.setFitHeight(icon_size); idea_base_icon.setFitWidth(icon_size);
        idea_white_icon.setFitHeight(icon_size); idea_white_icon.setFitWidth(icon_size);

        // Initialize the stopwatch
        this.time = new Stopwatch();
        // display the time each seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            this.timerLabel.setText(time.getTime());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    };

    private void modifyChoiceBoxDisplay() {
        languageChoice.setCellFactory(new Callback<ListView<Pair<String, String>>, ListCell<Pair<String, String>>>() {
            @Override
            public ListCell<Pair<String, String>> call(ListView<Pair<String, String>> pairListView) {
                return new ListCell<Pair<String,String>>(){
                    @Override
                    protected void updateItem(Pair<String, String> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(languageManager.get("language."+item.getKey()));
                            ImageView icon = new ImageView("view/language/flags/"+item.getValue());
                            icon.setFitWidth(40);
                            icon.setFitHeight(40);
                            setGraphic(icon);
                        }
                    }
                };
            }
        });
        languageChoice.setButtonCell(new ListCell<Pair<String,String>>(){
            @Override
            protected void updateItem(Pair<String, String> item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(languageManager.get("language."+item.getKey()));
                    ImageView icon = new ImageView("view/language/flags/"+item.getValue());
                    icon.setFitWidth(40);
                    icon.setFitHeight(40);
                    setGraphic(icon);
                }
            }
        });
    }

    private void createEventListeners() {

        languageChoiceListener = new ChangeListener<Pair<String, String>>() {
            @Override
            public void changed(ObservableValue<? extends Pair<String, String>> observableValue, Pair<String, String> old_value, Pair<String, String> new_value) {
                if (old_value.getKey() != new_value.getKey()) {
                    String locale = languageManager.getLocale(new_value.getKey());
                    languageManager = new LanguageManager(locale);
                    applyLanguage(languageManager);
                }
            }
        };

        difficultyChoiceListener = new ChangeListener<String>() {
            public void changed(ObservableValue ov, String old_value, String new_value) {
                if (old_value != new_value) {
                    String key = languageManager.getKey(new_value);
                    // Remove the "difficulty."
                    key = key.substring(11);
                }
            }
        };

        themeChoiceListener = new ChangeListener<String>() {
            public void changed(ObservableValue ov, String old_value, String new_value) {
                if (old_value != new_value) {
                    String old_key = languageManager.getKey(old_value);
                    String new_key = languageManager.getKey(new_value);

                    // remove the "theme."
                    old_key = old_key.substring(6);
                    new_key = new_key.substring(6);

                    if (new_key.substring(0,4).equals("dark") && old_key.substring(0,5).equals("light") ){
                        // Going from a light theme to a dark theme,
                        // So we need to change the icons for better contrast
                        hint.setGraphic(idea_white_icon);

                    } else if (new_key.substring(0,5).equals("light") && old_key.substring(0,4).equals("dark")) {
                        // going from a dark theme to a light theme
                        hint.setGraphic(idea_base_icon);
                    }

                    // remove the old style and apply the new one
                    root.getStyleClass().remove(old_key);
                    root.getStyleClass().add(new_key);
                }
            }
        };
    }


    private void applyLanguage(LanguageManager lang) {

        // Remove Listeners to avoid looping indefinitely while modifying each ChoiceBox
        languageChoice.getSelectionModel().selectedItemProperty().removeListener(languageChoiceListener);
        difficultyChoice.getSelectionModel().selectedItemProperty().removeListener(difficultyChoiceListener);
        themeChoice.getSelectionModel().selectedItemProperty().removeListener(themeChoiceListener);

        // Saving the current difficulty to not be reset
        int current_difficulty = difficultyChoice.getSelectionModel().getSelectedIndex();

        // Dificulty
        difficultyLabel.setText(lang.get("difficulty.label") + " : ");
        difficultyLabel.setLabelFor(difficultyChoice);
        difficultyChoice.getItems().clear();

        for (String difficulty : lang.getDifficulties()) {
            difficultyChoice.getItems().add(difficulty);
        }
        // Set back the selected value or initialize it to the first value of the list
        if (current_difficulty != -1)
            difficultyChoice.getSelectionModel().select(current_difficulty);
        else {
            difficultyChoice.getSelectionModel().select(0);
        }

        // Language
        languageLabel.setText(lang.get("lang") + " : ");
        languageLabel.setLabelFor(languageChoice);
        languageChoice.getItems().clear();

        int selected_language = 0;

        Pair<String,String>[] iconLanguages = languageManager.getLanguageListCell();

        for (int i = 0; i < iconLanguages.length; i++) {
            languageChoice.getItems().add(iconLanguages[i]);
            if (lang.getLanguage().equals(iconLanguages[i].getKey()))
                selected_language = i;
        }
        languageChoice.getSelectionModel().select(selected_language);

        // Themes
        String[] themes = languageManager.getThemes();
        int current_theme = themeChoice.getSelectionModel().getSelectedIndex();
        themeChoice.getItems().clear();
        for (int i = 0; i < themes.length; i++) {
            themeChoice.getItems().add(themes[i]);
        }
        if (current_difficulty != -1)
            themeChoice.getSelectionModel().select(current_theme);
        else {
            themeChoice.getSelectionModel().select(0);
        }

        // Buttons
        if (isPlaying) {
            newGameButton.setText(lang.get("give_up"));
        } else {
            newGameButton.setText(lang.get("new_game"));
        }

        themeLabel.setText(lang.get("theme") +" : ");
        errorLabel.setText(lang.get("errors") +" : ");
        togglePencil.setText(lang.get("marks"));
        hint.setText(lang.get("hint"));
        togglePause.setText(lang.get("pause"));
        toggleEraser.setText(lang.get("erase"));

        // Put back the defined Listeners
        languageChoice.getSelectionModel().selectedItemProperty().addListener(languageChoiceListener);
        difficultyChoice.getSelectionModel().selectedItemProperty().addListener(difficultyChoiceListener);
        themeChoice.getSelectionModel().selectedItemProperty().addListener(themeChoiceListener);
    }

    @FXML
    private void onSudokuCellClick(MouseEvent event){
        if (!togglePause.isSelected()) {
            Node clickedNode = event.getPickResult().getIntersectedNode();
            if (clickedNode.getTypeSelector().equals("LabeledText")) {
                clickedNode = clickedNode.getParent();
            }
            if (this.selectedCell != null)
                this.selectedCell.getStyleClass().remove("selected");
            this.selectedCell = clickedNode;
            this.selectedCell.getStyleClass().add("selected");

            if (toggleEraser.isSelected()) {
                clearNumber();
            }
        }

    }
    @FXML
    private void onNewGameClick(){
        // Clear selected cell
        if (this.selectedCell != null) {
            this.selectedCell.getStyleClass().remove("selected");
            this.selectedCell = null;
        }

        if (!isPlaying) {
            newGame();
        } else {
            time.stop();
            int answer= askForGiveUp();
            if (answer == 0 || answer == 1){
                giveUp(answer);
            } else {
                 time.start();
            }
        }
    }

    private void newGame() {
        // Create new Sudoku board
        String difficulty = this.languageManager.getKey(difficultyChoice.getSelectionModel().getSelectedItem());
        difficulty = difficulty.substring(11);
        this.board = new GameBoard(difficulty);
        this.displayBoard();

        // Start timer
        time.reset();
        time.start();

        this.isPlaying = true;
        this.newGameButton.setText(languageManager.get("give_up"));
    }

    private int askForGiveUp() {
        int answer =2;

        // get current theme
        String themeClass = languageManager.getKey(themeChoice.getSelectionModel().getSelectedItem());
        themeClass = themeClass.substring(6);

        // Create the Alert dialog
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);

        // define style classes and the stylesheet
        dialog.getDialogPane().getStylesheets().add("view/style.css");
        dialog.getDialogPane().getStyleClass().addAll(themeClass, "dialog");

        // Apply translated title and contents
        dialog.setTitle(languageManager.get("give_up_popup.title"));
        dialog.setHeaderText(languageManager.get("give_up_popup.message"));
        // Set the buttons set
        ButtonType yesButton = new ButtonType(languageManager.get("give_up_popup.yes"), ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType(languageManager.get("give_up_popup.no"), ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType(languageManager.get("give_up_popup.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        // Define the window icon
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("view/images/sudoku.png"));
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == yesButton) {
            answer =0;
        } else if (result.get() == noButton) {
            answer =1;
        }
        return answer;
    }

    private void giveUp(int answer) {
        time.reset();
        this.isPlaying = false;
        this.newGameButton.setText(languageManager.get("new_game"));
        if (answer == 0) {
            displaySolution();
        } else {
            clearGrid();
        }
    }

    private void win() {
        displayWinPopup();
        this.newGameButton.setText(languageManager.get("new_game"));
        time.reset();
        isPlaying = false;
    }

    private void displayWinPopup() {
        String themeClass = languageManager.getKey(themeChoice.getSelectionModel().getSelectedItem());
        themeClass = themeClass.substring(6);
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.getDialogPane().getStylesheets().add("view/style.css");
        dialog.getDialogPane().getStyleClass().addAll(themeClass, "dialog");
        dialog.setTitle(languageManager.get("win_popup.title"));
        dialog.setHeaderText(languageManager.get("win_popup.message") + time.getTime());
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("view/images/sudoku.png"));
        dialog.show();
    }

    private void displayCompleteButWrongPopup() {
        String themeClass = languageManager.getKey(themeChoice.getSelectionModel().getSelectedItem());
        themeClass = themeClass.substring(6);
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.getDialogPane().getStylesheets().add("view/style.css");
        dialog.getDialogPane().getStyleClass().addAll(themeClass, "dialog");
        dialog.setTitle(languageManager.get("complete_Wrong_popup.title"));
        dialog.setHeaderText(languageManager.get("complete_Wrong_popup.message"));
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("view/images/sudoku.png"));
        dialog.show();
    }

    private void displaySolution() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                selectedCell = this.getCellLabel(i, j);
                if (this.board.getSolvedBoard()[i][j] !=0) {
                    ((Label)selectedCell).setText(""+this.board.getSolvedBoard()[i][j]);
                    selectedCell.setDisable(true);
                    clearMarks();
                }
            }
        }
    }

    private void clearGrid(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                selectedCell = this.getCellLabel(i, j);
                ((Label)selectedCell).setText("");
                selectedCell.setDisable(false);
                clearMarks();
            }
        }
    }

    private void displayBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Label cell = this.getCellLabel(i, j);
                if (this.board.getCell(i,j) !=0) {
                    cell.setText(""+this.board.getCell(i,j));
                    cell.setDisable(true);
                } else {
                    cell.setText("");
                    cell.setDisable(false);
                }
            }
        }
    }

    private Point getAbsoluteCoordinates() {
        int row = (GridPane.getRowIndex(selectedCell.getParent()) == null) ? 0 : GridPane.getRowIndex(selectedCell.getParent());
        int col = (GridPane.getColumnIndex(selectedCell.getParent()) == null) ? 0 : GridPane.getColumnIndex(selectedCell.getParent());
        int miniGridNode_row = (GridPane.getRowIndex(selectedCell) == null) ? 0 : GridPane.getRowIndex(selectedCell);
        int miniGridNode_col = (GridPane.getColumnIndex(selectedCell) == null) ? 0 : GridPane.getColumnIndex(selectedCell);

        return new Point(row*3 + miniGridNode_row, col*3 + miniGridNode_col);
    }

    private Label getCellLabel(int row, int col) {
        for(Node miniGridNode : sudokuGrid.getChildren()) {
            if (miniGridNode.getTypeSelector().equals("GridPane")) {
                int miniGridNode_row = (GridPane.getRowIndex(miniGridNode) == null) ? 0 : GridPane.getRowIndex(miniGridNode);
                int miniGridNode_col = (GridPane.getColumnIndex(miniGridNode) == null) ? 0 : GridPane.getColumnIndex(miniGridNode);
                if (miniGridNode_row == row/3 && miniGridNode_col == col/3) {
                    for (Node cellNode : ((GridPane)miniGridNode).getChildren() ) {
                        // For each marks Cell
                        int cellNode_row = (GridPane.getRowIndex(cellNode) == null) ? 0 : GridPane.getRowIndex(cellNode);
                        int cellNode_col = (GridPane.getColumnIndex(cellNode) == null) ? 0 : GridPane.getColumnIndex(cellNode);
                        if (cellNode_row == row%3 && cellNode_col == col%3) {
                            return (Label)cellNode;
                        }
                    }
                }
            }
        }
        return null;
    }

    @FXML
    private void onToggleErrorDisplay() {
        if (toggleError.isSelected()) {
            // Show error style on each erroneous cell
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Label cellLabel = getCellLabel(i,j);
                    if (!cellLabel.getText().isBlank()) {
                        int cell_intValue = Integer.parseInt(cellLabel.getText());
                        if (cell_intValue != this.board.getSolvedBoard()[i][j])
                            cellLabel.setStyle(errorStyle);
                    }
                }
            }
        } else {
            // Remove error style
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    getCellLabel(i,j).setStyle(null);
                }
            }
        }
    }

    @FXML
    private void onToggleEraserClick() {
        if (!togglePause.isSelected()) {
            if (togglePencil.isSelected())
                togglePencil.setSelected(false);

            if (toggleEraser.isSelected()) {
                this.clearNumber();
            }
        }
    }

    @FXML
    private void onTogglePencilClick() {
        if (toggleEraser.isSelected())
            toggleEraser.setSelected(false);
    }

    @FXML
    private void onTogglePauseClick(){
        if (togglePause.isSelected()) {
            time.stop();
        } else {
            time.start();
        }
    }

    @FXML
    private void onHintClick() {
        if (!togglePause.isSelected() && isPlaying) {
            Point coordinates = board.getRandomCell();
            if (coordinates !=null) {
                if (selectedCell != null)
                    selectedCell.getStyleClass().remove("selected");
                selectedCell = getCellLabel(coordinates.getX(), coordinates.getY());
                numberPressed(this.board.getSolvedBoard()[coordinates.getX()][coordinates.getY()]);
                selectedCell.getStyleClass().add("selected");
            }
        }
    }

    private void updateMark(int nb) {
        int mark_col = (nb-1)%3;
        int mark_row = (nb-1)/3;

        int row = (GridPane.getRowIndex(selectedCell) == null) ? 0 : GridPane.getRowIndex(selectedCell);
        int col = (GridPane.getColumnIndex(selectedCell) == null) ? 0 : GridPane.getColumnIndex(selectedCell);

        for(Node miniGridNode : ((GridPane)selectedCell.getParent()).getChildren() ){
            // For each node in a miniGrid
            if (miniGridNode.getTypeSelector().equals("GridPane")) {
                int miniGridNode_row = (GridPane.getRowIndex(miniGridNode) == null) ? 0 : GridPane.getRowIndex(miniGridNode);
                int miniGridNode_col = (GridPane.getColumnIndex(miniGridNode) == null) ? 0 : GridPane.getColumnIndex(miniGridNode);
                if (row == miniGridNode_row && col == miniGridNode_col) {
                    for (Node cellNode : ((GridPane)miniGridNode).getChildren() ) {
                        // For each marks Cell
                        int cellNode_row = (GridPane.getRowIndex(cellNode) == null) ? 0: GridPane.getRowIndex(cellNode);
                        int cellNode_col = (GridPane.getColumnIndex(cellNode) == null) ? 0: GridPane.getColumnIndex(cellNode);

                        if (cellNode_row == mark_row && cellNode_col == mark_col) {
                            if (((Label)cellNode).getText().isBlank()) {
                                ((Label)cellNode).setText(""+nb);
                            } else {
                                ((Label)cellNode).setText("");
                            }
                        }
                    }
                }
            }
        }
    }

    private void clearMarks() {
        int row = (GridPane.getRowIndex(selectedCell) == null) ? 0 : GridPane.getRowIndex(selectedCell);
        int col = (GridPane.getColumnIndex(selectedCell) == null) ? 0 : GridPane.getColumnIndex(selectedCell);
        for(Node miniGridNode : ((GridPane)selectedCell.getParent()).getChildren() ){
            // For each node in a miniGrid
            if (miniGridNode.getTypeSelector().equals("GridPane")) {
                int miniGridNode_row = (GridPane.getRowIndex(miniGridNode) == null) ? 0 : GridPane.getRowIndex(miniGridNode);
                int miniGridNode_col = (GridPane.getColumnIndex(miniGridNode) == null) ? 0 : GridPane.getColumnIndex(miniGridNode);
                if (row == miniGridNode_row && col == miniGridNode_col) {
                    for (Node cellNode : ((GridPane)miniGridNode).getChildren() ) {
                        ((Label)cellNode).setText("");
                    }
                }
            }
        }
    }

    private void clearNumber() {
        if (this.selectedCell != null) {
            ((Label)selectedCell).setText("");
            selectedCell.setStyle(null);
            Point p = getAbsoluteCoordinates();
            this.board.setCell(0, p.getX(), p.getY());
        }
    }

    private void numberPressed(int nb) {
        if (!togglePause.isSelected() && selectedCell != null) {
            if (togglePencil.isSelected()) {
                if (((Label)selectedCell).getText().isEmpty()) {
                    this.updateMark(nb);
                }
            } else {
                clearMarks();
                ((Label) selectedCell).setText("" + nb);
                Point p = getAbsoluteCoordinates();
                if (toggleError.isSelected()) {
                    for (Node miniGridNode : ((GridPane)selectedCell.getParent()).getChildren()) {
                        // For each node in a miniGrid
                        if (miniGridNode.getTypeSelector().equals("Label")) {
                            if (this.board.getSolvedBoard()[p.getX()][p.getY()] != nb) {
                                selectedCell.setStyle(errorStyle);
                            } else {
                                selectedCell.setStyle(null);
                            }
                        }
                    }
                }
                this.board.setCell(nb, p.getX(), p.getY());
                if (board.isFull()) {
                    if (board.isComplete()){
                        win();
                    } else {
                        displayCompleteButWrongPopup();
                    }
                }
            }
        }
    }

    @FXML
    private void onNb1Click() {
        this.numberPressed(1);
    }
    @FXML
    private void onNb2Click() {
        this.numberPressed(2);
    }
    @FXML
    private void onNb3Click() {
        this.numberPressed(3);
    }
    @FXML
    private void onNb4Click() {
        this.numberPressed(4);
    }
    @FXML
    private void onNb5Click() {
        this.numberPressed(5);
    }
    @FXML
    private void onNb6Click() {
        this.numberPressed(6);
    }
    @FXML
    private void onNb7Click() {
        this.numberPressed(7);
    }
    @FXML
    private void onNb8Click() {
        this.numberPressed(8);
    }
    @FXML
    private void onNb9Click() {
        this.numberPressed(9);
    }

    @FXML
    private void onKeyBoardPressed(KeyEvent event) {
        switch (event.getCode()) {
            case BACK_SPACE: case DELETE:
                this.toggleEraser.fire();
                this.onToggleEraserClick();
                break;
            case H: case E:
                this.hint.fire();
                this.onHintClick();
                break;
            case SHIFT: case CONTROL:
                this.togglePencil.fire();
                this.onTogglePencilClick();
                break;
            case ENTER: case P:
                this.togglePause.fire();
                break;
            case DIGIT1: case NUMPAD1:
                this.numberPressed(1);
                break;
            case DIGIT2: case NUMPAD2:
                this.numberPressed(2);
                break;
            case DIGIT3: case NUMPAD3:
                this.numberPressed(3);
                break;
            case DIGIT4: case NUMPAD4:
                this.numberPressed(4);
                break;
            case DIGIT5: case NUMPAD5:
                this.numberPressed(5);
                break;
            case DIGIT6: case NUMPAD6:
                this.numberPressed(6);
                break;
            case DIGIT7: case NUMPAD7:
                this.numberPressed(7);
                break;
            case DIGIT8: case NUMPAD8:
                this.numberPressed(8);
                break;
            case DIGIT9: case NUMPAD9:
                this.numberPressed(9);
                break;
        }
    }
}
