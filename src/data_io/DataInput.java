package data_io;


import data_structure.Board;
import data_structure.Field2D;
import data_structure.FieldInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class DataInput {
    final String rowSign = "R";
    private Field2D[][] board;
    private int width;
    private int height;

    public void ReadBoard(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader((filename)));

            String line = br.readLine();
            this.prepareBoard(line);

            while ((line = br.readLine()) != null) {
                this.analyzeRequirement(line);
            }
            br.close();
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
    }

    public void readString(String input) {
        String[] lines = input.split("\n");
        this.prepareBoard(lines[0]);
        lines = Arrays.copyOfRange(lines, 1, lines.length);
        for (String line : lines) {
            this.analyzeRequirement(line);
        }
    }

    public Board makeGameBoard() {
        if (board == null)
            return null;
        return new Board((byte) this.width, (byte) this.height, this.board);
    }

    private void prepareBoard(String sizeLine) {
        Scanner scanner = new Scanner(sizeLine);
        this.width = scanner.nextInt();
        this.height = scanner.nextInt();

        this.board = new Field2D[this.width][this.height]; // default constructor set all field to blank
        for (int x = 0; x < this.width; ++x)
            for (int y = 0; y < this.height; ++y)
                this.board[x][y] = new Field2D();
    }

    private void analyzeRequirement(String line) {
        if (line.length() < 1)
            return;
        Scanner scanner = new Scanner(line);

        String type = scanner.next();
        byte x = scanner.nextByte();
        byte y = scanner.nextByte();
        byte sum = scanner.nextByte();
        byte length = scanner.nextByte();

        FieldInfo info = new FieldInfo(x, y, sum, length);

        if (type.equals(rowSign)) { // row
            this.board[x][y].setRow(info);
            for (int i = 1; i <= length; ++i) {
                this.board[x + i][y].setAsWritable();
            }
        } else { // column
            this.board[x][y].setColumn(info);
            for (int i = 1; i <= length; ++i) {
                this.board[x][y + i].setAsWritable();
            }
        }
    }
}
