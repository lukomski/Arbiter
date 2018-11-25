package Controll;


import Tools.DialogReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import Model.Duel;
import Model.Tournament;
import Model.Player;
import Tools.LogWriter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Controller {
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
    private AnchorPane duelAnchorPane = null;
    @FXML
    private AnchorPane tournamentAnchorPane = null;
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
    private TournamentBarController  tournamentBarController;

    public void changeSizeButtonPressed(){
        boardSize = DialogReader.readNumberFromDialog("Board size", "Set board size", 5, 3, 50);
        sizeText.setText("Board size: "+boardSize+"x"+boardSize);
    }

    public void bntStartPressed(ActionEvent event){
        if (duelBarController.isVisible()) {
            System.out.println("player1:" + duelBarController.getPlayer1directory() + "player2:" + duelBarController.getPlayer2directory());
            Duel duel = new Duel(duelBarController.getPlayer1directory(), duelBarController.getPlayer2directory(), boardSize);
            winner = duel.startDuel();
            winnerText.setText("Winner: " + winner.getNick());
        } else {
            ArrayList<File> list = new ArrayList<>();

            File tournamentDirectory = tournamentBarController.getTournamentDirectory();
            for(int i = 0;i < tournamentDirectory.listFiles().length;i++){
                list.add(tournamentDirectory.listFiles()[i]);
            }

            Tournament tournament = new Tournament(list, boardSize);
            tournament.makeTournament();

            LogWriter log = new LogWriter("tournamentLog");
            log.writeTournamentList(tournament.getScoreList());

            tournamentText.setText(buildScoreTable(tournament.getScoreList()));
        }
    }
    public void tournamentChoicePressed(){
        System.out.println(btnChoiceDuel.getStyleClass());
        System.out.println(btnChoiceTournament.getStyleClass());
        duelBarController.setVisible(false);
        tournamentBarController.setVisible(true);
        /* button shadow */
        btnChoiceDuel.getStyleClass().clear();
        btnChoiceDuel.getStyleClass().addAll("button", "menuTypeGame", "doubleShadowed");
        btnChoiceTournament.getStyleClass().clear();
        btnChoiceTournament.getStyleClass().addAll("button", "menuTypeGame");
    }

    public void duelChoicePressed(){
        tournamentBarController.setVisible(false);
        duelBarController.setVisible(true);

        /* button shadow */
        btnChoiceTournament.getStyleClass().clear();
        btnChoiceTournament.getStyleClass().addAll("button","menuTypeGame","doubleShadowed");
        btnChoiceDuel.getStyleClass().clear();
        btnChoiceDuel.getStyleClass().addAll("button", "menuTypeGame");
    }

    private String buildScoreTable(Map<String, Integer> map){
        StringBuilder score = new StringBuilder("Tournament score list\n\n");
        for (String currentKey : map.keySet()) {
            score.append(currentKey + ": " + map.get(currentKey)+"\n");
        }
        return score.toString();
    }

}
