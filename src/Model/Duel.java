package Model;


import Tools.LogWriter;
import Tools.Position;
import javafx.geometry.Pos;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Duel{

    private Board board;
    private Player[] players;
    private Player winner = null;
    private LogWriter logWriter;
    private Arena arena;
    private int currPlayerId = 0;
    private String winReason="NOT RESPOND";
    private Player errorPlayer;
    private List moveList;
    private boolean exit = false;

    private final static String confirm = "ok";


    public Duel(Player[] players, Arena arena){
        this.board = arena.getBoard();
        this.players = players;
        this.arena = arena;
        logWriter = new LogWriter(players[0].getDirName()+"vs"+players[1].getDirName());
    }

    public void run() {
        for (Player player : players) {
            try {
                player.initProcess();
            } catch (IOException e) {
                errorPlayer = player;
                arena.duelEnded();
                return;
            }
            //send start info
            player.sendMessage(board.getSize() + ""/* + board.getFilledStartPoints()*/);
            // return OK
            String message = getMessage(player);
            if (!message.equals(confirm)) {
                System.out.println(message);
                System.out.println("WARNING Duel: confirm getting start points is not expected");
             }
        }
        // send start message to first player
        players[0].sendMessage("Start");
        currPlayerId = 0;
        logWriter.writeDuelTitle(board.getSize(),"TODO start points", players[0].getNick(), players[1].getNick());
        while(!exit){
            doNextMove() ;
        }
        sendStopToPlayers();
        logWriter.getPrintWriter().close();
    }
    public List<String> loadLogFile(){
        List<String> moves;
        try {
            moves = logWriter.loadLogFile();
        } catch(IOException e){
            System.out.println("Duel: the file is not readable" );
            return null;
        }
        return moves;
    }

    private void sendStopToPlayers(){
        for(Player player: players){
            player.sendMessage("STOP");
            player.destroy();
        }
        arena.duelEnded();
    }

    public boolean doNextMove() {

        String message = getMessage(players[currPlayerId]);
        Position[] fields = getFields(message);

        if(fields == null){
            winner=players[(currPlayerId+1)%2];
            winReason="NOT RESPOND WITHIN 0.5 SEC";
            sendStopToPlayers();
            exit = true;
            return false;
        }

        logWriter.writeMessage("$" + currPlayerId + ":" + Position.pairToString(fields));
        if(!board.isCoordsCorrect(fields)){
            logWriter.writeMessage("field is not free");
            System.out.println("Duel: field is not free");
            winner = players[(currPlayerId+1)%2];
            winReason="ALZHEIMER";
            sendStopToPlayers();
            exit = true;
            return false;
        }
        try {
            board.fillBoard(fields, currPlayerId + 1);
        } catch (Exception e) {
            System.out.println("Duel: field out of bouds or not free");
            winner=players[(currPlayerId+1)%2];
            winReason="WRONG MESSAGE";
            sendStopToPlayers();
            exit = true;
            return false;
        }
        if (!board.isMovePossible()) {
            winner = players[currPlayerId];
            winReason="NORMAL WIN";
            logWriter.writeWinner(winner.getNick());
            sendStopToPlayers();
            exit = true;
            return false;
        }
        System.out.println("Duel: Sending = " + Position.pairToString(fields));
        players[currPlayerId = (currPlayerId + 1) % 2].sendMessage(Position.pairToString(fields));
        return true;
    }

    private String getMessage(Player player){
        String message = null;
        try {
            message = player.getMessage();
        } catch (TimeoutException e) {
            System.out.println("Duel: timeout Exception");
        } catch (Exception e){
            System.out.println("Duel: Something goes wrong but nobody knows what");
        }
        return message;
    }

    private Position[] getFields(String message){
        Position[] fields = null;
        try {
            fields = Position.stringPairToPairPosition(message);
        } catch(NullPointerException | IndexOutOfBoundsException e){
            System.out.println("msg: " + message);
            System.out.println("Duel: Wrong message format Exception");
        } catch (Exception e){
            System.out.println("Duel: Something goes wrong but nobody knows what");
        }
        return fields;
    }



    @Override
    public String toString() {
        return players[0].getNick()+" vs "+players[1].getNick()+" Winner: "+winner.getNick()+"\n"+winReason;
    }
    public boolean isNormalWin(){
        return winReason=="NORMAL WIN";
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
