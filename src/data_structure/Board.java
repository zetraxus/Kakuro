package data_structure;


import data_io.PossiblesSumCombinations;

import java.util.Arrays;
import java.util.Vector;


public class Board {
    final int ASCIZEROCODE = 48;
    private byte width;
    private byte height;

    private Field2D[][] gameBoard;
    private Vector<FieldInfo> columnsInfo;
    private Vector<FieldInfo> rowsInfo;

    public Board(byte width, byte height, Field2D[][] startBoard) {
        this.width = width;
        this.height = height;
        fillGameBoard(startBoard);

        setPossibilities();
        while (findNewFilled()) {
            setPossibilities();
        }
    }

    public Board(Board oldBoard) {
        width = oldBoard.width;
        height = oldBoard.height;
        fillGameBoard(oldBoard.gameBoard);
    }

    public Board(Board oldBoard, String shortcut) {
        width = oldBoard.width;
        height = oldBoard.height;
        fillGameBoard(oldBoard.gameBoard);
        fillValuesFromShortcut(shortcut);
    }

    public void setField(int x, int y, int value) {
        gameBoard[x][y].getWritable().setValue(value);
        setPossibilities();
        while (findNewFilled()) {
            setPossibilities();
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

    private boolean findNewFilled() {
        boolean found = false;
        for (FieldInfo column : columnsInfo) {
            for (FieldWritable writable : column.getFields()) {
                if (writable.getState() == FieldWritable.State.UNFILLED && writable.getPossibilitiesCount() == 1) {
                    writable.setState(FieldWritable.State.FILLED);
                    found = true;
                }
            }
        }
        return found;
    }

    private void fillValuesFromShortcut(String shortcut) {
        int indexInShortcut = 0;

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (gameBoard[j][i].getType() == Field2D.Type.WRITABLE) {
                    if (shortcut.charAt(indexInShortcut) != ASCIZEROCODE)
                        this.setField(j, i, (int) shortcut.charAt(indexInShortcut) - ASCIZEROCODE);
                    ++indexInShortcut;
                }
            }
        }
        //setPossibilities(); // TODO: is this needed or not?
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
        // check if in all row and column sum it's possible to set at least one combination of sums
        for (FieldInfo row : this.rowsInfo) {
            if (!row.isPossibleToSolve())
                return false;
        }
        for (FieldInfo column : this.columnsInfo) {
            if (!column.isPossibleToSolve())
                return false;
        }
        return true;
    }

    private int checkIfSolved() {
        int solvingStatus = isPossibleOrSolved();

        if (solvingStatus < 0)
            return -1;
        if (solvingStatus == 1) {
            if (!areSumsSolved(columnsInfo) || !areSumsSolved(rowsInfo))
                return -1;
            return 1;
        }
        return 0;
    }

