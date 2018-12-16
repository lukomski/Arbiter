package Model;

import Controll.MainController;
import Tools.BasicInfo;
import Tools.LogWriter;
import Tools.ScoreResult;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Arena extends Thread {
    private HashMap<String, ScoreResult> scoreList;
    private List<File> playersDirList;
    private List<File> disqualifieds;
    private List<Duel> duelQueue;
    private Board board;
    private MainController mainController;
    private Player winner;
    private List<Player> players;
    private Duel duel;
    private LogWriter logWriter;
    private LogWriter errorLogWriter;
    private boolean forceStop = false;
    private boolean humanPlayer;


    public Arena(MainController mainController, boolean humanPlayer){
        this.playersDirList = mainController.getDirectories();
        this.players = new ArrayList<>();
        this.board = mainController.getBoard();
        this.mainController = mainController;
        scoreList = new HashMap<>();
        disqualifieds = new ArrayList<>();
        duelQueue = new LinkedList<>();
        logWriter = new LogWriter("tournamentDuelResults");
        logWriter.writeTournamentDuelResultTitle();
       errorLogWriter = new LogWriter("errors");
        this.humanPlayer = humanPlayer;
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
            errorLogWriter.writeMessage("Warning: No duels to play");
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
        HashMap<String, ScoreResult> map = scoreList;
        StringBuilder score = new StringBuilder("Scores:\n\nName : Wins : Loses : Disqualifications\n\n");
        for (String currentKey : map.keySet()) {
            score.append(currentKey + " : " + map.get(currentKey).getWins()+
                    " : "+map.get(currentKey).getLoses()+" : "+map.get(currentKey).getDisqualifications()+ "\n");
        }
        return score.toString();
    }

    private void fillPlayerList(){
        int k=0;

        for(File directory: playersDirList){

            try {
                Player player;
                if(humanPlayer && k==1){
                    BasicInfo basicInfo = new BasicInfo();
                    player = new Player(basicInfo,humanPlayer);
                    humanPlayer=false;
                }else {
                    BasicInfo basicInfo = new BasicInfo(directory);
                    player = new Player(basicInfo);
                }
                players.add(player);

            } catch (Exception e){
                errorLogWriter.writeMessage("Warning: Unable to read basic info about program from directory " + directory + " - omitted");
            }
            k++;
        }
    }
    private void fillScoreList(){
        for(Player player: players){
            scoreList.put(player.getFullName(), new ScoreResult());

        }
    }
    public void setForceStop(boolean forceStop){
        this.forceStop = forceStop;
        if(mainController.duelBarController.isVisible()){
            mainController.forceEnd("Terminated");
            return;
        }
    }
    public void doNextMove(String humanMove){
        duel.doNextMove(humanMove);
    }
    public void moveEnded(){
        if(forceStop){
            mainController.forceEnd("Terminated");
            return;
        }
        if(!mainController.isControlled()) {
            duel.doNextMove("");
        } else {
            board.draw();
        }
    }
    public void duelEnded(){
        winner = duel.getWinner();
       /* scoreList.put(winner.getFullName(),*/ scoreList.get(winner.getFullName()).addWin();
       if(winner.equals(duel.getPlayer1())){
           scoreList.get(duel.getPlayer2().getFullName()).addLoss();
           if(duel.getWinReason()!="NORMAL WIN")
               scoreList.get(duel.getPlayer2().getFullName()).addDisqualification();
       }else{
           scoreList.get(duel.getPlayer1().getFullName()).addLoss();
           if(duel.getWinReason()!="NORMAL WIN")
               scoreList.get(duel.getPlayer1().getFullName()).addDisqualification();
       }

        board.draw();

       logWriter.writeDuelResult(duel.getPlayer1FullName(),duel.getPlayer2FullName(),winner.getNick(),duel.getWinReason());

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
    public HashMap<String,ScoreResult> getScoreList(){
        return scoreList;
    }

}
