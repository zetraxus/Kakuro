package data_structure;


import data_io.DataInput;

import java.util.Arrays;
import java.util.Vector;


public class board {
    private byte width;
    private byte height;

    private field2D[][] gameBoard;
    private Vector<fieldInfo> columnsInfo;
    private Vector<fieldInfo> rowsInfo;

    public board(byte width, byte height, field2D[][] startBoard) {
        this.width = width;
        this.height = height;

        gameBoard = new field2D[width][height];
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
    }

    public board(board board) {//TODO
    }

    public field2D getField(int x, int y) {
        return gameBoard[x][y];
    }

    public byte getWidth() {
        return width;
    }

    public byte getHeight() {
        return height;
    }

    public void setField(int x, int y, int value) {
        gameBoard[x][y].getWritable().setValue(value);
        updatePossibilities(x, y, value);
    }

    private void setPossibilities() {
        //TODO update after it
        boolean[] possibilitiesRows;
        boolean[] possibilities;
        boolean[] outTable;

        DataInput di = new DataInput();

        for (fieldInfo i : columnsInfo) {
            possibilities = di.getPossibilities(i.getSum(), i.getFieldCount());
            for (fieldWritable j : i.getFields()) {
                j.setPossibilities(possibilities);
            }
        }

        for (fieldInfo i : rowsInfo) {
            possibilities = di.getPossibilities(i.getSum(), i.getFieldCount());
            for (fieldWritable j : i.getFields()) {
                outTable = new boolean[9];
                possibilitiesRows = j.getPossibilities();

                for (int k = 0; k < 9; ++k)
                    outTable[k] = possibilities[k] & possibilitiesRows[k];

                j.setPossibilities(outTable);
            }
        }

        //this should consider filled fields influence
        //and should not fill fields
        for (fieldInfo i : columnsInfo) {
            for (fieldWritable j : i.getFields()) {
                if (j.getState() == fieldWritable.State.UNFILLED) {
                    updatePossibilitiesByFilledFields(i.getX(), j.getRowFieldInfo().getY());
                }
            }
        }
//        for (fieldInfo i : columnsInfo) {
//            for (fieldWritable j : i.getFields()) {
//                if(j.getState()== fieldWritable.State.UNFILLED){
//                    updatePossibilities(i.getX(),j.getRowFieldInfo().getY(),j.getValue());
//                }
//            }
//        }

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

    private void updatePossibilities(int x, int y, int value) {
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

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        StringBuilder[] rowBuilders = new StringBuilder[this.height];
        // Initialize all elements in this array
        Arrays.setAll(rowBuilders, i -> new StringBuilder());

        for (int i = 0; i < this.width; ++i){
            for (int j = 0; j < this.height; ++j){
                rowBuilders[j].append(this.gameBoard[i][j]);
                rowBuilders[j].append(" ");
            }
        }
        for (StringBuilder row : rowBuilders){
            builder.append(row.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
