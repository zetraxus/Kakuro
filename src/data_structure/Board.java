package data_structure;


import data_io.PossiblesSumCombinations;

import java.util.*;


public class Board {
    private final int SOLVED = 1;
    private final int POSSIBLETOSOLVE = 0;
    private final int IMPOSSIBLETOSOLVE = -1;
    private final int ASCIZEROCODE = 48;
    private byte width;//this is pointless
    private byte height;//maybe just size?

    private Field2D[][] gameBoard;
    private Vector<FieldInfo> columnsInfo;
    private Vector<FieldInfo> rowsInfo;

    private Set<FieldInfo> changedInfo = new HashSet<>();

    public Board(byte width, byte height, Field2D[][] startBoard) {
        this.width = width;
        this.height = height;
        fillGameBoard(startBoard);

        setPossibilitiesBasedOnTemplate();
        setPossibilitiesBasedOnFilledField();
    }

    public Board(Board oldBoard) {
        width = oldBoard.width;
        height = oldBoard.height;
        fillGameBoard(oldBoard.gameBoard);
    }

    public void setField(int x, int y, int value) {
        gameBoard[x][y].getWritable().setValue(value);
        changedInfo.add(gameBoard[x][y].getWritable().getColumnFieldInfo());
        changedInfo.add(gameBoard[x][y].getWritable().getRowFieldInfo());
        updatePossibilitiesByFilledFields(gameBoard[x][y].getWritable());
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

//        for (FieldInfo fieldInfo: columnsInfo){
//            for (FieldWritable fieldWritable : fieldInfo.getFields()){
//                if(fieldWritable.getState() == FieldWritable.State.FILLED)
//                    shortcut.append(fieldWritable.getValue());
//                else
//                    shortcut.append(0);
//            }
//        }


        return shortcut.toString();
    }

    public boolean isPossibleToSolve() {
        if (changedInfo.size() == 0){
            // check if in all row and column sum it's possible to set at least one combination of sums
            for (FieldInfo row : this.rowsInfo) {
                if (!row.isPossibleToSolve())
                    return false;
            }
            for (FieldInfo column : this.columnsInfo) {
                if (!column.isPossibleToSolve())
                    return false;
            }
        }
        else {
            // check only on changed sums
            for (FieldInfo changed : this.changedInfo){
                if (!changed.isPossibleToSolve())
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
        if(!isPossibleToSolve())
            return IMPOSSIBLETOSOLVE;
        if (!areSumsSolved(columnsInfo) || !areSumsSolved(rowsInfo))
            return POSSIBLETOSOLVE;
        return SOLVED;
    }

    public Vector<GameState> nextStep() {
        Vector<GameState> newStates = new Vector<>();

        StringBuilder shortcut = new StringBuilder(this.generateShortcut());
        int shortcutPosition = 0;
        Board temp;

//        for (FieldInfo fieldInfo: columnsInfo){ // it doesnt work, you can try to fix it + generate shortcut
//            for (FieldWritable fieldWritable : fieldInfo.getFields()){
//                if(fieldWritable.getState() == FieldWritable.State.UNFILLED){
//                    boolean[] possibilities = fieldWritable.getPossibilities();
//                    for(int i = 0 ; i < 9; ++i){
//                        if(possibilities[i]){
//                            int value = 0;//this.getCost() - getCostIfSetField(gameBoard[j][i].getWritable(), k) - 1; // current cost - 1 possibility less in every field in row and column - this field which is set
//                            temp = new Board(this);
//                            temp.columnsInfo.elementAt(columnsInfo.indexOf(fieldInfo)).getFields().elementAt(fieldInfo.getFields().indexOf(fieldWritable)).setValue(i+1);
//                            int checkIfSolved = temp.checkIfSolved();
//                            if (checkIfSolved != IMPOSSIBLETOSOLVE) {
//                                String newShortcut = getChangedShortcut(shortcut, shortcutPosition, i + 1);
//                                GameState newGameState = new GameState(newShortcut, value, checkIfSolved == SOLVED, temp);
//                                newStates.add(newGameState);
//                            }
//                        }
//                    }
//                }
//                ++shortcutPosition;
//            }
//        }

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (gameBoard[j][i].getType() == Field2D.Type.WRITABLE) {
                    if (gameBoard[j][i].getWritable().getState() == FieldWritable.State.UNFILLED) {
                        boolean[] possibilities = gameBoard[j][i].getWritable().getPossibilities();
                        for (int k = 0; k < 9; ++k) {
                            if (possibilities[k] == true) {
                                int value = this.getCost();
                                temp = new Board(this);
                                temp.setField(j, i, k + 1);
                                int checkIfSolved = temp.checkIfSolved();
                                if (checkIfSolved != IMPOSSIBLETOSOLVE) {
                                    String newShortcut = temp.generateShortcut();
                                    GameState newGameState = new GameState(newShortcut, value, checkIfSolved == SOLVED, temp);
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

    //this function is useless in a new order
//    public int getCostIfSetField(FieldWritable fieldWritable, int value) {
//        int cost = 0;
//
//        for (FieldWritable fieldInRow : fieldWritable.getRowFieldInfo().getFields()) {
//            if (fieldInRow != fieldWritable) {
//                if (fieldInRow.getPossibility(value) == true)
//                    ++cost;
//            }
//        }
//
//        for (FieldWritable fieldInColumn : fieldWritable.getColumnFieldInfo().getFields()) {
//            if (fieldInColumn != fieldWritable) {
//                if (fieldInColumn.getPossibility(value) == true)
//                    ++cost;
//            }
//        }
//
//        return cost;
//    }

    private int getCost(){
        int cost = 0;
        int heuristicValue = 0;

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (gameBoard[j][i].getType() == Field2D.Type.WRITABLE) {
                    if (gameBoard[j][i].getWritable().getState() == FieldWritable.State.FILLED) {
//                        cost +=9;
                    }
                    else {
//                        heuristicValue += gameBoard[j][i].getWritable().getPossibilitiesCount();
                    }
                }
            }
        }
        return cost+heuristicValue;
    }

    private void setPossibilitiesBasedOnTemplate() {
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
                if (j.getState() == FieldWritable.State.UNFILLED)
                    j.setPossibilities(possibilities);
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
                }
            }
        }
    }

    private void setPossibilitiesBasedOnFilledField(){
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
            if(fieldInRow.getPossibilitiesCount() == 1){
                fieldInRow.setState(FieldWritable.State.FILLED);
                updatePossibilitiesByFilledFields(fieldInRow);
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
}
