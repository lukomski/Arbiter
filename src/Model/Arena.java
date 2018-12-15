package Model;

import Controll.MainController;
import Tools.BasicInfo;
import Tools.LogWriter;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Arena extends Thread {
    private HashMap<String,Integer> scoreList;
    private List<File> playersDirList;
    private List<File> disqualifieds;
    private List<Duel> duelQueue;
    private Board board;
    private MainController mainController;
    private Player winner;
    private List<Player> players;
    private Duel duel;
    private LogWriter logWriter;


    public Arena(MainController mainController){
        this.playersDirList = mainController.getDirectories();
        this.players = new ArrayList<>();
        this.board = mainController.getBoard();
        this.mainController = mainController;
        scoreList = new HashMap<>();
        disqualifieds = new ArrayList<>();
        duelQueue = new LinkedList<>();
        logWriter = new LogWriter("tournamentDuelResults");
        logWriter.writeTournamentDuelResultTitle();
    }
    @Override
    public void run(){
        fillPlayerList();
        fillScoreList();
        makeDuelqueue();
        makeDuel();
    }
    private void makeDuelqueue(){
        int playerIndex = 0;
        for(Player firstPlayer: players) {
            for(int i=++playerIndex;i<players.size();i++){

                Player secondPlayer = players.get(i);
                Player[] players = {firstPlayer,  secondPlayer};
                duelQueue.add(new Duel(players, this));
            }
        }
    }

    public void makeDuel() {
        if(duelQueue.size() == 0){
            mainController.forceEnd("No duels to play");
            logWriter.writeMessage("Warning: No duels to play");
        } else {
            duel = duelQueue.get(0);
            duelQueue.remove(0);
            board.clean();
            duel.start();
        }
    }

    private void sortScoreList(){
        scoreList = scoreList.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }


    public String buildScoreTable(){
        HashMap<String, Integer> map = scoreList;
        StringBuilder score = new StringBuilder("Scores:\n\n");
        for (String currentKey : map.keySet()) {
            score.append(currentKey + " : " + map.get(currentKey)+"\n");
        }
        return score.toString();
    }

    private void fillPlayerList(){

        for(File directory: playersDirList){

            try {
                BasicInfo basicInfo = new BasicInfo(directory);
                Player player = new Player(basicInfo);
                players.add(player);

            } catch (Exception e){
                logWriter.writeMessage("Warning: Unable to read basic info about program from directory " + directory + " - omitted");
            }
        }
    }
    private void fillScoreList(){
        for(Player player: players){
            scoreList.put(player.getNick(), 0);

        }
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
        scoreList.put(winner.getNick(), scoreList.get(winner.getNick()) + 1);
        board.draw();

       logWriter.writeDuelResult(duel.getPlayer1Name(),duel.getPlayer2Name(),winner.getNick(),duel.getWinReason());

        if(duelQueue.size() == 0) {

            sortScoreList();
            //TODO make log auto win
            //logWriter.writeWinner(winner.getNick());
            mainController.tournamentEnded();
        } else{
            makeDuel();
        }

    }
    public Board getBoard(){
        return board;
    }
    public HashMap<String,Integer> getScoreList(){
        return scoreList;
    }

}
