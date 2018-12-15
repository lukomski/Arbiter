package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

public class LogWriter {
    private File file;
    private PrintWriter printWriter=null;
    public LogWriter(String logName){
        new File("logs").mkdir();
        file = new File("logs\\"+logName+".txt");
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void writeDuelTitle(int size,String startPoints, String name1, String name2){
        printWriter.println("Duel:");
        printWriter.println("Board size: "+size+" Start filled points: "+startPoints+" Player1: "+name1+" Player2: "+name2);
        printWriter.flush();
    }
    public void writeTournamentDuelResultTitle(){
        printWriter.println("Tournament duel results:");
        printWriter.flush();
    }
    public void writeDuelMove(String nick, String move){
        printWriter.println(nick+": "+move);
        printWriter.flush();

    }
    public void writeDuelResult(String name1, String name2, String winner,String reason){
        printWriter.println(name1+" vs "+name2+"  Winner: "+winner+"  "+reason);
        printWriter.flush();

    }
    public void writeWinner(String name){
        printWriter.println("Winner: "+name);
        printWriter.flush();
    }
    public void writeTournamentList(Map<String,Integer> map) {

        printWriter.println("Tournament:");
        for (String currentKey : map.keySet()) {
            printWriter.println(currentKey + ": " + map.get(currentKey));
            printWriter.flush();
        }
    }
}
