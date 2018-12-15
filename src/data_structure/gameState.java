package data_structure;

public class gameState {
    private board board;
    private String shortcut;
    private int heuristicValue;

    public gameState(board board, int heuristicValue) {
        this.board = board;
        this.heuristicValue = heuristicValue;
        shortcut = "";

        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j)
                if (this.board.getField(j, i).getType() == field2D.Type.WRITABLE)
                    if (this.board.getField(j, i).getWritable().getState() == fieldWritable.State.UNFILLED)
                        shortcut += "0";
                    else
                        shortcut += this.board.getField(j, i).getWritable().getValue();
        }
    }

    public int getHeuristicValue() {
        return heuristicValue;
    }

    public board getBoard() {
        return board;
    }
}
