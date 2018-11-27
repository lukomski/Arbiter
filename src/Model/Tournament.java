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

    public List<Object> makeTournament(){
        List<Object> error = fillScoreList();

        for(int i = 0; i < playersDirList.size();i++){
            Player player1;
            try {
                player1 = new Player(playersDirList.get(i));
                error.add(playersDirList.get(i));
            } catch (Exception e){
                continue;
            }
            for(int j = i + 1;j < playersDirList.size();j++){
                Player player2;
                try {
                    player2 = new Player(playersDirList.get(j));
                    error.add(playersDirList.get(j));
                } catch (Exception e){
                    continue;
                }
                Duel duel = new Duel(player1, player2, boardSize);
                Player winner = duel.startDuel();

                scoreList.put(winner.getDirName(),scoreList.get(winner.getDirName())+1);
            }
        }
        sortScoreList();
        return error;
    }

    private void sortScoreList(){
        scoreList = scoreList.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public HashMap<String,Integer> getScoreList(){
        return scoreList;
    }

    private List<Object> fillScoreList(){
        List<Object> error = new ArrayList<>();
        for(int i=0;i<playersDirList.size();i++){
            try {
                scoreList.put(playersDirList.get(i).getName(), 0);
            } catch (Exception e){
                error.add(playersDirList.get(i));
            }
        }
        return error;
    }

}
