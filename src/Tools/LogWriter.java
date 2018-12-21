package Tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
    public void writeTournamentList(Map<String,ScoreResult> map) {

        printWriter.println("Tournament:");
        printWriter.println("Name : Wins : Loses : Disqualifications");
        for (String currentKey : map.keySet()) {
            printWriter.println(currentKey + " : " + map.get(currentKey).getWins()+
                    " : "+map.get(currentKey).getLoses()+" : "+map.get(currentKey).getDisqualifications());
            printWriter.flush();
        }
    }
    public void writeMessage(String msg){
        printWriter.println(msg);
        printWriter.flush();
    }
    public List<String> loadMoves() throws IOException

    {
        List<String> moves = new ArrayList<>();
        FileReader fileReader = new FileReader(file);
        while(fileReader.ready()){

        }
        return moves;
    }
    public List<String> loadLogFile() throws IOException{
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> moves = new ArrayList<>();
        while(bufferedReader.ready()){
            String line = bufferedReader.readLine();
            if(line.length() >= 1 && line.charAt(0) == '$') {
                String[] parts = line.split(":");
                moves.add(parts[1]);
            } else {
                System.out.println("LogWriter: Ignore: ");
            }
        }
        return moves;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}
