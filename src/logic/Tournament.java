package logic;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Tournament {
    private HashMap<String,Integer> scoreList;
    private List <File>playersDirList;
    public Tournament(List playersDirList){
        this.playersDirList = playersDirList;
        scoreList = new HashMap<>();


    }
    public HashMap startTournament(){

        fillScoreList();

        for(int i=0;i<playersDirList.size();i++){
            for(int j=i+1;j<playersDirList.size();j++){
                    Duel duel = new Duel(playersDirList.get(i),playersDirList.get(j));

                    String winner = duel.startTournamentDuel();

                    scoreList.put(winner,scoreList.get(winner)+1);

            }
        }
        return scoreList;


    }
    private void fillScoreList(){
        for(int i=0;i<playersDirList.size();i++){
            scoreList.put(playersDirList.get(i).getName(),0);
        }
    }

}
