package Model;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Tournament {
    private HashMap<String,Integer> scoreList;
    private List<File> playersDirList;
    private int boardSize;

    public Tournament(List playersDirList, int boardSize){
        this.playersDirList = playersDirList;
        this.boardSize = boardSize;
        scoreList = new HashMap<>();
    }

    public void makeTournament(){
        fillScoreList();

        for(int i = 0; i < playersDirList.size();i++){
            for(int j = i + 1;j < playersDirList.size();j++){
                    Duel duel = new Duel(playersDirList.get(i),playersDirList.get(j), boardSize);
                    Player winner = duel.startDuel();

                    scoreList.put(winner.getDirName(),scoreList.get(winner.getDirName())+1);
            }
        }
        sortScoreList();
    }

    private void sortScoreList(){
        scoreList = scoreList.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public HashMap<String,Integer> getScoreList(){
        return scoreList;
    }

    private void fillScoreList(){
        for(int i=0;i<playersDirList.size();i++){
            scoreList.put(playersDirList.get(i).getName(),0);
        }
    }

}
