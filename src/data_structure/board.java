package data_structure;


import data_io.DataInput;

import java.util.Arrays;
import java.util.Vector;


public class board {
    final int ASCIZEROCODE = 48;
    private byte width;
    private byte height;

    private field2D[][] gameBoard;
    private Vector<fieldInfo> columnsInfo;
    private Vector<fieldInfo> rowsInfo;

    public board(byte width, byte height, field2D[][] startBoard) {
        this.width = width;
        this.height = height;

        gameBoard = new field2D[width][height];//nie wszedzie zachowalem konwencje [width][height]
        columnsInfo = new Vector<>();
        rowsInfo = new Vector<>();

        for (int i = 0; i < height; ++i)
            for (int j = 0; j < width; ++j) {
                gameBoard[j][i] = startBoard[j][i];

                if (gameBoard[j][i].getType() == field2D.Type.INFOCOLUMN || gameBoard[j][i].getType() == field2D.Type.INFOCOLUMNANDROW)
                    columnsInfo.add(gameBoard[j][i].getColumn());
                if (gameBoard[j][i].getType() == field2D.Type.INFOROW || gameBoard[j][i].getType() == field2D.Type.INFOCOLUMNANDROW)
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
        setPossibilities();
        while (findNewFilled()) {
            setPossibilities();
        }
    }

    public board(board oldBoard) {
        columnsInfo = new Vector<>();
        rowsInfo = new Vector<>();
        for (fieldInfo i : oldBoard.columnsInfo
        ) {
            columnsInfo.add(new fieldInfo(i.getX(), i.getY(), i.getSum(), i.getFieldCount()));

        }
        for (fieldInfo i : oldBoard.rowsInfo
        ) {
            rowsInfo.add(new fieldInfo(i.getX(), i.getY(), i.getSum(), i.getFieldCount()));

        }
        this.width = oldBoard.width;
        this.height = oldBoard.height;
        gameBoard = new field2D[width][height];
        for (int x = 0; x < this.width; ++x) {
            for (int y = 0; y < this.height; ++y) {
                gameBoard[x][y] = new field2D();
                if (oldBoard.gameBoard[x][y].getType() == field2D.Type.WRITABLE) {
                    gameBoard[x][y].setAsWritable();
                    gameBoard[x][y].getWritable().setPossibilities(oldBoard.gameBoard[x][y].getWritable().getPossibilities());
                    gameBoard[x][y].getWritable().setState(oldBoard.gameBoard[x][y].getWritable().getState());//yes, it should be there
                    for (fieldInfo i : columnsInfo) {
                        if (i.getX() == oldBoard.gameBoard[x][y].getWritable().getColumnFieldInfo().getX() && i.getY() == oldBoard.gameBoard[x][y].getWritable().getColumnFieldInfo().getY()) {
                            i.addWritableFields(gameBoard[x][y].getWritable());
                            gameBoard[x][y].getWritable().setColumnFieldInfo(i);
                        }
                    }
                    for (fieldInfo i : rowsInfo) {
                        if (i.getX() == oldBoard.gameBoard[x][y].getWritable().getRowFieldInfo().getX() && i.getY() == oldBoard.gameBoard[x][y].getWritable().getRowFieldInfo().getY()) {
                            i.addWritableFields(gameBoard[x][y].getWritable());
                            gameBoard[x][y].getWritable().setRowFieldInfo(i);
                        }
                    }
                } else {
                    for (fieldInfo i : columnsInfo) {
                        if (i.getX() == x && i.getY() == y) {
                            gameBoard[x][y].setColumn(i);
                        }
                    }
                    for (fieldInfo i : rowsInfo) {
                        if (i.getX() == x && i.getY() == y) {
                            gameBoard[x][y].setRow(i);
                        }
                    }

                }
            }
        }
    }


    public void setField(int x, int y, int value) {
        gameBoard[x][y].getWritable().setValue(value);
        setPossibilities();
        while (findNewFilled()) {
            setPossibilities();
        }

    }

    private boolean findNewFilled() {
        boolean found = false;
        for (fieldInfo i : columnsInfo) {
            for (fieldWritable j : i.getFields()) {
                if (j.getState() == fieldWritable.State.UNFILLED && j.getPossibilitiesCount() == 1) {
                    j.setState(fieldWritable.State.FILLED);
                    found = true;
                }

            }

        }
        return found;
    }

    public board generate(String shortcut) {
        int indexInShortcut = 0;

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (gameBoard[j][i].getType() == field2D.Type.WRITABLE) {
                    if (shortcut.charAt(indexInShortcut) != ASCIZEROCODE)
                        this.setField(j, i, (int) shortcut.charAt(indexInShortcut) - ASCIZEROCODE);
                    ++indexInShortcut;
                }
            }
        }
        //setPossibilities();
        return this;
    }

    public String generateShortcut() {
        String shortcut = "";

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j)
                if (gameBoard[j][i].getType() == field2D.Type.WRITABLE) {
                    if (gameBoard[j][i].getWritable().getState() == fieldWritable.State.FILLED)
                        shortcut += gameBoard[j][i].getWritable().getValue();
                    else
                        shortcut += "0";
                }
        }

        return shortcut;
    }

    private int checkIfSolved() {

        boolean isSolved = true;
        boolean isPossibleToSolve = true;
        for (int x = 0; x < height; ++x) {
            for (int y = 0; y < width; ++y) {
                if (gameBoard[y][x].getType() == field2D.Type.WRITABLE) {
                    if (gameBoard[y][x].getWritable().getState() == fieldWritable.State.UNFILLED) {
                        isSolved = false;
                        if (gameBoard[y][x].getWritable().getPossibilitiesCount() == 0)
                            isPossibleToSolve = false; // it's impossible to solve Kakuro in this board situation
                    }
                }
            }
        }
        if (!isPossibleToSolve)
            return -1;
        if (isSolved) {
            for (fieldInfo i : columnsInfo
            ) {
                int sum = 0;
                for (fieldWritable j : i.getFields()
                ) {
                    sum += j.getValue();

                }
                if (sum != i.getSum()) {
                    return -1;

                }

            }
            for (fieldInfo i : rowsInfo
            ) {
                int sum = 0;
                for (fieldWritable j : i.getFields()
                ) {
                    sum += j.getValue();

                }
                if (sum != i.getSum()) return -1;
            }
            return 1;
        }
        return 0;
    }

    public Vector<gameState> nextStep() { // I haven't tested this function yet, so use it carefully, or test it.
        Vector<gameState> newStates = new Vector<>();

        String currentShortcut = this.generateShortcut();
        int shortcutPosition = 0;

        boolean isPossibleToSolve = true;
        for (int x = 0; x < height; ++x) {
            for (int y = 0; y < width; ++y) {
                if (gameBoard[y][x].getType() == field2D.Type.WRITABLE) {
                    if (gameBoard[y][x].getWritable().getState() == fieldWritable.State.UNFILLED) {
                        if (gameBoard[y][x].getWritable().getPossibilitiesCount() == 0)
                            isPossibleToSolve = false; // it's impossible to solve Kakuro in this board situation
                    }
                }
            }
        }
        if (isPossibleToSolve == false)
            return newStates;//to sie nigdy nie powinno stac moim zdaniem, Łukasz

        board temp;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (gameBoard[j][i].getType() == field2D.Type.WRITABLE) {
                    if (gameBoard[j][i].getWritable().getState() == fieldWritable.State.UNFILLED) {
                        boolean[] possibilities = gameBoard[j][i].getWritable().getPossibilities();
                        for (int k = 0; k < 9; ++k) {
                            if (possibilities[k] == true) {
                                String newShortcut = currentShortcut.substring(0, shortcutPosition) + (k + 1) + currentShortcut.substring(shortcutPosition + 1);
                                int value = 0;//this.getCost() - getCostIfSetField(gameBoard[j][i].getWritable(), k) - 1; // current cost - 1 possibility less in every field in row and column - this field which is set

                                temp = new board(this);
                                temp.setField(j, i, k + 1);
                                if (temp.checkIfSolved() >= 0) {
                                    gameState newGameState = new gameState(newShortcut, value, temp.checkIfSolved() > 0);

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

    public int getCostIfSetField(fieldWritable fieldWritable, int value) {
        int cost = 0;

        for (fieldWritable fieldInRow : fieldWritable.getRowFieldInfo().getFields()) {
            if (fieldInRow != fieldWritable) {
                if (fieldInRow.getPossibility(value) == true)
                    ++cost;
            }
        }

        for (fieldWritable fieldInColumn : fieldWritable.getColumnFieldInfo().getFields()) {
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
                if (gameBoard[j][i].getType() == field2D.Type.WRITABLE) {
                    if (gameBoard[j][i].getWritable().getState() == fieldWritable.State.UNFILLED) {
                        cost += gameBoard[j][i].getWritable().getPossibilitiesCount();
                    }
                }
            }
        }
        return cost;
    }

    private void setPossibilities() {
        boolean[] possibilitiesRows;
        boolean[] possibilities;
        boolean[] outTable;

        DataInput di = new DataInput();

        for (fieldInfo i : columnsInfo) {
            possibilities = di.getPossibilities(i.getSum(), i.getFieldCount());
            //TODO in future:
//            6. Określenie możliwości dla zajętej częściowo tabeli.
//            a. Dla każdej kolumny(obiektu kolumna, nie zaś całej kolumny)
//            określamy możliwości na podstawie sumy, ilości komórek i ich zajęcia np. dla 5in2 ze stojącą 2 w jednej z komórek : {(1,4),(2,3)}
//            a więc w każdej komórce ustawiamy możliwości 2,3

            for (fieldWritable j : i.getFields()) {
                if (j.getState() != fieldWritable.State.FILLED)
                    j.setPossibilities(possibilities);
            }
        }

        for (fieldInfo i : rowsInfo) {
            possibilities = di.getPossibilities(i.getSum(), i.getFieldCount());
            for (fieldWritable j : i.getFields()) {
                if (j.getState() != fieldWritable.State.FILLED) {
                    outTable = new boolean[9];
                    possibilitiesRows = j.getPossibilities();

                    for (int k = 0; k < 9; ++k)
                        outTable[k] = possibilities[k] & possibilitiesRows[k];

                    j.setPossibilities(outTable);
                }
            }
        }

        //this should consider filled fields influence
//        and should not fill fields
        for (fieldInfo i : columnsInfo) {
            for (fieldWritable j : i.getFields()) {
                if (j.getState() == fieldWritable.State.UNFILLED) {
                    updatePossibilitiesByFilledFields(i.getX(), j.getRowFieldInfo().getY());
                }
            }
        }
    }

    private void updatePossibilitiesByFilledFields(int x, int y) {
        for (fieldWritable i : gameBoard[x][y].getWritable().getColumnFieldInfo().getFields()) {
            if (i != gameBoard[x][y].getWritable() && i.getState() == fieldWritable.State.FILLED) {
                gameBoard[x][y].getWritable().revokePossibility(i.getValue());
            }
        }
        for (fieldWritable i : gameBoard[x][y].getWritable().getRowFieldInfo().getFields()) {
            if (i != gameBoard[x][y].getWritable() && i.getState() == fieldWritable.State.FILLED) {
                gameBoard[x][y].getWritable().revokePossibility(i.getValue());
            }
        }
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
