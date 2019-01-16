package data_structure;

import javafx.util.Pair;

public class History {
    private int[] history;
    private int pointInHistory;

    History(int width, int height) {
        history = new int[2 * (width - 1) * (height - 1)];
        pointInHistory = 0;

    }

    History(int width, int height, int[] oldHistory, int oldPointInHistory) {
        history = new int[2 * (width - 1) * (height - 1)];
        history=oldHistory.clone();
        pointInHistory = oldPointInHistory;

    }

    void add(int x, int y) {
        history[pointInHistory] = x;
        history[pointInHistory + 1] = y;
        pointInHistory += 2;
    }

    public int[] getHistory() {
        return history;
    }

    public int getPointInHistory() {
        return pointInHistory;
    }

    public Pair<Integer,Integer> getValue(int i){
        return new Pair<>(history[2*i],history[2*i+1]);
    }

}
