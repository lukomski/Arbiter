package Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Position {
    int x;
    int y;
    Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public static List<Integer[]> text2ListPositions(String text){
        Pattern pattern = Pattern.compile("\\{(\\d+),(\\d+)\\}");
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

    public static String positionList2text(List<Integer[]> positions){
        StringBuilder stringBuilder = new StringBuilder();
        for(Integer[] d: positions){
            if(stringBuilder.length() != 0){
                stringBuilder.append(",");
            }
            stringBuilder.append("{" + d[0] + "," + d[1] + "}");
        }
        return stringBuilder.toString();
    }

    /* Position -> Log */
    @Override
    public String toString(){
        return x + "x" + y;
    }

    public static String pairToString(Position[] positions){
        try {
            return positions[0] + "_" + positions[1];
        } catch(Exception e){
            return null;
        }
    }

    /* Log -> Position */
    private static Position stringToPosition(String text){
        String numbersAsString[] = text.split("x");
        int numbers[] = new int[2];
        numbers[0] = Integer.parseInt(numbersAsString[0]);
        numbers[1] = Integer.parseInt(numbersAsString[1]);
        return new Position(numbers[0], numbers[1]);
    }
    public static Position[] stringPairToPairPosition(String text){
        try {
            Position[] positions = new Position[2];
            String pointsAsString[] = text.split("_");
            positions[0] = stringToPosition(pointsAsString[0]);
            positions[1] = stringToPosition(pointsAsString[1]);
            return positions;
        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException e){
            System.out.println("Position: wrong pairPos:'" + text + "'");
           // e.printStackTrace();
            return null;
        }
    }
}
