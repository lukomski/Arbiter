package Model;


import Tools.LogWriter;

public class Duel extends Thread{

    private Board board;
    private Player player1;
    private Player player2;

    private Player winner = null;
    private LogWriter logWriter;
    private Tournament tournament;
    private int prevPlayerId = 2;


    public Duel(Player player1, Player player2, Board board, Tournament tournament){
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.tournament = tournament;
        logWriter = new LogWriter("duelLog");
    }

    @Override
    public void run(){
        startDuel();
    }

    private void startDuel(){
        player1.initProcess();
        player2.initProcess();
        sendStartInfo();
        logWriter.writeTitle(board.getSize(),player1.getNick(),player2.getNick());
        doNextMove();
    }

    private void sendStartInfo(){
        player1.sendMessage(board.getSize()+"");
        System.out.println(player1.getMessage());

        player2.sendMessage(board.getSize()+"");
        System.out.println(player2.getMessage());

        player1.sendMessage("START");
    }
    private void sendStopInfo(){
        player1.sendMessage("STOP");
        player2.sendMessage("STOP");
    }

    public Player getWinner(){
        return winner;
    }
    private void closeGame(){
        sendStopInfo();
        tournament.duelEnded();
    }
    public boolean doNextMove(){
        if(prevPlayerId == 2) {
            String move1 = player1.getMessage();
            logWriter.write(player1.getNick(), move1);
            board.fillBoard(move1, 1);
            if (!board.isMovePossible()) {
                winner = player1;
                closeGame();
                return false;
            }
            player2.sendMessage(move1);
            prevPlayerId = 1;
        }else {
            String move2 = player2.getMessage();
            logWriter.write(player2.getNick(), move2);
            board.fillBoard(move2, 2);
            if (!board.isMovePossible()) {
                winner = player2;
                closeGame();
                return false;
            }
            player1.sendMessage(move2);
            prevPlayerId = 2;
        }
        tournament.moveEnded();
        return true;

    }
}
