package Controll;

import Model.Board;
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


    Player winner;
    int boardSize = 5;

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

    private Board board;

    @FXML
    public void initialize(){
        board = new Board(Integer.parseInt(btnChangeBoardSize.getText()), canvas);
        board.draw();
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

    public void bntStartPressed(ActionEvent event){

        if(controlOption.isSelected())
            nextMoveButton.setDisable(false);

        Tournament tournament;
        if (duelBarController.isVisible()) {
            tournament = new Tournament(duelBarController.getDirectories(), board);
        } else {
            tournament = new Tournament(tournamentBarController.getDirectories(), board);
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




}
