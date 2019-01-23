package data_structure;


import data_io.PossiblesSumCombinations;
import javafx.util.Pair;

import java.util.*;


public class Board {
    private final int SOLVED = 1;
    private final int POSSIBLETOSOLVE = 0;
    private final int IMPOSSIBLETOSOLVE = -1;
    private final int ASCIZEROCODE = 48;
    private byte width;
    private byte height;
    private History history;
    private int NumberOfPossiblilities;
    private int cost;

    private Field2D[][] gameBoard;
    private Vector<FieldInfo> columnsInfo;
    private Vector<FieldInfo> rowsInfo;

    private Set<FieldInfo> changedInfo = new HashSet<>();

    public Board(byte width, byte height, Field2D[][] startBoard) {
        NumberOfPossiblilities = 0;
        this.width = width;
        this.height = height;
        fillGameBoard(startBoard);
        history = new History(width, height);
        cost = 0;

        setPossibilitiesBasedOnTemplate();
        setPossibilitiesBasedOnFilledField();
        for (FieldInfo j : columnsInfo) {
            for (FieldWritable i : j.getFields()) {
                if (i.getState() == FieldWritable.State.UNFILLED)
                    NumberOfPossiblilities += i.getPossibilitiesCount();
            }
        }
        history.addBreak();
    }

    public Board(Board oldBoard) {
        width = oldBoard.width;
        height = oldBoard.height;
        fillGameBoard(oldBoard.gameBoard);
        history = new History(width, height, oldBoard.history.getHistory(), oldBoard.history.getPointInHistory());
        NumberOfPossiblilities = oldBoard.NumberOfPossiblilities;
        cost = oldBoard.cost;
    }

    public void setField(int x, int y, int value, boolean updateOther) {
        if (updateOther) {
            gameBoard[x][y].getWritable().setValue(value);
            changedInfo.add(gameBoard[x][y].getWritable().getColumnFieldInfo());
            changedInfo.add(gameBoard[x][y].getWritable().getRowFieldInfo());
            history.add(x, y);
            updatePossibilitiesByFilledFields(gameBoard[x][y].getWritable());
            history.addBreak();
        } else {
            gameBoard[x][y].getWritable().setValue(value);
        }
    }

    private void fillGameBoard(Field2D[][] initialBoard) {
        gameBoard = new Field2D[width][height];
        columnsInfo = new Vector<>();
        rowsInfo = new Vector<>();

        for (int i = 0; i < height; ++i)
            for (int j = 0; j < width; ++j) {
                gameBoard[j][i] = new Field2D(initialBoard[j][i]);

                if (gameBoard[j][i].getType() == Field2D.Type.INFOCOLUMN || gameBoard[j][i].getType() == Field2D.Type.INFOCOLUMNANDROW)
                    columnsInfo.add(gameBoard[j][i].getColumn());
                if (gameBoard[j][i].getType() == Field2D.Type.INFOROW || gameBoard[j][i].getType() == Field2D.Type.INFOCOLUMNANDROW)
                    rowsInfo.add(gameBoard[j][i].getRow());
            }

        for (int i = 0; i < columnsInfo.size(); ++i) {
            byte x = columnsInfo.elementAt(i).getX();
            byte y = (byte) (columnsInfo.elementAt(i).getY() + 1);

            for (int j = 0; j < columnsInfo.elementAt(i).getFieldCount(); ++j, ++y) {
                gameBoard[x][y].getWritable().setColumnFieldInfo(columnsInfo.elementAt(i));
                columnsInfo.elementAt(i).addWritableFields(gameBoard[x][y].getWritable());
            }
        }

        for (int i = 0; i < rowsInfo.size(); ++i) {
            byte x = (byte) (rowsInfo.elementAt(i).getX() + 1);
            byte y = rowsInfo.elementAt(i).getY();

            for (int j = 0; j < rowsInfo.elementAt(i).getFieldCount(); ++j, ++x) {
                gameBoard[x][y].getWritable().setRowFieldInfo(rowsInfo.elementAt(i));
                rowsInfo.elementAt(i).addWritableFields(gameBoard[x][y].getWritable());
            }
        }
    }

