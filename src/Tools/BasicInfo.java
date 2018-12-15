package Tools;

import java.io.*;

public class BasicInfo {
    private String command;
    private String nick;
    private File directory;

    public BasicInfo(File dir) throws Exception {
        this.directory = dir;

        File file = new File(dir.getPath() + "/info.txt");
        FileReader fr =  new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fr);

        command = bufferedReader.readLine();
        nick = bufferedReader.readLine();

    }
    public String getCommand(){
        return command;
    }
    public String getNick(){
        return nick;
    }
    public File getDirectory(){
        return directory;
    }


}
