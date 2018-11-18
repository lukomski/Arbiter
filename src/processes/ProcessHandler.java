package processes;

import tools.InfoReader;

import java.io.*;

public class ProcessHandler {
    private ProcessBuilder processBuilder;
    private String nick;

    private BufferedReader input;
    private PrintWriter output;
    private Process process;
    private String dirName;

    public ProcessHandler(File dir){
        processBuilder = new ProcessBuilder();
        String info[] = InfoReader.read(dir);

        processBuilder.command( info[0].split(" ")  );

        processBuilder.directory(dir);

        nick = info[1];

        dirName = dir.getName();



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

    public void sendMessage(String line ){
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
