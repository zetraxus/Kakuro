package data_io;


import data_structure.board;
import data_structure.field2D;
import data_structure.fieldInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DataInput {
    final String rowSign = "R";
    private field2D[][] board;
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

    public board makeGameBoard() {
        if (board == null)
            return null;
        return new board((byte) this.width, (byte) this.height, this.board);
    }

    private void prepareBoard(String sizeLine) {
        Scanner scanner = new Scanner(sizeLine);
        this.width = scanner.nextInt();
        this.height = scanner.nextInt();

        this.board = new field2D[this.width][this.height]; // default constructor set all field to blank
        for (int x = 0; x < this.width; ++x)
            for (int y = 0; y < this.height; ++y)
                this.board[x][y] = new field2D();
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

        fieldInfo info = new fieldInfo(x, y, sum, length);

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

    public boolean[] getPossibilities(int x, int y) {//na podstawie pliku sums podaje możliwości
        boolean[] possibilities = new boolean[9];
        try {
            BufferedReader br = new BufferedReader(new FileReader(("examples/sums.txt")));

            String line;
            while ((line = br.readLine()) != null) {
                if (x < 10) {
                    if (line.charAt(0) == (char) x + '0')
                        if (line.charAt(3) == (char) y + '0') {
                            char temp;
                            int index = 5;
                            int max = line.length();
                            while (index < max) {
                                temp = line.charAt(index);
                                ++index;
                                if (temp != '-')
                                    possibilities[temp - '1'] = true;
                            }
                        }
                } else {
                    if (line.substring(0, 2).equals(Integer.toString(x))) {
                        if (line.charAt(4) == (char) y + '0') {
                            char temp;
                            int index = 6;
                            int max = line.length();
                            while (index < max) {
                                temp = line.charAt(index);
                                ++index;
                                if (temp != '-')
                                    possibilities[temp - '1'] = true;
                            }
                        }
                    }
                }
            }
            br.close();
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
        return possibilities;

    }
}
