package logic;

import tools.InfoHolder;

public class Board {
    private boolean [][]matrix;

    public Board(){
        matrix = new boolean[InfoHolder.BOARD_SIZE][InfoHolder.BOARD_SIZE];
        for(int i=0;i<InfoHolder.BOARD_SIZE;i++){
            for(int j=0;j<InfoHolder.BOARD_SIZE;j++){
                matrix[i][j]=false;
            }
        }
    }
    public void fillBoard(String coords){
        String cord[] = coords.split("_");
        String c1[] = cord[0].split("x");
        String c2[] = cord[1].split("x");
        int x1 = Integer.parseInt(c1[0]);
        int y1 = Integer.parseInt(c1[1]);
        int x2 = Integer.parseInt(c2[0]);
        int y2 = Integer.parseInt(c2[1]);
        matrix[x1][y1]=true;
        matrix[x2][y2]=true;

    }
    public boolean isMovePossible(){
        for(int i=0;i<InfoHolder.BOARD_SIZE;i++){
            for(int j=0;j<InfoHolder.BOARD_SIZE;j++){
                if(!matrix[i][j]){
                    if(j!=0 && !matrix[i][j-1])
                        return true;
                    if(j!=InfoHolder.BOARD_SIZE-1 && !matrix[i][j+1])
                        return true;
                    if(i!=0 && !matrix[i-1][j])
                        return true;
                    if(i!=InfoHolder.BOARD_SIZE-1 && !matrix[i+1][j])
                        return true;
                }
            }
        }
        return false;
    }
    public void printBoard(){
        for(int i=0;i<InfoHolder.BOARD_SIZE;i++){
            for(int j=0;j<InfoHolder.BOARD_SIZE;j++){
                if(matrix[i][j])
                    System.out.print(1);
                else
                    System.out.print(0);
            }
            System.out.println("");

        }
    }
}
