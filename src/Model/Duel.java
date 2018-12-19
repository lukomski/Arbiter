package Model;


import Tools.LogWriter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Duel{

    private Board board;
    private Player[] players;
    private Player winner = null;
    private LogWriter logWriter;
    private Arena arena;
    private int currPlayerId = 0;
    private String winReason="NOT RESPOND";
    private boolean isCloseGame = false;
    private Player errorPlayer;


    public Duel(Player[] players, Arena arena){
        this.board = arena.getBoard();
        this.players = players;
        this.arena = arena;

        logWriter = new LogWriter(players[0].getDirName()+"vs"+players[1].getDirName());
    }

    public void run(){

        for(Player player: players){
            try {
                player.initProcess();
            } catch (IOException e) {
                errorPlayer=player;
                arena.duelEnded();
                return;
            }
            //send start info
            player.sendMessage(board.getSize() + board.getFilledStartPoints());

          if(!player.isHumanPlayer()) {
              if (gettingAnswer(player) == null) {
                  winner = players[(player.getId() + 1) % 2];
                  isCloseGame = true;
              }
          }

        }
        if(isCloseGame)
        {
            closeGame();
            return;
        }

        players[0].sendMessage("START");
        logWriter.writeDuelTitle(board.getSize(),board.getFilledStartPoints(),players[0].getNick(),players[1].getNick());
        while(doNextMove(""));
    }

    private void closeGame(){
        for(Player player: players){
            player.sendMessage("STOP");
        }
        arena.duelEnded();
    }

    public boolean doNextMove(String humanMove) {
        String move = gettingAnswer(players[currPlayerId]);


        if(move==null){
            winner=players[(currPlayerId+1)%2];
            winReason="NOT RESPOND WITHIN 0.5 SEC";
            closeGame();
            return false;
        }

        logWriter.writeDuelMove(players[currPlayerId].getFullName(), move);
        try {
            board.fillBoard(move, currPlayerId + 1);
        } catch (Exception e) {
            winner=players[(currPlayerId+1)%2];
            winReason="WRONG MESSAGE";
            closeGame();
            return false;
        }
        if (!board.isMovePossible()) {
            winner = players[currPlayerId];
            winReason="NORMAL WIN";
            logWriter.writeWinner(winner.getNick());
            closeGame();
            return false;
        }
        players[currPlayerId = (currPlayerId + 1) % 2].sendMessage(move);
        return true;
    }

    private String gettingAnswer(Player player){
        String message = null;
        try {
            message = player.getMessage();
        } catch (TimeoutException e){
            System.out.println("Duel: timeout Exception");
            message = null;
        } catch (Exception e){

        }
        return message;


    }

    public Player getWinner(){
        return winner;
    }
    public String getPlayer1Name(){
        return players[0].getNick();
    }
    public String getPlayer1FullName(){
        return players[0].getFullName();
    }
    public String getPlayer2FullName(){
        return players[1].getFullName();
    }
    public String getPlayer2Name(){
        return players[1].getNick();
    }
    public String getWinReason(){
        return winReason;
    }
    public Player getPlayer1(){
        return players[0];
    }
    public Player getPlayer2(){
        return players[1];
    }
    public Player getErrorPlayer(){
        return errorPlayer;
    }
}
