package GUI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BoardDraw {
    GraphicsContext gc;
    int boardSize;
    float rectSize;
    float canvasSize = 700;
    float numberOfLines;
    public BoardDraw(Canvas canvas, int boardSize){
        this.boardSize=boardSize;
        gc= canvas.getGraphicsContext2D();
        gc.setLineWidth(1);
        float canvasSize = 700;
        numberOfLines = boardSize-1;
        rectSize =((canvasSize-numberOfLines*(float)gc.getLineWidth())/boardSize);

    }
    public void drawLines(){


        float x;

        for(int i=0;i<(boardSize-1);i++){
            x=((canvasSize-numberOfLines)/boardSize)*(i+1)+i+(float)gc.getLineWidth()/2;

            gc.strokeLine(x,0,x,canvasSize);
            gc.strokeLine(0,x,canvasSize,x);
        }
    }
    public void drawRect(int x1, int y1, int x2, int y2,int playerIndex){

        gc.setFill( playerIndex == 0 ? Color.RED : Color.BLUE );

        gc.fillRect(x1* rectSize +x1*gc.getLineWidth(),y1* rectSize +y1*gc.getLineWidth(), rectSize, rectSize);
        gc.fillRect(x2* rectSize +x2*gc.getLineWidth(),y2* rectSize +y2*gc.getLineWidth(), rectSize, rectSize);


    }
    public void clearBoard(){
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }
    public void setBoardSize(int boardSize){
        this.boardSize=boardSize;

        numberOfLines = boardSize-1;
        rectSize =((canvasSize-numberOfLines*(float)gc.getLineWidth())/boardSize);
    }
}
