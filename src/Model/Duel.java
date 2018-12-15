package Model;


import Tools.LogWriter;

public class Duel extends Thread{

    private Board board;
    private Player[] players;
    private Player winner = null;
    private LogWriter logWriter;
    private Arena arena;
    private int currPlayerId = 0;
    private String winReason="NOT RESPOND";
    private boolean isCloseGame = false;


    public Duel(Player[] players, Arena arena){
        this.board = arena.getBoard();
        this.players = players;
        this.arena = arena;

        logWriter = new LogWriter(players[0].getDirName()+"vs"+players[1].getDirName());
    }

    @Override
    public void run(){

        for(Player player: players){
            player.initProcess();
            //send start info
            player.sendMessage(board.getSize() + board.getFilledStartPoints());
            String tt=null;
          if((tt =gettingAnswer(player))==null){
              winner=players[(player.getId()+1)%2];
              isCloseGame = true;
          }
            System.out.println(tt);



        }
        if(isCloseGame)
        {
            closeGame();
            return;
        }


        players[0].sendMessage("START");
        logWriter.writeDuelTitle(board.getSize(),board.getFilledStartPoints(),players[0].getNick(),players[1].getNick());
        doNextMove();
    }

    private void closeGame(){
        for(Player player: players){
            player.sendMessage("STOP");
        }
        arena.duelEnded();
    }

    public boolean doNextMove() {


        String move = gettingAnswer(players[currPlayerId]);

        if(move==null){
            winner=players[(currPlayerId+1)%2];
            winReason="NOT RESPOND WITHIN 0.5 SEC";
            closeGame();
            return false;
        }
        logWriter.writeDuelMove(players[currPlayerId].getNick(), move);
        board.fillBoard(move, currPlayerId + 1);
        if (!board.isMovePossible()) {
            winner = players[currPlayerId];
            winReason="NORMAL WIN";
            logWriter.writeWinner(winner.getNick());
            closeGame();
            return false;
        }
        players[currPlayerId = (currPlayerId + 1) % 2].sendMessage(move);
        arena.moveEnded();
        return true;
    }
    private String gettingAnswer(Player player){
        int shifts = 10;
        int timeShift = 500/shifts;
        int shiftCounter = 0;
        boolean answer = false;
        String message=null;
        while(!answer){

            if((message = player.getMessage())!=null){
                answer=true;

                break;
            }



            if(shiftCounter++ == shifts){
                break;
            }
            try {
                Thread.sleep(timeShift);
            } catch(Exception e){
                break;
            }
        }
        return message;


    }

    public Player getWinner(){
        return winner;
    }
    public String getPlayer1Name(){
        return players[0].getNick();
    }
    public String getPlayer2Name(){
        return players[1].getNick();
    }
    public String getWinReason(){
        return winReason;
    }
}
