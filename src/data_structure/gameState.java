package data_structure;

public class gameState {
    boolean isSolved;
    private String boardShortcut;
    private int heuristicValue;

    public gameState(String boardShortcut, int heuristicValue, boolean isSolved) {
        this.heuristicValue = heuristicValue;
        this.boardShortcut = boardShortcut;
        this.isSolved = isSolved;
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
}
