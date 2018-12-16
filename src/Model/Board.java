package Model;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board {
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
    public boolean hoverRect(double x, double y, int angle, boolean rightMouseClick){
        // clean hover
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if(matrix[i][j] == 4 || matrix[i][j] == 5 )
                    matrix[i][j] = 0;
            }
        }

        // choose direction
        Direction direction;
        double width = graphicsContext.getCanvas().getWidth() - frame;
        double fieldWidth = width / size;
        int matrixX = (int)Math.floor( (x - (frame / 2) ) / fieldWidth);
        int matrixY = (int)Math.floor( (y - (frame / 2) ) / fieldWidth);
        // choosing start points
        if(angle == -1){
            matrix[matrixX][matrixY] = 4;
            draw();
            return true;
        }

        double posXInField = (x - (frame / 2)) - matrixX * fieldWidth;
        double posYInField = (y - (frame / 2)) - matrixY * fieldWidth;
        System.out.println("podInField" + posXInField + " " + posYInField);
        /* north or west*/
        if(posYInField >= posXInField){
            if(posYInField >= (fieldWidth - posXInField)){
                direction = Direction.south;
            } else {
                direction = Direction.west;
            }
        }
        /* east or south */
        else {
            if(posYInField >= (fieldWidth - posXInField)){
                direction = Direction.east;
            } else {
                direction = Direction.north;
            }

        }
        System.out.println("direction:" + direction);
        switch (direction){
            case north:
            {
                int mY = (matrixY + size - 1) % size;
                if(isFieldFree(matrix[matrixX][matrixY]) && isFieldFree(matrix[matrixX][mY])){
                    matrix[matrixX][matrixY] = 4;
                    matrix[matrixX][mY] = 5;
                }
                break;
            }
            case east:
            {
                int mX = (matrixX + 1) % size;
                if(isFieldFree(matrix[matrixX][matrixY]) && isFieldFree(matrix[mX][matrixY])) {
                    matrix[matrixX][matrixY] = 4;
                    matrix[mX][matrixY] = 5;
                    break;
                }
            }
            case south:{
                int mY = (matrixY + 1) % size;
                if(isFieldFree(matrix[matrixX][matrixY]) && isFieldFree(matrix[matrixX][mY])){
                    matrix[matrixX][matrixY] = 4;
                    matrix[matrixX][mY] = 5;
                    break;
                }
            }
            case west:{
                int mX = (matrixX + size - 1) % size;
                if(isFieldFree(matrix[matrixX][matrixY]) && isFieldFree(matrix[mX][matrixY])){
                    matrix[matrixX][matrixY] = 4;
                    matrix[mX][matrixY] = 5;
                    break;
                }
            }
        }
        draw();
        return true;

    }
    public void fillBoard(String coords,int playerIndex) throws Exception{
        String cord[] = coords.split("_");
        String c1[] = cord[0].split("x");
        String c2[] = cord[1].split("x");
        int x1 = Integer.parseInt(c1[0]);
        int y1 = Integer.parseInt(c1[1]);
        int x2 = Integer.parseInt(c2[0]);
        int y2 = Integer.parseInt(c2[1]);
        if(!isCoordsCorrect(x1,y1,x2,y2))
            throw new Exception();
        matrix[x1][y1]=playerIndex;
        matrix[x2][y2]=playerIndex;


    }
    public boolean isCoordsCorrect(int x1, int y1, int x2, int y2){

        return isFieldFree(matrix[x1][y1]) && isFieldFree(matrix[x2][y2]);
    }

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
        return filledStartPoints;
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
