package solver;


import data_structure.board;
import data_structure.gameState;

import java.util.Comparator;
import java.util.PriorityQueue;

public class solver {

    Comparator<gameState> comparator = new Comparator<gameState>() {
        @Override
        public int compare(gameState o1, gameState o2) {
            return o1.getHeuristicValue() - o2.getHeuristicValue();
        }
    };

    private PriorityQueue<gameState> queue = new PriorityQueue<>(comparator);

    public solver(board board, int initialValue) {
        gameState initial = new gameState(board, initialValue);
        queue.add(initial);
    }


    public board solve() {
        //TODO

        gameState analyzed = queue.remove();
        board board = analyzed.getBoard();

        //by commenting you can see how possibilities changes
        //example.txt
        /*board.setField(4,1,9);
        board.setField(4,2,7);
        board.setField(1,4,2);
        board.setField(1,3,9);
        board.setField(2,4,1);
        board.setField(3,2,9);
        board.setField(2,2,8);
        board.setField(2,1,6);
        board.setField(3,1,8);
        board.setField(2,3,2);
        board.setField(3,3,7);*/

        //board_6x6.txt
        board.setField(5, 2, 1);
        board.setField(6, 1, 9);
        board.setField(6, 2, 2);
        board.setField(4, 2, 3);
        board.setField(2, 1, 1);
        board.setField(2, 6, 8);
        board.setField(1, 6, 1);
        board.setField(1, 5, 2);
        board.setField(3, 5, 1);
        board.setField(5, 6, 9);
        board.setField(5, 5, 7);
        board.setField(5, 4, 4);
        board.setField(6, 5, 1);
        board.setField(6, 4, 2);
        board.setField(4, 4, 1);
        board.setField(3, 4, 3);
        board.setField(3, 3, 6);
        board.setField(1, 2, 9);
        board.setField(2, 2, 8);
        board.setField(1, 3, 7);
        board.setField(2, 3, 3);
        board.setField(4, 3, 9);

        return board;
    }
}
