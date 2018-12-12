package data_structure;

import java.lang.reflect.Array;
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
