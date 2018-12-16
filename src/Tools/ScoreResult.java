package Tools;

public class ScoreResult implements Comparable<ScoreResult>{
    private int wins=0;
    private int loses=0;
    private int disqualifications=0;
    public int compareTo(ScoreResult scoreResult){
        return this.wins-scoreResult.wins;
    }

    public void addWin(){
        wins++;
    }
    public void addLoss(){
        loses++;
    }
    public void addDisqualification(){
        disqualifications++;
    }
    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getDisqualifications() {
        return disqualifications;
    }
}
