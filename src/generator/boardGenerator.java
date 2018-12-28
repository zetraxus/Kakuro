package generator;

import java.lang.reflect.Field;
import java.util.Random;

public class BoardGenerator {
    private final int BLANK = 0;
    private final int FILLED = -1;
    private int[][] board;
    private int size;

    public BoardGenerator(int size) {
        size--;
        if (size < 2) {
            this.size = 2;
        } else {
            this.size = size;
        }
        board = new int[this.size][this.size];
        generate();
        while (checkBlankFieldsSpaceReq())
            generate();
        while (!setValues())
            resetBoard();
        show();
        System.out.println(toString());
    }

    //this generator could generate board with two fields that don't have paths to one another

    private void generate() {
        Random rand = new Random();
        int n;
        for (int i = 0; i != size; ++i) {
            for (int j = 0; j != size; ++j) {
                if ((n = rand.nextInt(2)) == 0)
                    n = rand.nextInt(2);
                board[i][j] = 0 - n;//75% chance of FILLED, 25% chance of BLANK
            }
        }

        if (size > 9) {
            int length;
            for (int i = 0; i != size; ++i) {
                length = 0;
                for (int j = 0; j != size; ++j) {
                    if (board[i][j] == FILLED) {
                        ++length;
                    } else {
                        length = 0;
                    }
                    if (length > 9) {
                        board[i][j] = BLANK;
                        length = 0;
                    }
                }

            }
            for (int i = 0; i != size; ++i) {
                length = 0;
                for (int j = 0; j != size; ++j) {
                    if (board[j][i] == FILLED) {
                        ++length;
                    } else {
                        length = 0;
                    }
                    if (length > 9) {
                        board[j][i] = BLANK;
                        length = 0;
                    }
                }

            }
        }


        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;
            for (int i = 0; i != size; ++i) {
                for (int j = 0; j != size; ++j) {
                    if (board[i][j] == FILLED) {
                        if ((i == 0 || board[i - 1][j] != FILLED) && (i == size - 1 || board[i + 1][j] != FILLED)) {
                            board[i][j] = BLANK;
                            isChanged = true;
                        } else if ((j == 0 || board[i][j - 1] != FILLED) && (j == size - 1 || board[i][j + 1] != FILLED)) {
                            board[i][j] = BLANK;
                            isChanged = true;
                        }
                    }
                }
            }
        }

    }

    private boolean checkBlankFieldsSpaceReq() {
        int n = 0;
        for (int i = 0; i != size; ++i) {
            for (int j = 0; j != size; ++j) {
                if (board[i][j] == FILLED) n++;
            }
        }
        return n <= size * size * 5 / 8;
    }

    private boolean setValues() {
        int temp;
        boolean[] possibilities = new boolean[9];
        for (int k = 0; k != 9; ++k) {
            possibilities[k] = true;
        }
        Random rand = new Random();
        for (int i = 0; i != size; ++i) {
            for (int j = 0; j != size; ++j) {
                if (board[i][j] < 0) {
                    temp = rand.nextInt(9) + 1;
                    board[i][j] = temp;
                    while (checkNumber(i, j, temp)) {
                        possibilities[temp - 1] = false;
                        if (!possibilities[0] && !possibilities[1] && !possibilities[2] && !possibilities[3] && !possibilities[4] && !possibilities[5] && !possibilities[6] && !possibilities[7] && !possibilities[8]) {
                            return false;
                        }
                        temp = rand.nextInt(9) + 1;
                        board[i][j] = temp;
                    }
                    for (int k = 0; k != 9; ++k) {
                        possibilities[k] = true;
                    }
                }
            }
        }

        return true;
    }

    private boolean checkNumber(int x, int y, int value) {//function used by setValues
        int tempX = x;
        int tempY = y;
        while (tempX > 0 && board[tempX - 1][y] != 0) tempX--;
        while (tempX < size && board[tempX][y] != 0) {
            if (tempX != x && board[tempX][y] == value) return true;
            tempX++;
        }
        while (tempY > 0 && board[x][tempY - 1] != 0) tempY--;
        while (tempY < size && board[x][tempY] != 0) {
            if (tempY != y && board[x][tempY] == value) return true;
            tempY++;
        }
        return false;
    }

    private void resetBoard() {
        for (int i = 0; i != size; ++i) {
            for (int j = 0; j != size; ++j) {
                if (board[i][j] > 0)
                    board[i][j] = -1;
            }
        }
    }

    private void show() {
        for (int i = 0; i != size; ++i) {
            for (int j = 0; j != size; ++j) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

    }

    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append(size + 1).append(" ").append(size + 1).append("\n");
        int length;
        int sum;
        int start;
        int end;
        for (int i = 0; i != size; ++i) {
            length = 0;
            sum = 0;
            start = -1;
            end = 0;
            while (end < size) {
                if (board[i][end] == 0) {
                    start = end;
                    length = 0;
                    sum = 0;
                } else {
                    length++;
                    sum += board[i][end];
                    if (end + 1 == size || board[i][end + 1] == 0) {
                        toReturn.append("R ").append(start + 1).append(" ").append(i + 1).append(" ").append(sum).append(" ").append(length).append("\n");
                    }
                }
                end++;
            }
        }

        for (int i = 0; i != size; ++i) {
            length = 0;
            sum = 0;
            start = -1;
            end = 0;
            while (end < size) {
                if (board[end][i] == 0) {
                    start = end;
                    length = 0;
                    sum = 0;
                } else {
                    length++;
                    sum += board[end][i];
                    if (end + 1 == size || board[end + 1][i] == 0) {
                        toReturn.append("C ").append(i + 1).append(" ").append(start + 1).append(" ").append(sum).append(" ").append(length).append("\n");
                    }
                }
                end++;
            }
        }
        return toReturn.toString();
    }
}
