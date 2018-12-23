package Model;


import Tools.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board {
    private enum FieldStatus{free, firstPlayer, secondPlayer, blocked}
    private int [][]matrix;
    private int size;
    private GraphicsContext graphicsContext;
    private String filledStartPoints;

    private double frame;
    private double rectWidth;
    private enum Direction{
        north, east, south, west;
    }

    public Board(int size, Canvas canvas){
        filledStartPoints="";
        this.size = size;
        graphicsContext = canvas.getGraphicsContext2D();
        matrix = new int[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                matrix[i][j] = 0;
            }
        }

    }
    public void setRandomStartPoints(){
        clearFromPoints();
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
        if(matrix[posX][posY] == 3)
            return false;
        matrix[posX][posY] = 3;

        filledStartPoints+="_"+posX+"x"+posY;

        draw();
        return true;
    }
    private boolean isFieldFree(int field){
        return field == 0 || field == 4 || field == 5;
    }
    public void fillBoard(Position[] coords, int playerIndex) throws Exception{
        if(!isCoordsCorrect(coords))
            throw new Exception();
        matrix[coords[0].getX()][coords[0].getY()]=playerIndex;
        matrix[coords[1].getX()][coords[1].getY()]=playerIndex;
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
                if(matrix[i][j] == 0){
                    if(j!=0 && matrix[i][j-1] == 0)
                        return true;
                    if(j!= size -1 && matrix[i][j+1] == 0)
                        return true;
                    if(i!=0 && matrix[i-1][j] == 0)
                        return true;
                    if(i!= size -1 && matrix[i+1][j] == 0)
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
                if (matrix[x][y] == 1) {
                    color = Color.BLUE;
                } else if (matrix[x][y] == 2) {
                    color = Color.GREEN;
                } else if(matrix[x][y] == 3) {
                    color = Color.YELLOW;
                } else if( matrix[x][y]==4){
                    color = Color.DARKGREY;
                }else if( matrix[x][y]==5){
                    color = Color.DARKGREY;
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
                if(matrix[x][y]!=3)
                    matrix[x][y] = 0;
            }
        }
    }
    public void cleanHover(){
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                if(matrix[x][y]==4 || matrix[x][y]==5)
                    matrix[x][y] = 0;
            }
        }
        draw();
    }

    public void clearFromPoints(){
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                    matrix[x][y] = 0;
            }
        }
        filledStartPoints = "";
        draw();
    }

    public String getFilledStartPoints() {
        String s = new String();
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                if(matrix[x][y] != 0){
                    s +=" " + x + "x" + y;
                }
            }
        }
        return s;
    }
    public int countPosition(double x){
        double rectSize = graphicsContext.getCanvas().getWidth()/size;
        int position = (int)Math.floor(x/rectSize);
        return position;
    }
    public String getHovers(){
        int field4X = -1;
        int field4Y = -1;
        int field5X = -1;
        int field5Y = -1;
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                if(matrix[x][y] == 4){
                    field4X = x;
                    field4Y = y;
                }
                if(matrix[x][y] == 5){
                    field5X = x;
                    field5Y = y;
                }
            }
        }
        if(field4X == -1 || field5X == -1){
            return null;
        } else {
            return field4X + "x" + field4Y + "_" + field5X + "x" + field5Y;
        }
    }

    public void printBoard(){
        for(int i = 0; i< size; i++){
            for(int j = 0; j< size; j++){
               System.out.print(matrix[i][j]);
            }
            System.out.println("");
        }
    }


}
