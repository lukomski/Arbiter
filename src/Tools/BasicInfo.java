package Tools;

import java.io.*;

public class BasicInfo {
    private String command;
    private String nick;
    private String name;
    private File directory;

    public BasicInfo(File dir) throws IOException {
        this.directory = dir;

        File file = new File(dir.getPath() + "/info.txt");
        FileReader fr =  new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fr);


        nick = bufferedReader.readLine();
        name = bufferedReader.readLine();
        command = bufferedReader.readLine();

        if (isExeFile(command))
            command = "cmd /c " + command;


    }
    public BasicInfo(){
        nick = "Useros";
        name = "User Userowski";
        directory = new File("User");
        command = "";
    }
    private boolean isExeFile(String command){
        String []commandTab = command.split("\\.");

        if(commandTab.length>0)
            if(commandTab[commandTab.length-1].equals("exe"))
                return true;
        return false;
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
    public String getName() {
        return name;
    }
    public String getFullName(){
        return directory.getName() + "- "+name+" '"+nick+"'";
    }
}
