package Program;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {
    private final static String startMessage = "START";
    private final static String confirmMessage = "OK";
    private BufferedReader bufferedReader;
    /* true means that field is free */
    private boolean[][] board = null;
    private int size;

    private String getMessage(){
        String message = null;
        try {
            message = bufferedReader.readLine();
        } catch(Exception e){
            e.printStackTrace();
        }
        return message;
    }
    private void sendMessage(String message){
        System.out.println(message);
    }
    private String onePosition2Text(int x, int y){
        return "{" + x + ";" + y + "}";
    }
    private String positions2Text(int x1, int y1, int x2, int y2){
        return onePosition2Text(x1, y1) + "," + onePosition2Text(x2, y2);
    }


    private List<Integer[]> generateNextMove(){
        List<Integer[]> positions = new ArrayList<>();
        positions.add(new Integer[2]); // first field
        positions.add(new Integer[2]); // second field
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                if(!board[x][y]){
                    continue;
                }
                positions.get(0)[0] = x;
                positions.get(0)[1] = y;
                positions.get(1)[0] = x;
                positions.get(1)[1] = y;
                // right
                if(board[(x + 1) % size][y]){
                    positions.get(1)[0] = (x + 1) % size;
                    return positions;
                }
                // left
                if(board[(x + size - 1) % size][y]){
                    positions.get(1)[0] = (x + size - 1) % size;
                    return positions;
                }
                // up
                if(board[x][(y + 1) % size]){
                    positions.get(1)[1] = (y + 1) % size;
                    return positions;
                }
                // down
                if(board[x][(y + size - 1) % size]){
                    positions.get(1)[1] = (y + size - 1) % size;
                    return positions;
                }
            }
        }
        return null;
    }
    private void fillFields(List<Integer[]> positions){
        for(Integer[] position: positions){
            board[position[0]][position[1]] = false;
        }
    }

    private void run(){
        boolean exit = false;
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        bufferedReader = new BufferedReader(inputStreamReader);

        // get board sizee
        String message = getMessage();
        //confirm getting board size
        sendMessage(confirmMessage);

        // create board
        size = Integer.parseInt(message);
        board = new boolean[size][size];
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                board[x][y] = true; // free
            }
        }
        // get start points
        message = getMessage();
        sendMessage(confirmMessage);
        fillFields(text2ListPositions(message));

        while (!exit) {
            message = getMessage();
            // actualize board
            fillFields(text2ListPositions(message));
            List<Integer[]> myMove = generateNextMove();
            sendMessage(positionList2text(myMove));
            fillFields(myMove);
         //   draw();
        }
    }

    private void draw(){
        System.out.println("draw:");
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                System.out.print((board[x][y])?1:0);
            }
            System.out.println();
        }
    }
    public List<Integer[]> text2ListPositions(String text){
        Pattern pattern = Pattern.compile("\\{(\\d+);(\\d+)\\}");
        Matcher matcher = pattern.matcher(text);
        ArrayList<Integer[]> positions = new ArrayList<>();
        while(matcher.find()) {
            Integer[] position = new Integer[2];
            position[0] = Integer.parseInt(matcher.group(1));
            position[1] = Integer.parseInt(matcher.group(2));
            positions.add(position);
        }
        return positions;
    }
    public String positionList2text(List<Integer[]> positions){
        StringBuilder stringBuilder = new StringBuilder();
        for(Integer[] d: positions){
            if(stringBuilder.length() != 0){
                stringBuilder.append(",");
            }
            stringBuilder.append("{" + d[0] + ";" + d[1] + "}");
        }
        return stringBuilder.toString();
    }
    public static void main(String[] args) {
        Program program = new Program();
        program.run();
    }
}
