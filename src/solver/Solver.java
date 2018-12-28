package solver;


import data_structure.Board;
import data_structure.GameState;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

public class Solver {

    Comparator<GameState> comparator = new Comparator<GameState>() {
        @Override
        public int compare(GameState o1, GameState o2) {
            return o1.getHeuristicValue() - o2.getHeuristicValue();
        }
    };

    private PriorityQueue<GameState> queue = new PriorityQueue<>(comparator);
    private Board template;

    public Solver(Board board, int initialValue) {
        System.out.println("Solver() board\n" + board.toString());
        template = new Board(board);
        System.out.println("Solver() template\n" + template.toString());
        System.out.println("Solver() after setfield template\n" + template.toString());
        System.out.println("Solver() after setfield board\n" + board.toString());
        GameState initial = new GameState(template.generateShortcut(), initialValue, false);
        System.out.println(template.generateShortcut());
        queue.add(initial);
    }


    public Board solve() {
        GameState analyzed;
        Board analyzedBoard = null;
        int countOfAnalyzed = 0;

        while (queue.isEmpty() == false) {
            ++countOfAnalyzed;
            analyzed = queue.remove();
            analyzedBoard = new Board(template, analyzed.getBoardShortcut());

            if (!analyzedBoard.isPossibleToSolve()) {
                System.out.println("FALSE BOARD " + countOfAnalyzed);
                continue;
            }
            if (analyzed.isSolved()) {
                System.out.println("" + analyzed.isSolved() + " " + analyzed.getBoardShortcut());
                System.out.println(analyzedBoard);
                break;
            }

            Vector<GameState> newGeneratedStates = analyzedBoard.nextStep();
            for (int i = 0; i < newGeneratedStates.size(); ++i) {
                queue.add(newGeneratedStates.elementAt(i));
                System.out.println("" + analyzed.isSolved() + " " + newGeneratedStates.elementAt(i).getBoardShortcut());
            }
        }
        System.out.println("Analyzed board: " + countOfAnalyzed);

        return analyzedBoard;
    }
}