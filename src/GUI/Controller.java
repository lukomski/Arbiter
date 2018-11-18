package GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import logic.Duel;
import logic.Tournament;
import tools.InfoHolder;
import tools.InfoReader;
import tools.LogWriter;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

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


    CustomDialog dialog = new CustomDialog();
    File player1directory;
    File player2directory;
    File tournamentDirectory;
    BoardDraw boardDraw = new BoardDraw();
    String winner;

    public void changeSizeButtonPressed(){

        int boardSize = dialog.initNumberDialog("Board size", "Set board size", 5, 3, 50);
        InfoHolder.BOARD_SIZE = boardSize;
        sizeText.setText("Board size: "+boardSize+"x"+boardSize);


    }
    public void selectPlayersButtonPressed(){

        player1directory = dialog.initDirectoryChooser("Choose Player 1 directory",mainPane);
        player1Text.setText("Player 1: "+InfoReader.read(player1directory)[1]);

        player2directory = dialog.initDirectoryChooser("Choose Player 2 directory",mainPane);
        player2Text.setText("Player 2: "+InfoReader.read(player2directory)[1]);
    }
    public void pressButton(ActionEvent event){


        Duel duel = new Duel(player1directory,player2directory);
        winner = duel.startDuel();
        winnerText.setText("Winner: " + winner);

    }
    public void selectTournamentDirPressed(){
        tournamentDirectory = dialog.initDirectoryChooser("Choose Tournament directory",mainPane);

    }
    public void startTournamentPressed(){
        ArrayList<File> list = new ArrayList<>();

        for(int i=0;i<tournamentDirectory.listFiles().length;i++){
            list.add(tournamentDirectory.listFiles()[i]);

        }


        Tournament tournament = new Tournament(list);
        HashMap<String,Integer> map = tournament.startTournament();
        Map<String, Integer> sortedMap = sortHashMap(map);

        LogWriter log = new LogWriter("tournamentLog");
        log.writeTournamentList(sortedMap);

        tournamentText.setText(buildScoreTable(sortedMap));



    }
    private Map sortHashMap(HashMap<String,Integer> map){
        Map<String, Integer> sortedMap = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return sortedMap;
    }
    private String buildScoreTable(Map<String, Integer> map){
        StringBuilder score = new StringBuilder("Tournament score list\n\n");
        for (String currentKey : map.keySet()) {
            score.append(currentKey + ": " + map.get(currentKey)+"\n");

        }
        return score.toString();

    }

}
