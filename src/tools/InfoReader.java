package tools;

import java.io.*;

public class InfoReader {

    public static String[] read(File dir){

        File file = new File(dir.getPath()+"/info.txt");
        FileReader fr=null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader( fr );
        String []info = new String[2];
        try {
            info[0] = bufferedReader.readLine();
            info[1] = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;

    }

}
