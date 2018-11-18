package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LogWriter {
    private File file;
    private PrintWriter printWriter=null;
    public LogWriter(String logName){
        file = new File(logName+".txt");
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void writeTitle(int size, String name1, String name2){
        printWriter.println("Board size: "+size+" Player1: "+name1+" Player2: "+name2);
        printWriter.flush();
    }
    public void write(String nick, String move){
        printWriter.println(nick+": "+move);
        printWriter.flush();

    }
    public void writeWinner(String name){
        printWriter.println("Winner: "+name);
        printWriter.flush();
    }
    public void writeTournamentList(Map<String,Integer> map) {


        for (String currentKey : map.keySet()) {
            printWriter.println(currentKey + ": " + map.get(currentKey));
            printWriter.flush();
        }
    }
}
