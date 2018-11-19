package Model;

import tools.InfoReader;

import java.io.*;

public class Player {
    private String nick;
    private String dirName;

    private ProcessBuilder processBuilder;
    private BufferedReader input;
    private PrintWriter output;
    private Process process;

    public Player(File dir){
        String info[] = InfoReader.read(dir); /* change name */
        nick = info[1];
        dirName = dir.getName();

        processBuilder = new ProcessBuilder();
        processBuilder.command( info[0].split(" ")  );
        processBuilder.directory(dir);
    }

    public void initProcess(){
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        input = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
        output = new PrintWriter( process.getOutputStream() , true );
    }

    public void sendMessage(String line){
        output.println( line );
    }

    public String getMessage(){
        String inn="error";
        try {
             inn = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inn;
    }
    public String getNick(){
        return nick;
    }
    public String getDirName(){
        return  dirName;
    }

}
