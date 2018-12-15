package Model;


import Tools.LogWriter;

public class Duel extends Thread{

    private Board board;
    private Player[] players;
    private Player winner = null;
    private LogWriter logWriter;
    private Arena arena;
    private int currPlayerId = 0;


    public Duel(Player[] players, Arena arena){
        this.board = arena.getBoard();
        this.players = players;
        this.arena = arena;
        logWriter = new LogWriter("duelLog");
    }

    @Override
    public void run(){
        for(Player player: players){
            player.initProcess();
            //send start info
            player.sendMessage(board.getSize() + board.getFilledStartPoints());
            System.out.println(player.getMessage());
        }
        players[0].sendMessage("START");
        logWriter.writeTitle(board.getSize(),players[0].getNick(),players[1].getNick());
        doNextMove();
    }

    private void closeGame(){
        for(Player player: players){
            player.sendMessage("STOP");
        }
        arena.duelEnded();
    }

    public boolean doNextMove() {
        String move = players[currPlayerId].getMessage();
        logWriter.write(players[currPlayerId].getNick(), move);
        board.fillBoard(move, currPlayerId + 1);
        if (!board.isMovePossible()) {
            winner = players[currPlayerId];
            closeGame();
            return false;
        }
        players[currPlayerId = (currPlayerId + 1) % 2].sendMessage(move);
        arena.moveEnded();
        return true;
    }

    public Player getWinner(){
        return winner;
    }
}
