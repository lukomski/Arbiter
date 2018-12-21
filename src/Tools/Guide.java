package Tools;

import Model.Board;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

public class Guide{
    private Board board;
    private List<int[][]> moves;
    private int iterator = 0;
    private int currentPlayerId = 0;
    // flags
    private boolean exit = false;

    // Actions

    public Guide(Board board, List<String> mod) {
        this.board = board;
        // this.moves = moves;
        moves = new ArrayList<>();
        // translate
        for (int i = 0; i < mod.size(); i++) {
            try {
                String s = mod.get(i);
                int[][] v = Player.decrypteMove(s);
                moves.add(v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void nextMove() {
        System.out.println("KEY PRESSED");
        if (iterator >= moves.size()) {
            exit = true;
            return;
        }
        String move = Player.int2strMove(moves.get(iterator++));
        try {
            board.fillBoard(move, currentPlayerId + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentPlayerId = (currentPlayerId + 1) % 2;
        board.draw();
    }
}
