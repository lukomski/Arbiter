package Model;


import Tools.LogWriter;
import Tools.Position;

import java.io.IOException;
import java.util.ArrayList;
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

    public void run() throws InterruptedException{
        int playerNotConfirm = 0;
        int i=1;
        for (Player player : players) {
            try {
                player.initProcess();
            } catch (IOException e) {
                errorPlayer = player;
                arena.duelEnded();
                return;
            }

            if(playerNotConfirm==1)
                break;
            System.out.println("Duel: before first send and reeceive " + board.getSize() + player);
            //send board size
            player.sendMessage(board.getSize() + "");
            String message = getMessage(player);
            if(message==null){
                playerNotConfirm=i;
            }
            else if (!message.equals(confirm)) {
                System.out.println("Duel: confirm not correct:" + message);
            }
            System.out.println("Duel: after first send and receive");

            // send start points
            player.sendMessage(Position.positionList2text(board.getBlockedPointList()));
            System.out.println("Duel: blocked points = " + Position.positionList2text(board.getBlockedPointList()) );
            message = getMessage(player);
            if(message==null){
                playerNotConfirm=i;
            }
            else if (!message.equals(confirm)) {
                System.out.println("Duel: confirm not correct:" + message);
            }
            i++;
        }
        // send start message to first player
        players[0].sendMessage("Start");
        currPlayerId = 0;
        logWriter.writeDuelTitle(board.getSize(),Position.positionList2text(board.getBlockedPointList()), players[0].getNick(), players[1].getNick());
        logWriter.writeMessage("$Board:" + Position.positionList2text(board.getBlockedPointList()));

        if(playerNotConfirm!=0){
            winner=players[playerNotConfirm%2];
            winReason="not confirm";
        }
        else{
            while(!exit){
                doNextMove() ;
            }
        }

        sendStopToPlayers();
        logWriter.getPrintWriter().close();
    }

    public List<String> loadLogFile(){
        List<String> moves;
        try {
            moves = logWriter.loadLogFile();
            // set start points
            board.hardClean();
            List<Integer[]> startPoints = Position.text2ListPositions(logWriter.getStartPointsAsText());
            for(Integer[] pos: startPoints){
                board.setStartPoint(pos);
            }
            board.draw();
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

    public boolean doNextMove() throws InterruptedException{

        String message = getMessage(players[currPlayerId]);

        if(message == null){
            winner=players[(currPlayerId+1)%2];
            winReason="not respond within 0.5 sec";
            //sendStopToPlayers();
            exit = true;
            return false;
        }

        List<Integer[]> fields = getFields(message);
        logWriter.writeMessage("$" + currPlayerId + ":" + Position.positionList2text(fields));

        if(fields.size() != 2){
            winReason = "incorrect message:" + " '" + message + "'";
            logWriter.writeMessage(winReason);
            System.out.println("Duel: " + winReason);
            winner = players[(currPlayerId+1)%2];
            exit = true;
            return false;
        }
        if(!board.areFieldsOnBoard(fields)){
            winReason = "positions are out of board:" + " '" + message + "'";
            logWriter.writeMessage(winReason);
            System.out.println("Duel: " + winReason);
            winner = players[(currPlayerId+1)%2];
            exit = true;
            return false;
        }
        if(!board.isPairPosSticky(fields)){
            winReason = "positions are not sticky:" + " '" + message + "'";
            logWriter.writeMessage(winReason);
            System.out.println("Duel: " + winReason);
            winner = players[(currPlayerId+1)%2];
            exit = true;
            return false;
        }
        if(!board.areFieldsFree(fields)){
            winReason = "fields are not free: " + " '" + message + "'";
            logWriter.writeMessage(winReason);
            System.out.println("Duel: " + winReason);
            winner = players[(currPlayerId+1)%2];
            exit = true;
            return false;
        }
        board.fillBoard(fields, currPlayerId + 1);

        if (!board.isMovePossible()) {
            winner = players[currPlayerId];
            winReason="NORMAL WIN";
            logWriter.writeWinner(winner.getNick());
            exit = true;
            return false;
        }
        System.out.println("Duel: Sending = " + Position.positionList2text(fields));
        players[currPlayerId = (currPlayerId + 1) % 2].sendMessage(Position.positionList2text(fields));
        return true;
    }

    private String getMessage(Player player) throws InterruptedException{
        String message = null;
        try {
            message = player.getMessage();
        } catch (TimeoutException e) {
            System.out.println("Duel: timeout Exception");
        } catch (IOException e){
            System.out.println("Duel: IOException during getting message");
        }
        return message;
    }

    private List<Integer[]> getFields(String message){
        List<Integer[]> positions = new ArrayList<>();
        positions = Position.text2ListPositions(message);
        if(positions.size() != 2){
            System.out.println("message:" + message);
            System.out.println("Warning: count of fields is " + positions.size() );
        }
        return positions;
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
