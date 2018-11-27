package Controll;


import Tools.DialogReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import Model.Tournament;
import Model.Player;
import Tools.LogWriter;

import java.io.File;
import java.util.*;

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

    @FXML
    private DuelBarController duelBarController;
    @FXML
    private TournamentBarController tournamentBarController;

       public void changeSizeButtonPressed() {
           int boardSize = DialogReader.readNumberFromDialog("Board size", "Set board size", 5, 3, 50);
           if (boardSize >= 3 && boardSize <= 50) {
               this.boardSize = boardSize;
               sizeText.setText("Board size: " + boardSize + " x " + boardSize);
           }
       }
    public void bntStartPressed(ActionEvent event){
        Tournament tournament;
        if (duelBarController.isVisible()) {
            tournament = new Tournament(duelBarController.getDirectories(), boardSize);
        } else {
            tournament = new Tournament(tournamentBarController.getDirectories(), boardSize);
        }
        tournament.makeTournament();
        LogWriter log = new LogWriter("tournamentLog");
        log.writeTournamentList(tournament.getScoreList());
        tournamentText.setText(buildScoreTable(tournament.getScoreList()));
    }

    public void tournamentChoicePressed(){
        duelBarController.setVisible(false);
        tournamentBarController.setVisible(true);
        /* button shadow */
        btnChoiceDuel.getStyleClass().removeAll("doubleShadowed");
        btnChoiceDuel.getStyleClass().addAll("doubleShadowed");
        btnChoiceTournament.getStyleClass().removeAll("doubleShadowed");
    }

    public void duelChoicePressed(){
        tournamentBarController.setVisible(false);
        duelBarController.setVisible(true);

        /* button shadow */
        btnChoiceTournament.getStyleClass().removeAll("doubleShadowed");
        btnChoiceTournament.getStyleClass().addAll("doubleShadowed");
        btnChoiceDuel.getStyleClass().removeAll("doubleShadowed");
    }

    private String buildScoreTable(Map<String, Integer> map){
        StringBuilder score = new StringBuilder("Tournament score list\n\n");
        for (String currentKey : map.keySet()) {
            score.append(currentKey + ": " + map.get(currentKey)+"\n");
        }
        return score.toString();
    }

}
