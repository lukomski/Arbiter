package Model;


import Tools.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private enum FieldStatus{free, firstPlayer, secondPlayer, blocked}
    private FieldStatus[][] matrix;
    private int size;
    private GraphicsContext graphicsContext;
    private String filledStartPoints;

    private double frame;
    private double rectWidth;


    public Board(int size, Canvas canvas){
        filledStartPoints="";
        this.size = size;
        graphicsContext = canvas.getGraphicsContext2D();
        matrix = new FieldStatus[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                matrix[i][j] = FieldStatus.free;
            }
        }

    }
    public void setRandomStartPoints(){
        double rectSize = graphicsContext.getCanvas().getWidth()/size;
        System.out.println("Board: start rand blocked fields");
        clean();
        boolean isNewPoint;
        for(int i=0;i<Math.round(size*size*0.1);i++){
            do {
                Integer[] pos = new Integer[2];
                pos[0] = (int) Math.floor(Math.random() * graphicsContext.getCanvas().getWidth()/rectSize);
                pos[1] = (int) Math.floor(Math.random() * graphicsContext.getCanvas().getWidth()/rectSize);
                isNewPoint = setStartPoint(pos);
            }while(!isNewPoint);
        }
        System.out.println("Board: end rand blocked fields");
    }
    public boolean setStartPoint(Integer[] pos) {
        if(matrix[pos[0]][pos[1]] == FieldStatus.blocked)
            return false;
        matrix[pos[0]][pos[1]] = FieldStatus.blocked;
        System.out.println("Board: clocked + " + pos[0] + " " + pos[1]);
        return true;
    }
    private boolean isFieldFree(FieldStatus field){
        return field == FieldStatus.free;
    }
    public FieldStatus int2FieldStatus(int x){
        if(x == 0){
            return FieldStatus.free;
        } else if(x == 1){
            return FieldStatus.firstPlayer;
        } else if(x == 2){
            return FieldStatus.secondPlayer;
        } else {
            return FieldStatus.blocked;
        }
    }
    public void fillBoard(List<Integer[]> coords, int playerIndex){
        for(Integer[] coord: coords) {
            matrix[coord[0]][coord[1]] = int2FieldStatus(playerIndex);
        }
    }

    public boolean isCoordsCorrect(List<Integer[]> positions){
        for(Integer[] position: positions){
            if(!isFieldFree(matrix[position[0]][position[1]])){
                return false;
            }
        }
        if(!isCoorsNextToEachOther(positions.get(0)[0],positions.get(0)[1],positions.get(1)[0],positions.get(1)[1]))
            return false;
        return true;
    }
    private boolean isCoorsNextToEachOther(int x1, int y1, int x2, int y2){
        return (((x1+1==x2 || x1-1==x2) && y1 == y2) || (x1==x2 && (y1+1==y2 || y1-1==y2)) ||
                ((Math.abs(x1-x2)==size-1) && y1==y2) || ((Math.abs(y1-y2)==size-1) && x1==x2));

    }

    public boolean isMovePossible(){
        for(int i = 0; i< size; i++){
            for(int j = 0; j< size; j++){
                if(isFieldFree(matrix[i][j])){
                    if(isFieldFree(matrix[i][(j + size - 1) % size]))
                        return true;
                    if(isFieldFree(matrix[i][(j + 1) % size]))
                        return true;
                    if(isFieldFree(matrix[(i + size - 1) % size][j]))
                        return true;
                    if(isFieldFree(matrix[(i + 1) % size][j]))
                        return true;
                }
            }
        }
        return false;
    }
    //funkcja do testowania gry bez przechodzenia na druga strone planszy
    public boolean isMovePossible1(){
        for(int i = 0; i< size; i++){
            for(int j = 0; j< size; j++){
                if(matrix[i][j] == FieldStatus.free){
                    if(j!=0 && matrix[i][j-1] == FieldStatus.free)
                        return true;
                    if(j!= size -1 && matrix[i][j+1] == FieldStatus.free)
                        return true;
                    if(i!=0 && matrix[i-1][j] == FieldStatus.free)
                        return true;
                    if(i!= size -1 && matrix[i+1][j] == FieldStatus.free)
                        return true;
                }
            }
        }
        return false;
    }

    public void draw(){
        frame = 5;
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();
        rectWidth = ((canvasWidth < canvasHeight? canvasWidth : canvasHeight)- frame * (size + 1) ) / size;
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0,0,graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                Color color = Color.GREY;
                if (matrix[x][y] == FieldStatus.firstPlayer) {
                    color = Color.BLUE;
                } else if (matrix[x][y] == FieldStatus.secondPlayer) {
                    color = Color.GREEN;
                } else if(matrix[x][y] == FieldStatus.blocked) {
                    color = Color.YELLOW;
                }

                graphicsContext.setFill(color);
                double posX = x * (frame + rectWidth) + frame;
                double posY = y * (frame + rectWidth) + frame;
                graphicsContext.fillRect(posX, posY, rectWidth, rectWidth);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public void hardClean(){
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                    matrix[x][y] = FieldStatus.free;
            }
        }
    }

    public void clean(){
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                if(matrix[x][y] != FieldStatus.blocked) {
                    matrix[x][y] = FieldStatus.free;
                }
            }
        }
    }

    public List<Integer[]> getBlockedPointList() {
        List<Integer[]> positions = new ArrayList<>();
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                if(matrix[x][y] == FieldStatus.blocked){
                    Integer[] position = new Integer[2];
                    position[0] = x;
                    position[1] = y;
                    positions.add(position);
                }
            }
        }
        return positions;
    }
}
