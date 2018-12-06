package Controll;


import GUI.BoardDraw;
import Tools.DialogReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import Model.Tournament;
import Model.Player;
import Tools.LogWriter;

public class MainController {
    @FXML
    private AnchorPane duelBar;

    @FXML
    public AnchorPane mainPane;
    @FXML
    private Button changeSizeButton;
    @FXML
    private Label sizeText;
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane boardPane;
    @FXML
    private Label winnerText;
    @FXML
    private Label tournamentText;
    @FXML
    private Button btnChoiceDuel;
    @FXML
    private Button btnChoiceTournament;


    Player winner;
    int boardSize = 5;
    private BoardDraw boardDraw;

    @FXML
    private DuelBarController duelBarController;
    @FXML
    private TournamentBarController tournamentBarController;

    @FXML
    private RadioButton controlOption;
    @FXML
    private RadioButton instantOption;
    @FXML
    private Button nextMoveButton;

    public void changeSizeButtonPressed() {
        int boardSize = DialogReader.readNumberFromDialog("Board size", "Set board size", 5, 3, 50);
        if (boardSize >= 3 && boardSize <= 50) {
            this.boardSize = boardSize;
            sizeText.setText("Board size: " + boardSize + " x " + boardSize);
        }

        boardDraw.setBoardSize(boardSize);
        boardDraw.clearBoard();
        boardDraw.drawLines();
    }

    public void bntStartPressed(ActionEvent event){

        if(controlOption.isSelected())
            nextMoveButton.setDisable(false);

        boardDraw.clearBoard();
        boardDraw.drawLines();

        Tournament tournament;
        if (duelBarController.isVisible()) {
            tournament = new Tournament(duelBarController.getDirectories(), boardSize, boardDraw);
        } else {
            tournament = new Tournament(tournamentBarController.getDirectories(), boardSize, boardDraw);
        }
        tournament.makeTournament();
        tournamentText.setText(tournament.buildScoreTable());

        LogWriter log = new LogWriter("tournamentLog");
        log.writeTournamentList(tournament.getScoreList());
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
    }

    public void duelChoicePressed(){
        switchBars(tournamentBarController, btnChoiceTournament, duelBarController, btnChoiceDuel);
    }

    @FXML
    public void initialize(){

        boardDraw = new BoardDraw(canvas, boardSize);
        boardDraw.clearBoard();
        boardDraw.drawLines();

    }



}
