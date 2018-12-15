package Model;

import Tools.BasicInfo;

import java.io.*;

public class Player {
    private BasicInfo basicInfo;

    private ProcessBuilder processBuilder;
    private BufferedReader input;
    private PrintWriter output;
    private Process process;
    private int id;

    public Player(BasicInfo basicInfo)throws Exception{
        this.basicInfo = basicInfo;

        processBuilder = new ProcessBuilder();
        processBuilder.command(basicInfo.getCommand().split(" ")  );
        processBuilder.directory(basicInfo.getDirectory());
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
        return basicInfo.getNick();
    }
    public String getDirName(){
        return  basicInfo.getDirectory().toString();
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

}
