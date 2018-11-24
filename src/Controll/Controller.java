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

    AnchorPane localPane;
    File player1directory;
    File player2directory;
    File tournamentDirectory;

    Player winner;
    int boardSize = 5;
    boolean isDuel = true;

    @FXML
    private DuelBarController duelBarController;
    @FXML
    private TournamentBarController  tournamentBarController;

    void setPlayer1directory(File file){
        player1directory = file;
    }
    void setPlayer2directory(File file){
        player2directory = file;
    }
    void setTournamentDirectory(File file){
        tournamentDirectory = file;
    }
    AnchorPane getMainPane(){
        return mainPane;
    }
    public void changeSizeButtonPressed(){
        boardSize = DialogReader.readNumberFromDialog("Board size", "Set board size", 5, 3, 50);
        sizeText.setText("Board size: "+boardSize+"x"+boardSize);
    }



    public void bntStartPressed(ActionEvent event){
        if (duelAnchorPane != null && duelAnchorPane.isVisible()) {
            Duel duel = new Duel(player1directory, player2directory, boardSize);
            winner = duel.startDuel();
            winnerText.setText("Winner: " + winner.getNick());
        } else {
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
    }
    public void tournamentChoicePressed() throws IOException {
        if(tournamentAnchorPane == null) {
            tournamentBarController = new TournamentBarController(this);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../GUI/tournamentMenu.fxml"));
            fxmlLoader.setController(tournamentBarController);
            tournamentAnchorPane = fxmlLoader.load();
            tournamentAnchorPane.setLayoutY(50);
            mainPane.getChildren().add(tournamentAnchorPane);
        }else{
            if(duelAnchorPane != null)
                duelAnchorPane.setVisible(false);
            tournamentAnchorPane.setVisible(true);
        }
    }

    public void duelChoicePressed() throws IOException{
        if(duelAnchorPane == null) {
            duelBarController = new DuelBarController(this);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../GUI/duelMenu.fxml"));
            fxmlLoader.setController(duelBarController);
            duelAnchorPane = fxmlLoader.load();
            duelAnchorPane.setLayoutY(50);
            mainPane.getChildren().add(duelAnchorPane);
        } else {
            if(tournamentAnchorPane != null)
                tournamentAnchorPane.setVisible(false);
            duelAnchorPane.setVisible(true);
        }

    }
    private String buildScoreTable(Map<String, Integer> map){
        StringBuilder score = new StringBuilder("Tournament score list\n\n");
        for (String currentKey : map.keySet()) {
            score.append(currentKey + ": " + map.get(currentKey)+"\n");
        }
        return score.toString();
    }

}
