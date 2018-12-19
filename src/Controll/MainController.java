package Controll;

import Model.Arena;
import Model.Board;
import Tools.DialogReader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import Tools.LogWriter;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class MainController {
    @FXML
    private ScrollPane duelsScrollPane;
    @FXML
    private Slider sizeSlider;
    @FXML
    private AnchorPane duelBar;
    @FXML
    private CheckBox controlCheckBox;

    @FXML
    public AnchorPane mainPane;
    @FXML
    private Button btnChangeBoardSize;
    @FXML
    private Label sizeText;
    @FXML
    private Canvas canvas;
    @FXML
    private TextArea tournamentText;
    @FXML
    private Button btnChoiceDuel;
    @FXML
    private Button btnChoiceTournament;


    @FXML
    public DuelBarController duelBarController;
    @FXML
    private TournamentBarController tournamentBarController;

    @FXML
    private Button btnNextMove;

    @FXML
    private Button acceptButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button fillBoardButton;
    @FXML
    private Button startButton;
    @FXML
    private Button randomButton;
    @FXML
    private CheckBox programUserCheckBox;
    @FXML
    private Button directoryButton;


    private Arena arena;
    private int boardSize = 5;
    private boolean isControlled = false;
    private String humanMove="";
    private boolean humanTurn=false;
    private int humanRectAngle=1;
    private File directory;
    private Board board;
    private int size;

    @FXML
    public void initialize(){
        startButton.setDisable(true);
    }

    @FXML
    private void btnDirectoryPressed() {
        DialogReader dr = new DialogReader();
        directory = dr.readDirectoryFromDialog("Choose Arena directory", mainPane);
        if (directory != null) {
            directoryButton.setText(directory.getName());
            startButton.setDisable(false);
        }
    }

    public void bntStartPressed(){
        if(startButton.getText() == "Stop"){
            arena.interrupt();

        } else {
            startButton.setText("Stop");
            size = (int) sizeSlider.getValue();
            board = new Board(size, canvas);
            board.draw();

            System.out.println(tournamentText);
            tournamentText.setText("Progressing");
            arena = new Arena(this);
            arena.start();
        }
    }
    public void arenaEnded(){
        Platform.runLater(() -> startButton.setText("Start"));
        Platform.runLater(() -> tournamentText.setText("DONE"));
    }

    public File getDirectory() {
        return directory;
    }

    public Board getBoard() {
        return board;
    }

}
