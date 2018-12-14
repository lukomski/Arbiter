package Model;


import Tools.LogWriter;

public class Duel extends Thread{
    private int miliseconds4Answere = 5000;

    private Board board;
    private Player[] players;
    private Player winner = null;
    private LogWriter logWriter;
    private Arena arena;
    private int currPlayerId = 0;
    private String answer;


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
            if(!gettingAnswer(player)){
                System.out.println("brak odpowiedzi");
            }
        }
        players[0].sendMessage("START");
        logWriter.writeTitle(board.getSize(),players[0].getNick(),players[1].getNick());
        doNextMove();
    }
    private boolean gettingAnswer(Player player){
        int shifts = 10;
        int timeShift = miliseconds4Answere/shifts;
        int shiftCounter = 0;
        while(!player.isAnswer()){
            shiftCounter++;
            if(shiftCounter++ == shifts){
                return false;
            }

            try {
                Thread.sleep(timeShift);
            } catch(Exception e){
                return false;
            }
        }
        answer = player.getAnswer();
        return true;
    }

    private void closeGame(){
        for(Player player: players){
            player.sendMessage("STOP");
        }
        arena.duelEnded();
    }

    public boolean doNextMove() {
        if (!board.isMovePossible()) {
            winner = players[(currPlayerId + 1) % 2];
            closeGame();
            return false;
        }

        if(!gettingAnswer(players[currPlayerId]));
       // String move = players[currPlayerId].getAnswer();
        logWriter.write(players[currPlayerId].getNick(), answer);
        try {
            board.fillBoard(answer, currPlayerId + 1);
            players[currPlayerId = (currPlayerId + 1) % 2].sendMessage(answer);
            arena.moveEnded();
            return true;
        } catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Nieprawisłowy komunikat od programu: " + answer);
        } catch (NullPointerException e){
            System.out.println("Puste dane");
        }
        return false;
    }

    public Player getWinner(){
        return winner;
    }
}
