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
    private List<File> directories;
    private List<File> disqualifieds;
    private List<Duel> duels;
    private Board board;
    private MainController mainController;
    private Player winner;
    private List<Player> players;
    private Duel duel;
    private LogWriter logWriter;
    private LogWriter errorLogWriter;
    private boolean humanPlayer;
    private File directory;


    public Arena(MainController mainController){
        this.mainController = mainController;
        this.board = mainController.getBoard();
        /* dirs */
        directories = new ArrayList<>();
        this.directory = mainController.getDirectory();
        loadDirectories();


        this.players = new ArrayList<>();
        scoreList = new HashMap<>();
        disqualifieds = new ArrayList<>();
        duels = new ArrayList<>();


        logWriter = new LogWriter("tournamentDuelResults");
        logWriter.writeTournamentDuelResultTitle();
       errorLogWriter = new LogWriter("errors");
    }
    @Override
    public void run(){
        fillPlayerList();
        System.out.println("Arena: Players read");
        fillScoreList();
        System.out.println("Arena:: score list filled");
        makeDuelList();
        System.out.println("Arena: duel list made");
        for(int i = 0; i < duels.size(); i++){
            makeDuel(i);
        }
        mainController.arenaEnded();
        System.out.println("Arena: EXIT");
    }
    private void makeDuelList(){
        int playerIndex = 0;
        for(Player firstPlayer: players) {
            for(int i=++playerIndex;i<players.size();i++){

                Player secondPlayer = players.get(i);
                Player[] players = {firstPlayer,  secondPlayer};
                duels.add(new Duel(players, this));
            }
        }
    }

    public void makeDuel(int i) {
        duel = duels.get(i);
        board.clean();
        duel.run();
        board.draw();
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

        for(File directory: directories){

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




    public void duelEnded(){
        winner = duel.getWinner();
        if(winner!=null) {
            scoreList.get(winner.getFullName()).addWin();
            if (winner.equals(duel.getPlayer1())) {
                scoreList.get(duel.getPlayer2().getFullName()).addLoss();
                if (duel.getWinReason() != "NORMAL WIN")
                    scoreList.get(duel.getPlayer2().getFullName()).addDisqualification();
            } else {
                scoreList.get(duel.getPlayer1().getFullName()).addLoss();
                if (duel.getWinReason() != "NORMAL WIN")
                    scoreList.get(duel.getPlayer1().getFullName()).addDisqualification();
            }

            board.draw();

            logWriter.writeDuelResult(duel.getPlayer1FullName(), duel.getPlayer2FullName(), winner.getNick(), duel.getWinReason());
        }else{
            errorLogWriter.writeMessage("Error: Wrong program launch instuctions in "+duel.getErrorPlayer().getDirectory());

        }

    }
    public Board getBoard(){
        return board;
    }
    public HashMap<String,ScoreResult> getScoreList(){
        return scoreList;
    }
    public void loadDirectories() {
        for (int i = 0; i < directory.listFiles().length; i++) {
            directories.add(directory.listFiles()[i]);
        }
    }

}
