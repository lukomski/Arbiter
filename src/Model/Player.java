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
    private boolean humanPlayer=false;

    public Player(BasicInfo basicInfo)throws Exception{
        this.basicInfo = basicInfo;

        processBuilder = new ProcessBuilder();
        processBuilder.command(basicInfo.getCommand().split(" ")  );
        processBuilder.directory(basicInfo.getDirectory());
    }
    public Player(BasicInfo basicInfo, boolean humanPlayer){
        this.basicInfo = basicInfo;
        this.humanPlayer = humanPlayer;
    }

    public void initProcess(){
        if(!humanPlayer) {
            try {
                process = processBuilder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new PrintWriter(process.getOutputStream(), true);
        }
    }

    public void sendMessage(String line){
        if(!humanPlayer){
            output.println( line );
        }

    }

    public String getMessage(){
        if(!humanPlayer) {
            String inn = "error";
            try {
                inn = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inn;
        }
        return "";
    }
    public String getNick(){
        return basicInfo.getNick();
    }
    public String getDirName(){
        return  basicInfo.getDirectory().getName();
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public boolean isHumanPlayer() {
        return humanPlayer;
    }
}
