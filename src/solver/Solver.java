package solver;


import data_structure.Board;
import data_structure.GameState;

import java.util.*;

public class Solver {

    Comparator<GameState> comparator = new Comparator<GameState>() {
        @Override
        public int compare(GameState o1, GameState o2) {
            return o1.getHeuristicValue() - o2.getHeuristicValue();
        }
    };

    private PriorityQueue<GameState> queue = new PriorityQueue<>(comparator);
    private Vector<GameState> analysedVector = new Vector<>(); // for randomSolve
    private Board template;
    private Integer analysedCount;
    HashSet<String> mapsAddedToQueue;

    public Solver(Board board, int initialValue) {
        template = new Board(board);
        GameState initial = new GameState(template.generateShortcut(), initialValue, false, template);
        queue.add(initial);
        mapsAddedToQueue = new HashSet<>();
        mapsAddedToQueue.add(template.generateShortcut());

        analysedVector.add(initial); // for randomSolve;
    }

    public Integer getAnalysedCount() {
        return analysedCount;
    }

    public Board solve() {
        GameState analyzed;
        Board analyzedBoard = null;
        int countOfAnalyzed = 0;

        while (!queue.isEmpty()) {
            ++countOfAnalyzed;
            analyzed = queue.remove();
            analyzedBoard = analyzed.getBoard();
            if (analyzed.isSolved()) {
                analysedCount = countOfAnalyzed;
                break;
            }

            Vector<GameState> newGeneratedStates = analyzedBoard.nextStep();
            for (GameState gameState : newGeneratedStates) {
                if (!mapsAddedToQueue.contains(gameState.getBoardShortcut())) {
                    queue.add(gameState);
                    mapsAddedToQueue.add(gameState.getBoardShortcut());
                }
            }
        }

        return analyzedBoard;
    }

    public Board radndomSolve() {
        GameState analyzed;
        Board analyzedBoard = null;
        int countOfAnalyzed = 0;

        while (!analysedVector.isEmpty()) {
            Random rand = new Random();
            int nextBoard = rand.nextInt(analysedVector.size());
            analyzed = analysedVector.elementAt(nextBoard);
            analyzedBoard = analyzed.getBoard();
            Collections.swap(analysedVector, nextBoard, (analysedVector.size()-1));
            analysedVector.remove(analysedVector.size()-1);

            ++countOfAnalyzed;
            if (analyzed.isSolved()) {
                analysedCount = countOfAnalyzed;
                break;
            }

            Vector<GameState> newGeneratedStates = analyzedBoard.nextStep();
            for (GameState gameState : newGeneratedStates) {
                if (!mapsAddedToQueue.contains(gameState.getBoardShortcut())) {
                    analysedVector.add(gameState);
                    mapsAddedToQueue.add(gameState.getBoardShortcut());
                }
            }
        }

        return analyzedBoard;
    }
}
