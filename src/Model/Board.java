package Model;


import Tools.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
        clean();
        boolean isNewPoint;
        for(int i=0;i<Math.round(size*size*0.1);i++){
            do {
                isNewPoint = setStartPoint(Math.random() * graphicsContext.getCanvas().getWidth(),
                        Math.random() * graphicsContext.getCanvas().getWidth());
            }while(!isNewPoint);
        }
    }
    public boolean setStartPoint(double x, double y) {
        double rectSize = graphicsContext.getCanvas().getWidth()/size;
        int posX = (int)Math.floor(x/rectSize);
        int posY=(int)Math.floor(y/rectSize);
        if(matrix[posX][posY] == FieldStatus.blocked)
            return false;
        matrix[posX][posY] = FieldStatus.blocked;

        filledStartPoints+="_"+posX+"x"+posY;

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
    public void fillBoard(Position[] coords, int playerIndex) throws Exception{

        if(!isCoordsCorrect(coords))
            throw new Exception();
        matrix[coords[0].getX()][coords[0].getY()] = int2FieldStatus(playerIndex);
        matrix[coords[1].getX()][coords[1].getY()] = int2FieldStatus(playerIndex);
    }

    public boolean isCoordsCorrect(Position[] positions){
        for(Position position: positions){
            if(!isFieldFree(matrix[position.getX()][position.getY()])){
                return false;
            }
        }
        return true;
    }

    public boolean isMovePossible1(){
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
    public boolean isMovePossible(){
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
        rectWidth = ( graphicsContext.getCanvas().getWidth() - frame * (size + 1) ) / size;
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

    public void clean(){
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                    matrix[x][y] = FieldStatus.free;
            }
        }
    }
}
