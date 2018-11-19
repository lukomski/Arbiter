package Model;


import tools.InfoHolder;
import tools.LogWriter;

import java.io.File;

public class Duel {

    private Board board;
    private Player player1;
    private Player player2;

    private Player winner = null;
    private LogWriter logWriter;


    public Duel(File player1Dir, File player2Dir){
        board = new Board();
        player1 = new Player(player1Dir);
        player2 = new Player(player2Dir);
        logWriter = new LogWriter("duelLog");
    }

    public Player startDuel(){
        player1.initProcess();
        player2.initProcess();
        sendStartInfo();
        handleDuel();
        sendStopInfo();

        logWriter.writeWinner(winner.getNick());
        System.out.println("Winner: "+winner);

        return winner;
    }

    private void sendStartInfo(){
        player1.sendMessage(InfoHolder.BOARD_SIZE+"");
        System.out.println(player1.getMessage());

        player2.sendMessage(InfoHolder.BOARD_SIZE+"");
        System.out.println(player2.getMessage());

        player1.sendMessage("START");
    }
    private void sendStopInfo(){
        player1.sendMessage("STOP");
        player2.sendMessage("STOP");
    }
    private void handleDuel(){
        logWriter.writeTitle(InfoHolder.BOARD_SIZE,player1.getNick(),player2.getNick());

        while(true){
            String move1 = player1.getMessage();
            logWriter.write(player1.getNick(),move1);
            board.fillBoard(move1);
            if(!board.isMovePossible()){
                winner = player1;
                break;
            }
            player2.sendMessage(move1);
            String move2 = player2.getMessage();
            logWriter.write(player2.getNick(),move2);
            board.fillBoard(move2);
            if(!board.isMovePossible()){
                winner = player2;
                break;
            }
            player1.sendMessage(move2);

        }
    }
}
