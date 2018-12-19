package Controll;

import Model.Arena;
import Model.Board;
import Tools.DialogReader;
import Tools.ScoreResult;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private TableView scoreTable;


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
        initScoreTable();


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
    public void clearScoreTable(){
        scoreTable.getItems().clear();
    }
    public void addItemToScoreTable(ScoreResult scoreResult){
        scoreTable.getItems().add(scoreResult);
    }
    public void initScoreTable(){
        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(85);
        idCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, ScoreResult>("id"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(183);
        nameCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, ScoreResult>("name"));

        TableColumn nickCol = new TableColumn("Nick");
        nickCol.setMinWidth(140);
        nickCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, ScoreResult>("nick"));

        TableColumn winsCol = new TableColumn("Wins");
        winsCol.setMinWidth(30);
        winsCol.setPrefWidth(35);
        winsCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, Integer>("wins"));

        TableColumn losesCol = new TableColumn("Loses");
        losesCol.setMinWidth(30);
        losesCol.setPrefWidth(35);
        losesCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, Integer>("loses"));

        TableColumn disqualificationsCol = new TableColumn("Disqualifications");
        disqualificationsCol.setMinWidth(100);
        disqualificationsCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, Integer>("disqualifications"));

        scoreTable.getColumns().addAll(idCol,nameCol,nickCol, winsCol, losesCol,disqualificationsCol);
        scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

}
