package Tools;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;

public class ScoreResult implements Comparable<ScoreResult>{
    private SimpleStringProperty name;
    private SimpleStringProperty nick;
    private SimpleStringProperty id;
    private SimpleIntegerProperty wins;
    private SimpleIntegerProperty loses;
    private SimpleIntegerProperty disqualifications;
    public ScoreResult(String id, String name, String nick){
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.nick = new SimpleStringProperty(nick);
        this.wins = new SimpleIntegerProperty(0);
        this.loses = new SimpleIntegerProperty(0);
        this.disqualifications = new SimpleIntegerProperty(0);

    }

    public int compareTo(ScoreResult scoreResult){
        return this.wins.get()-scoreResult.wins.get();
    }

    public void addWin(){
        wins.set(wins.get()+1);
    }
    public void addLoss(){
        loses.set(loses.get()+1);
    }
    public void addDisqualification(){
        disqualifications.set(disqualifications.get()+1);
    }
    public Integer getWins() {
        return wins.get();
    }

    public Integer getLoses() {
        return loses.get();
    }

    public Integer getDisqualifications() {
        return disqualifications.get();
    }
    public String getName(){
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setWins(Integer wins) {
        this.wins.set(wins);
    }

    public void setLoses(Integer loses) {
        this.loses.set(loses);
    }


    public void setDisqualifications(Integer disqualifications) {
        this.disqualifications.set(disqualifications);
    }


    public String getNick() {
        return nick.get();
    }


    public void setNick(String nick) {
        this.nick.set(nick);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

}

