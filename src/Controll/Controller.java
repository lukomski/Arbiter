package Controll;


import Tools.DialogReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import Model.Duel;
import Model.Tournament;
import Model.Player;
import Tools.InfoReader;
import Tools.LogWriter;

import java.io.File;
import java.util.*;

public class Controller {
    @FXML
    private Button changeSizeButton;
    @FXML
    private Label sizeText;
    @FXML
    private Pane mainPane;
    @FXML
    private Label player1Text;
    @FXML
    private Label player2Text;
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane boardPane;
    @FXML
    private Label winnerText;
    @FXML
    private Label tournamentText;


    File player1directory;
    File player2directory;
    File tournamentDirectory;
    Player winner;
    int boardSize = 5;

    public void changeSizeButtonPressed(){
        boardSize = DialogReader.readNumberFromDialog("Board size", "Set board size", 5, 3, 50);
        sizeText.setText("Board size: "+boardSize+"x"+boardSize);
    }

    public void selectPlayersButtonPressed(){
        player1directory = DialogReader.readDirectoryFromDialog("Choose Player 1 directory",mainPane);
        player1Text.setText("Player 1: "+InfoReader.read(player1directory)[1]);

        player2directory = DialogReader.readDirectoryFromDialog("Choose Player 2 directory",mainPane);
        player2Text.setText("Player 2: "+InfoReader.read(player2directory)[1]);
    }

    public void pressButton(ActionEvent event){
        Duel duel = new Duel(player1directory,player2directory, boardSize);
        winner = duel.startDuel();
        winnerText.setText("Winner: " + winner.getNick());
    }

    public void selectTournamentDirPressed(){
        tournamentDirectory = DialogReader.readDirectoryFromDialog("Choose Tournament directory",mainPane);
    }

    public void startTournamentPressed(){
        ArrayList<File> list = new ArrayList<>();

        for(int i = 0;i < tournamentDirectory.listFiles().length;i++){
            list.add(tournamentDirectory.listFiles()[i]);
        }

        Tournament tournament = new Tournament(list, boardSize);
        tournament.makeTournament();

        LogWriter log = new LogWriter("tournamentLog");
        log.writeTournamentList(tournament.getScoreList());

        tournamentText.setText(buildScoreTable(tournament.getScoreList()));
    }

    private String buildScoreTable(Map<String, Integer> map){
        StringBuilder score = new StringBuilder("Tournament score list\n\n");
        for (String currentKey : map.keySet()) {
            score.append(currentKey + ": " + map.get(currentKey)+"\n");
        }
        return score.toString();
    }

}
