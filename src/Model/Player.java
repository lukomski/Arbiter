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
    private String answer;

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

    public String getAnswer(){
        return answer;
    }
    public boolean isAnswer(){
        try {
            answer = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(answer == null){
            return false;
        } else {
            return true;
        }
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
