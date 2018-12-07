package Model;

import javafx.scene.canvas.Canvas;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Tournament {
    private HashMap<String,Integer> scoreList;
    private List<File> playersDirList;
    private List<File> disqualifieds;
    private int boardSize;
    private Canvas canvas;


    public Tournament(List playersDirList, int boardSize, Canvas canvas){
        this.playersDirList = playersDirList;
        this.boardSize = boardSize;
        this.canvas = canvas;
        scoreList = new HashMap<>();
        disqualifieds = new ArrayList<>();
    }

    public void makeTournament(){
        List<Integer> idDisqualifieds = fillScoreList();

        for(int i = 0; i < playersDirList.size();i++){
            if(idDisqualifieds.contains(i)){
                continue;
            }
            Player player1;
            try {
                player1 = new Player(playersDirList.get(i));

            } catch (Exception e){
                disqualifieds.add(playersDirList.get(i));
                System.out.println("pomijam gracza: " + playersDirList.get(i));
                continue;
            }
            for(int j = i + 1;j < playersDirList.size();j++){
                if(idDisqualifieds.contains(j)){
                    continue;
                }
                Player player2;
                try {
                    player2 = new Player(playersDirList.get(j));
                } catch (Exception e){
                    disqualifieds.add(playersDirList.get(j));
                    System.out.println("pomijam gracza: " + playersDirList.get(i));
                    /* win by default */
                    scoreList.put(player1.getDirName(),scoreList.get(player1.getDirName())+1);
                    continue;
                }
                Duel duel = new Duel(player1, player2, boardSize, canvas);
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

}
