package data_structure;

import java.util.Vector;

public class GameState {
    private boolean isSolved;
    private String boardShortcut;
    private int heuristicValue;
    private Board board;

    public GameState(String boardShortcut, int heuristicValue, boolean isSolved, Board board) {
        this.heuristicValue = heuristicValue;
        this.boardShortcut = boardShortcut;
        this.isSolved = isSolved;
        this.board = board;
    }

    public int getHeuristicValue() {
        return heuristicValue;
    }

    public String getBoardShortcut() {
        return boardShortcut;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public Board getBoard() {
        return board;
    }
}