    public String generateShortcut() {
        StringBuilder shortcut = new StringBuilder();

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j)
                if (gameBoard[j][i].getType() == Field2D.Type.WRITABLE) {
                    if (gameBoard[j][i].getWritable().getState() == FieldWritable.State.FILLED)
                        shortcut.append(gameBoard[j][i].getWritable().getValue());
                    else
                        shortcut.append(0);
                }
        }

        return shortcut.toString();
    }

    public boolean isPossibleToSolve() {
        if (changedInfo.size() == 0) {
            // check if in all row and column sum it's possible to set at least one combination of sums
            for (FieldInfo row : this.rowsInfo) {
                if (!row.isPossibleToSolve(history))
                    return false;
            }
            for (FieldInfo column : this.columnsInfo) {
                if (!column.isPossibleToSolve(history))
                    return false;
            }
        } else {
            // check only on changed sums
            for (FieldInfo changed : this.changedInfo) {
                if (!changed.isPossibleToSolve(history))
                    return false;
            }
        }
        return true;
    }

    private boolean areSumsSolved(Vector<FieldInfo> infoFields) {
        for (FieldInfo info : infoFields) {
            int sum = info.getFields().stream().mapToInt(e -> e.getValue()).sum();
            if (sum != info.getSum())
                return false;
        }
        return true;
    }

    private int checkIfSolved() {
        if (!isPossibleToSolve())
            return IMPOSSIBLETOSOLVE;
        if (!areSumsSolved(columnsInfo) || !areSumsSolved(rowsInfo))
            return POSSIBLETOSOLVE;
        return SOLVED;
    }

    public Vector<GameState> nextStep() {
        Vector<GameState> newStates = new Vector<>();

        StringBuilder shortcut = new StringBuilder(this.generateShortcut());
        Board temp;

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (gameBoard[j][i].getType() == Field2D.Type.WRITABLE) {
                    if (gameBoard[j][i].getWritable().getState() == FieldWritable.State.UNFILLED) {
                        boolean[] possibilities = gameBoard[j][i].getWritable().getPossibilities();
                        for (int k = 0; k < 9; ++k) {
                            if (possibilities[k] == true) {
                                temp = new Board(this);
                                temp.setField(j, i, k + 1, true);
                                int checkIfSolved = temp.checkIfSolved();
                                if (checkIfSolved != IMPOSSIBLETOSOLVE) {
                                    String newShortcut = temp.generateShortcut();
                                    int value = this.getCost();
                                    GameState newGameState = new GameState(newShortcut, value, checkIfSolved == SOLVED, temp);
                                    newStates.add(newGameState);
                                }
                            }
                        }
                    }
                }
            }
        }

        return newStates;
    }

    private int getCost() {
        int heuristicValue = 0;
        int newNumberOfPossiblilities = 0;
        for (FieldInfo j : columnsInfo) {
            for (FieldWritable i : j.getFields()) {
                if (i.getState() == FieldWritable.State.UNFILLED){
                    newNumberOfPossiblilities += i.getPossibilitiesCount();
                    ++heuristicValue;
                }
            }
        }

        cost += Math.pow((NumberOfPossiblilities - newNumberOfPossiblilities), 6);
        NumberOfPossiblilities = newNumberOfPossiblilities;
        return cost + heuristicValue;
    }

    private void setPossibilitiesBasedOnTemplate() {
        boolean[] possibilitiesRow;
        boolean[] possibilities;
        boolean[] crossPossibilities = new boolean[9];

        for (FieldInfo column : columnsInfo) {
            possibilities = PossiblesSumCombinations.getPossibilities(column.getSum(), column.getFieldCount());
            for (FieldWritable j : column.getFields()) {
                if (j.getState() == FieldWritable.State.UNFILLED) {
                    j.setPossibilities(possibilities);
                    if (j.getState() == FieldWritable.State.FILLED) {
                        history.add(j.getColumnFieldInfo().getX(), j.getRowFieldInfo().getY());
                    }
                }
            }
        }

        for (FieldInfo row : rowsInfo) {
            possibilities = PossiblesSumCombinations.getPossibilities(row.getSum(), row.getFieldCount());
            for (FieldWritable writable : row.getFields()) {
                if (writable.getState() == FieldWritable.State.UNFILLED) {
                    possibilitiesRow = writable.getPossibilities();
                    for (int k = 0; k < 9; ++k)
                        crossPossibilities[k] = possibilities[k] & possibilitiesRow[k];
                    writable.setPossibilities(crossPossibilities);
                    if (writable.getState() == FieldWritable.State.FILLED) {
                        history.add(writable.getColumnFieldInfo().getX(), writable.getRowFieldInfo().getY());
                    }
                }
            }
        }
    }

    private void setPossibilitiesBasedOnFilledField() {
        for (FieldInfo column : columnsInfo) {
            for (FieldWritable writable : column.getFields()) {
                if (writable.getState() == FieldWritable.State.FILLED) {
                    updatePossibilitiesByFilledFields(writable);
                }
            }
        }
    }

    private void updatePossibilitiesByFilledFields(FieldWritable writable) {
        for (FieldWritable fieldInColumn : writable.getColumnFieldInfo().getFields()) {
            updatePossibilitiesInField(writable, fieldInColumn);
        }
        for (FieldWritable fieldInRow : writable.getRowFieldInfo().getFields()) {
            updatePossibilitiesInField(writable, fieldInRow);
        }
    }

    private void updatePossibilitiesInField(FieldWritable writable, FieldWritable fieldInRow) {
        if (fieldInRow != writable && fieldInRow.getState() == FieldWritable.State.UNFILLED) {
            fieldInRow.revokePossibility(writable.getValue());
            changedInfo.add(fieldInRow.getRowFieldInfo());
            changedInfo.add(fieldInRow.getColumnFieldInfo());
            if (fieldInRow.getPossibilitiesCount() == 1) {
                fieldInRow.setState(FieldWritable.State.FILLED);
                history.add(fieldInRow.getColumnFieldInfo().getX(), fieldInRow.getRowFieldInfo().getY());
                updatePossibilitiesByFilledFields(fieldInRow);
            }
        }
    }

    public String getHistory() {
        StringBuilder builder = new StringBuilder();
        builder.append("Historia\ndługość historii: ").append(history.getPointInHistory() / 2).append("\n");
        System.out.println(history.getPointInHistory() / 2);
        for (int i = 0; i != history.getPointInHistory() / 2; ++i) {
//            builder.append(history[i]).append(" ").append(history[i+1]).append("\n");
            Pair<Integer, Integer> pair = history.getValue(i);
            builder.append("x: ").append(pair.getKey()).append(" y: ").append(pair.getValue());
            if (pair.getKey() != -1)
                builder.append(" wartość: ").append(gameBoard[pair.getKey()][pair.getValue()].getWritable().getValue()).append("\n");
            else
                builder.append("\n");
        }
        return builder.toString();
    }

    public int[] getHistoryAsArray() {
        return history.getHistory();
    }

    public byte getHeight() {
        return height;
    }

    public byte getWidth() {
        return width;
    }

    public Field2D[][] getGameBoard() {
        return gameBoard;
    }

    public boolean isSolved() {
        if (checkIfSolved() == SOLVED)
            return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        StringBuilder[] rowBuilders = new StringBuilder[this.height];
        // Initialize all elements in this array
        Arrays.setAll(rowBuilders, i -> new StringBuilder());

        for (int i = 0; i < this.width; ++i) {
            for (int j = 0; j < this.height; ++j) {
                rowBuilders[j].append(this.gameBoard[i][j]);
                rowBuilders[j].append(" ");
            }
        }
        for (StringBuilder row : rowBuilders) {
            builder.append(row.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
