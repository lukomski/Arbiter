package Tools;

import Model.Board;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

public class Guide{
    private Board board;
    private List<List<Integer[]>> moves;
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
                List<Integer[]> v = Position.text2ListPositions(s);
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
        try {
            for(List<Integer[]> move: moves){
                System.out.println(Position.positionList2text(move));
            }
            board.fillBoard(moves.get(iterator++), currentPlayerId + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentPlayerId = (currentPlayerId + 1) % 2;
        board.draw();
    }
}
