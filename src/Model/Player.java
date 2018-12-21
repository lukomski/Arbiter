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

    public String getMessage() throws IOException, TimeoutException, InterruptedException {
        int waitLimit = 500;

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
    public int[] strMes2intMesField(String s){
        String[] splitted = s.split("x");
        int[] v = new int[2];
        v[0] = Integer.parseInt(splitted[0]);
        v[1] = Integer.parseInt(splitted[1]);
        return v;
    }
    public static int[][] decrypteMove(String message) throws NullPointerException, IndexOutOfBoundsException{
        System.out.println(message);
        int[][] move = new int[2][2];
        String cord[] = message.split("_");
        String firstFiel[] = cord[0].split("x");
        String secondField[] = cord[1].split("x");
        move[0][0] = Integer.parseInt(firstFiel[0]);
        move[0][1] = Integer.parseInt(firstFiel[1]);
        move[1][0] = Integer.parseInt(secondField[0]);
        move[1][1] = Integer.parseInt(secondField[1]);
        return move;
    }
    public static String int2strMove(int[][] move){
        return move[0][0] + "x" + move[0][1] + "_" + move[1][0] + "x" + move[1][1];
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