    private int isPossibleOrSolved() {
        boolean isSolved = true;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (gameBoard[x][y].getType() == Field2D.Type.WRITABLE) {
                    if (gameBoard[x][y].getWritable().getState() == FieldWritable.State.UNFILLED) {
                        isSolved = false;
                        if (gameBoard[x][y].getWritable().getPossibilitiesCount() == 0)
                            return -1; // it's impossible to solve Kakuro in this board situation
                    }
                }
            }
        }
        if (isSolved)
            return 1;
        return 0;
    }

    private boolean areSumsSolved(Vector<FieldInfo> infoFields) {
        for (FieldInfo info : infoFields) {
            int sum = info.getFields().stream().mapToInt(e -> e.getValue()).sum();
            if (sum != info.getSum())
                return false;
        }
        return true;
    }

    public Vector<GameState> nextStep() { // I haven't tested this function yet, so use it carefully, or test it.
        Vector<GameState> newStates = new Vector<>();

        StringBuilder shortcut = new StringBuilder(this.generateShortcut());
        int shortcutPosition = 0;

        int solvingStatus = isPossibleOrSolved();
        if (solvingStatus < 0)
            return newStates;//to sie nigdy nie powinno stac moim zdaniem, Łukasz

        Board temp;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (gameBoard[j][i].getType() == Field2D.Type.WRITABLE) {
                    if (gameBoard[j][i].getWritable().getState() == FieldWritable.State.UNFILLED) {
                        boolean[] possibilities = gameBoard[j][i].getWritable().getPossibilities();
                        for (int k = 0; k < 9; ++k) {
                            if (possibilities[k] == true) {
                                int value = 0;//this.getCost() - getCostIfSetField(gameBoard[j][i].getWritable(), k) - 1; // current cost - 1 possibility less in every field in row and column - this field which is set
                                temp = new Board(this);
                                temp.setField(j, i, k + 1);

                                if (temp.checkIfSolved() >= 0) {
                                    String newShortcut = getChangedShortcut(shortcut, shortcutPosition, k + 1);
                                    GameState newGameState = new GameState(newShortcut, value, temp.checkIfSolved() > 0);
                                    newStates.add(newGameState);
                                }
                            }
                        }
                    }
                    ++shortcutPosition;
                }
            }
        }

        return newStates;
    }

    private String getChangedShortcut(StringBuilder originalShortcut, int shortcutPosition, int newValue) {
        char current = originalShortcut.charAt(shortcutPosition);
        originalShortcut.setCharAt(shortcutPosition, Character.forDigit(newValue, 10));
        String newShortcut = originalShortcut.toString();
        originalShortcut.setCharAt(shortcutPosition, current);
        return newShortcut;
    }

    public int getCostIfSetField(FieldWritable fieldWritable, int value) {
        int cost = 0;

        for (FieldWritable fieldInRow : fieldWritable.getRowFieldInfo().getFields()) {
            if (fieldInRow != fieldWritable) {
                if (fieldInRow.getPossibility(value) == true)
                    ++cost;
            }
        }

        for (FieldWritable fieldInColumn : fieldWritable.getColumnFieldInfo().getFields()) {
            if (fieldInColumn != fieldWritable) {
                if (fieldInColumn.getPossibility(value) == true)
                    ++cost;
            }
        }

        return cost;
    }

    public int getCost() {
        int cost = 0;

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (gameBoard[j][i].getType() == Field2D.Type.WRITABLE) {
                    if (gameBoard[j][i].getWritable().getState() == FieldWritable.State.UNFILLED) {
                        cost += gameBoard[j][i].getWritable().getPossibilitiesCount();
                    }
                }
            }
        }
        return cost;
    }

    private void setPossibilities() {
        boolean[] possibilitiesRow;
        boolean[] possibilities;
        boolean[] crossPossibilities = new boolean[9];

        for (FieldInfo column : columnsInfo) {
//            TODO in future:
//            6. Określenie możliwości dla zajętej częściowo tabeli.
//            a. Dla każdej kolumny(obiektu kolumna, nie zaś całej kolumny)
//            określamy możliwości na podstawie sumy, ilości komórek i ich zajęcia np. dla 5in2 ze stojącą 2 w jednej z komórek : {(1,4),(2,3)}
//            a więc w każdej komórce ustawiamy możliwości 2,3

            possibilities = PossiblesSumCombinations.getPossibilities(column.getSum(), column.getFieldCount());
            for (FieldWritable j : column.getFields()) {
                if (j.getState() != FieldWritable.State.FILLED)
                    j.setPossibilities(possibilities);
            }
        }

        for (FieldInfo row : rowsInfo) {
            possibilities = PossiblesSumCombinations.getPossibilities(row.getSum(), row.getFieldCount());
            for (FieldWritable writable : row.getFields()) {
                if (writable.getState() != FieldWritable.State.FILLED) {
                    possibilitiesRow = writable.getPossibilities();
                    for (int k = 0; k < 9; ++k)
                        crossPossibilities[k] = possibilities[k] & possibilitiesRow[k];
                    writable.setPossibilities(crossPossibilities);
                }
            }
        }

//      this should consider filled fields influence
//      and should not fill fields
        for (FieldInfo column : columnsInfo) {
            for (FieldWritable writable : column.getFields()) {
                if (writable.getState() == FieldWritable.State.UNFILLED) {
                    updatePossibilitiesByFilledFields(column.getX(), writable.getRowFieldInfo().getY());
                }
            }
        }
    }

    private void updatePossibilitiesByFilledFields(int x, int y) {
        for (FieldWritable fieldInColumn : gameBoard[x][y].getWritable().getColumnFieldInfo().getFields()) {
            if (fieldInColumn != gameBoard[x][y].getWritable() && fieldInColumn.getState() == FieldWritable.State.FILLED) {
                gameBoard[x][y].getWritable().revokePossibility(fieldInColumn.getValue());
            }
        }
        for (FieldWritable fieldInRow : gameBoard[x][y].getWritable().getRowFieldInfo().getFields()) {
            if (fieldInRow != gameBoard[x][y].getWritable() && fieldInRow.getState() == FieldWritable.State.FILLED) {
                gameBoard[x][y].getWritable().revokePossibility(fieldInRow.getValue());
            }
        }
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

    /*private void updatePossibilities(int x, int y, int value) {
        if(1>0)return;
        for (int i = 0; i != gameBoard[x][y].getWritable().getColumnFieldInfo().getFields().size(); i++) {
            int setNewFieldValue = -1;
            if (gameBoard[x][y].getWritable().getColumnFieldInfo().getFields().elementAt(i) != gameBoard[x][y].getWritable()) {
                setNewFieldValue = gameBoard[x][y].getWritable().getColumnFieldInfo().getFields().elementAt(i).revokePossibility(value);
                if (setNewFieldValue != -1)
                    ;//setField(x, y, setNewFieldValue);
            }
        }
        for (int i = 0; i != gameBoard[x][y].getWritable().getRowFieldInfo().getFields().size(); i++) {
            int setNewFieldValue = -1;
            if (gameBoard[x][y].getWritable().getRowFieldInfo().getFields().elementAt(i) != gameBoard[x][y].getWritable()) {
                setNewFieldValue = gameBoard[x][y].getWritable().getRowFieldInfo().getFields().elementAt(i).revokePossibility(value);
                if (setNewFieldValue != -1)
                    ;//setField(x, y, setNewFieldValue);
            }
        }

    }*/
}
