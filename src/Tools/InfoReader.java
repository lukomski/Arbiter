package Tools;

import java.io.*;

public class InfoReader {

    public static String[] read(File dir) throws Exception{

        File file = new File(dir.getPath()+"/info.txt");

        FileReader fr=null;

            fr = new FileReader(file);

        BufferedReader bufferedReader = new BufferedReader( fr );
        String []info = new String[2];

            info[0] = bufferedReader.readLine();
            info[1] = bufferedReader.readLine();


        return info;

    }

}
