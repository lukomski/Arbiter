package Model;

import Controll.MainController;
import javafx.scene.canvas.Canvas;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Tournament extends Thread {
    private HashMap<String,Integer> scoreList;
    private List<File> playersDirList;
    private List<File> disqualifieds;
    private List<Duel> duelQueue;
    private int boardSize;
    private Board board;
    private MainController mainController;
    private Player winner;
    private Duel duel;


    public Tournament(MainController mainController){
        this.playersDirList = mainController.getDirectories();
        this.boardSize = boardSize;
        this.board = mainController.getBoard();
        this.mainController = mainController;
        scoreList = new HashMap<>();
        disqualifieds = new ArrayList<>();
        duelQueue = new LinkedList<>();
    }
    @Override
    public void run(){
        makeDuelqueue();
        makeDuel();
    }
    private void makeDuelqueue(){
        List<Integer> idDisqualifieds = fillScoreList();

        for(int i = 0; i < playersDirList.size();i++) {
            if (idDisqualifieds.contains(i)) {
                continue;
            }
            Player player1;
            try {
                player1 = new Player(playersDirList.get(i));

            } catch (Exception e) {
                disqualifieds.add(playersDirList.get(i));
                System.out.println("pomijam gracza: " + playersDirList.get(i));
                continue;
            }
            for (int j = i + 1; j < playersDirList.size(); j++) {
                if (idDisqualifieds.contains(j)) {
                    continue;
                }
                Player player2;
                try {
                    player2 = new Player(playersDirList.get(j));
                } catch (Exception e) {
                    disqualifieds.add(playersDirList.get(j));
                    System.out.println("pomijam gracza: " + playersDirList.get(i));
                    /* win by default */
                    scoreList.put(player1.getDirName(), scoreList.get(player1.getDirName()) + 1);
                    continue;
                }
                duelQueue.add(new Duel(player1, player2, board, this));
            }
        }

    }

    public void makeDuel() {
        duel = duelQueue.get(0);
        duelQueue.remove(0);
        duel.start();
    }

    private void sortScoreList(){
        scoreList = scoreList.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public HashMap<String,Integer> getScoreList(){
        return scoreList;
    }
    public String buildScoreTable(){
        HashMap<String, Integer> map = scoreList;
        StringBuilder score = new StringBuilder("Tournament score list\n\n");
        for (String currentKey : map.keySet()) {
            score.append(currentKey + ": " + map.get(currentKey)+"\n");
        }
        return score.toString();
    }

    private List<Integer> fillScoreList(){
        disqualifieds = new ArrayList<>();
        List<Integer> idDisqualifieds = new ArrayList<>();
        for(int i=0;i<playersDirList.size();i++){
            try {
                scoreList.put(playersDirList.get(i).getName(), 0);
            } catch (Exception e){
                disqualifieds.add(playersDirList.get(i));
                idDisqualifieds.add(i);
            }
        }
        return idDisqualifieds;
    }
    public void doNextMove(){
        duel.doNextMove();
    }
    public void moveEnded(){
        if(!mainController.isControlled()) {
            duel.doNextMove();
        } else {
            board.draw();
        }
    }
    public void duelEnded(){
        winner = duel.getWinner();
        scoreList.put(winner.getDirName(), scoreList.get(winner.getDirName()) + 1);
        board.draw();
        board.clean();
        if(duelQueue.size() == 0) {

            sortScoreList();
            //TODO make log auto win
            //logWriter.writeWinner(winner.getNick());
            mainController.tournamentEnded();
        } else{
            makeDuel();
        }
    }

}
