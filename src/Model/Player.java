package Model;

import Tools.BasicInfo;

import java.io.*;
import java.util.concurrent.TimeoutException;

public class Player {
    private BasicInfo basicInfo;

    private ProcessBuilder processBuilder;
    private BufferedReader input;
    private PrintWriter output;
    private Process process;
    private int id;
    private boolean humanPlayer=false;

    public Player(BasicInfo basicInfo){
        this.basicInfo = basicInfo;

        processBuilder = new ProcessBuilder();
        processBuilder.command(basicInfo.getCommand().split(" ")  );
        processBuilder.directory(basicInfo.getDirectory());

    }
    public Player(BasicInfo basicInfo, boolean humanPlayer){
        this.basicInfo = basicInfo;
        this.humanPlayer = humanPlayer;
    }

    public void initProcess() throws  IOException{
        if(!humanPlayer) {

            process = processBuilder.start();

            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new PrintWriter(process.getOutputStream(), true);
        }
    }

    public void sendMessage(String line){
        output.println( line );
    }

    public String getMessage() throws IOException, TimeoutException, InterruptedException{
        int waitLimit = 1000;//500

        int wait = 0;

        while (!input.ready() && wait < waitLimit) {
            int millis = 1;
            Thread.sleep(millis);

            wait += millis;
        }
        if (wait >= waitLimit) {
            throw new TimeoutException();
        }
        String inn = input.readLine();
        return inn;
        //return inn;
    }

    public String getNick(){
        return basicInfo.getNick();
    }
    public String getDirName(){
        return  basicInfo.getDirectory().getName();
    }
    public String getFullName(){
        return basicInfo.getFullName();
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public String getDirectory(){
        return basicInfo.getDirectory().toString();
    }
    public String getName(){
        return basicInfo.getName();
    }

    public boolean isHumanPlayer() {
        return humanPlayer;
    }
    public void destroy(){
        process.destroy();
    }
}
