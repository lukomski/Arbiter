package Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Position {
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
}
