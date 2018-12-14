package Controll;

import Model.Arena;
import Model.Board;
import Tools.DialogReader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import Tools.LogWriter;

import java.util.List;

public class MainController {
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
    private Label tournamentText;
    @FXML
    private Button btnChoiceDuel;
    @FXML
    private Button btnChoiceTournament;


    @FXML
    private DuelBarController duelBarController;
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

    private Board board;
    private Arena arena;
    private int boardSize = 5;
    private boolean isControlled = false;

    @FXML
    public void initialize(){
        board = new Board(Integer.parseInt(btnChangeBoardSize.getText()), canvas);
        board.draw();

    }
    public void fillBoardButtonPressed(){
        setButtonsDisable(true);

        canvas.setOnMouseClicked(event -> {
            double x = event.getX(), y = event.getY();
            board.setStartPoint(x,y);
        });
    }
    public void acceptButtonPressed(){
        setButtonsDisable(false);
        canvas.setOnMouseClicked(event -> {

        });
    }
    public void clearButtonPressed(){
        board.clearFromPoints();
    }
    public void randomButtonPressed(){
        board.setRandomStartPoints();
        acceptButtonPressed();
    }

    public void changeSizeButtonPressed() {
        int boardSize = DialogReader.readNumberFromDialog("Board size", "Set board size", 5, 3, 50);
        if (boardSize >= 3 && boardSize <= 50) {
            this.boardSize = boardSize;
            btnChangeBoardSize.setText("" + boardSize);
            board = new Board(Integer.parseInt(btnChangeBoardSize.getText()), canvas);
            board.draw();
        }
    }

    public void bntStartPressed(){
        System.out.println(tournamentText);
        board.clean();
        tournamentText.setText("start pressed");
        controlCheckBox.setDisable(true);

        if(controlCheckBox.isSelected() && duelBar.isVisible()) {
            btnNextMove.setDisable(false);
            isControlled = true;
        } else {
            btnNextMove.setDisable(true);
            isControlled = false;
        }
        arena = new Arena(this);
        arena.start();
    }

    private void switchBars(BarController fromBar, Button fromButton, BarController toBar, Button toButton){
        fromBar.setVisible(false);
        toBar.setVisible(true);
        /* button shadow */
        fromButton.getStyleClass().removeAll("doubleShadowed");
        fromButton.getStyleClass().addAll("doubleShadowed");
        toButton.getStyleClass().removeAll("doubleShadowed");
    }

    public void tournamentChoicePressed(){
        switchBars(duelBarController, btnChoiceDuel, tournamentBarController, btnChoiceTournament);
        controlCheckBox.setDisable(true);
    }

    public void duelChoicePressed(){
        switchBars(tournamentBarController, btnChoiceTournament, duelBarController, btnChoiceDuel);
        controlCheckBox.setDisable(false);
    }
    public void tournamentEnded(){
        btnNextMove.setDisable(true);
        if(duelBar.isVisible()){
            controlCheckBox.setDisable(false);
        }
        System.out.println("Arena has just ended");
        Platform.runLater(() -> tournamentText.setText(arena.buildScoreTable()));

        // TODO check if Log is working conrrectly here
        LogWriter log = new LogWriter("tournamentLog");
        log.writeTournamentList(arena.getScoreList());
    }
    @FXML
    public void doNextMove(){
        arena.doNextMove();
        System.out.println("MainController: received doNextMove");
    }
    public Board getBoard(){
        return board;
    }
    public List getDirectories(){
        BarController barController = duelBarController.isVisible()? duelBarController : tournamentBarController;
        return barController.getDirectories();
    }
    public boolean isControlled(){
        return isControlled;
    }

    private void setButtonsDisable(boolean disable){
        startButton.setDisable(disable);
        btnChangeBoardSize.setDisable(disable);
        fillBoardButton.setDisable(disable);
        clearButton.setDisable(!disable);
        acceptButton.setDisable(!disable);
        randomButton.setDisable(!disable);

    }



}
