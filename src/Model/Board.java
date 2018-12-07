package Model;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board {
    private int [][]matrix;
    private int boardSize;
    private GraphicsContext graphicsContext;

    public Board(int boardSize, Canvas canvas){
        this.boardSize = boardSize;
        graphicsContext = canvas.getGraphicsContext2D();
        matrix = new int[boardSize][boardSize];
        for(int i = 0;i < boardSize;i++){
            for(int j = 0;j < boardSize;j++){
                matrix[i][j] = 0;
            }
        }
    }
    public void fillBoard(String coords,int playerIndex){
        String cord[] = coords.split("_");
        String c1[] = cord[0].split("x");
        String c2[] = cord[1].split("x");
        int x1 = Integer.parseInt(c1[0]);
        int y1 = Integer.parseInt(c1[1]);
        int x2 = Integer.parseInt(c2[0]);
        int y2 = Integer.parseInt(c2[1]);
        matrix[x1][y1]=playerIndex;
        matrix[x2][y2]=playerIndex;

        //boardDraw.drawRect(x1,y1,x2,y2,playerIndex);

    }
    public boolean isMovePossible(){
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                if(matrix[i][j] == 0){
                    if(j!=0 && matrix[i][j-1] == 0)
                        return true;
                    if(j!=boardSize-1 && matrix[i][j+1] == 0)
                        return true;
                    if(i!=0 && matrix[i-1][j] == 0)
                        return true;
                    if(i!=boardSize-1 && matrix[i+1][j] == 0)
                        return true;
                }
            }
        }
        return false;
    }

    public void draw(){
        double frame = 5;
        double rectWidth = ( graphicsContext.getCanvas().getWidth() - frame * (boardSize + 1) ) / boardSize;
        System.out.println("lineWidth:" + rectWidth);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0,0,graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
        for(int x = 0; x < boardSize; x++){
            for(int y = 0; y < boardSize; y++){
                graphicsContext.setFill( matrix[x][y] == 0 ? Color.RED : Color.BLUE );
                double posX = x * (frame + rectWidth) + frame;
                double posY = y * (frame + rectWidth) + frame;
                graphicsContext.fillRect(posX, posY, rectWidth, rectWidth);
            }
        }
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void printBoard(){
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
               System.out.print(matrix[i][j]);
            }
            System.out.println("");

        }
    }
}
