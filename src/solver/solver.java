package solver;


import data_io.DataInput;
import data_structure.board;
import data_structure.gameState;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

public class solver {

    Comparator<gameState> comparator = new Comparator<gameState>() {
        @Override
        public int compare(gameState o1, gameState o2) {
            return o1.getHeuristicValue() - o2.getHeuristicValue();
        }
    };

    private PriorityQueue<gameState> queue = new PriorityQueue<>(comparator);
    private board template;

    public solver(board board, int initialValue) {
//        template = board;board.setField(4,1,9);
//        System.out.println("solver() board\n" + board.toString());
        template = new board(board);
//        System.out.println("solver() template\n" + template.toString());
//        template.setField(4,2,7);
//        template.setField(1,4,2);
//        template.setField(1,3,9);
//        template.setField(2,4,1);
//        template.setField(3,2,9);
//        template.setField(2,2,8);
//        template.setField(2,1,6);
//        template.setField(3,1,8);
//        template.setField(2,3,2);
//        template.setField(3,3,7);
//        System.out.println("solver() after setfield template\n" + template.toString());
//        System.out.println("solver() after setfield board\n" + board.toString());
        gameState initial = new gameState(template.generateShortcut(), initialValue, false);
        System.out.println(template.generateShortcut());
        queue.add(initial);
    }


    public board solve() {
//        DataInput di = new DataInput();
//        di.ReadBoard("examples/board_6x6.txt");
//        di.ReadBoard("examples/example.txt");

        gameState analyzed = null;
        board analyzedBoard = null;

        tu:
        while (queue.isEmpty() == false) {
            analyzed = queue.remove();
            analyzedBoard = new board(template).generate(analyzed.getBoardShortcut());
            if (analyzed.isSolved()) {
                System.out.println("" + analyzed.isSolved() + " " + analyzed.getBoardShortcut());
                System.out.println(analyzedBoard);
                break tu;
            }

            Vector<gameState> newGeneratedStates = analyzedBoard.nextStep();
            for (int i = 0; i < newGeneratedStates.size(); ++i) {
                queue.add(newGeneratedStates.elementAt(i));
                System.out.println("" + analyzed.isSolved() + " " + newGeneratedStates.elementAt(i).getBoardShortcut());
            }

        }

        return analyzedBoard;
    }
}
