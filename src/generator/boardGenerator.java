package generator;

import java.util.Random;

public class boardGenerator {
    private int[][] board;
    private int size;

    public boardGenerator(int size) {
        size--;
        if(size < 3){
            this.size=3;
        }
        if (size > 9)//TODO large maps, they are tricky, because maximum of line length=9
            this.size = 9;
        else
            this.size = size;
        board = new int[size][size];
        generate();
        while (checkReq())// hehe, yeah, generate 'til...
            generate();
        while(!setValues())
            resetBoard();
        show();
        System.out.println(toString());
    }

    //this generator could generate board with two fields that don't have routes to one another

    private void generate() {
        Random rand = new Random();
        int n;
        for (int i = 0; i != size; ++i) {
            for (int j = 0; j != size; ++j) {
                if ((n = rand.nextInt(2)) == 0)
                    n = rand.nextInt(2);
                board[i][j] = 0 - n;
            }
        }
        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;
            for (int i = 0; i != size; ++i) {
                for (int j = 0; j != size; ++j) {
                    if (board[i][j] == -1) {
                        if ((i == 0 || board[i - 1][j] != -1) && (i == size - 1 || board[i + 1][j] != -1)) {
                            board[i][j] = 0;
                            isChanged = true;
                        }
                    }
                    if (board[i][j] == -1) {
                        if ((j == 0 || board[i][j - 1] != -1) && (j == size - 1 || board[i][j + 1] != -1)) {
                            board[i][j] = 0;
                            isChanged = true;
                        }
                    }
                }
            }
        }

    }

    private boolean checkReq() {
        int n = 0;
        for (int i = 0; i != size; ++i) {
            for (int j = 0; j != size; ++j) {
                if (board[i][j] == -1) n++;
            }
        }
        return n <= size * size * 3 / 4;
    }

    private boolean setValues() {
        int temp;
        boolean[] possibilities = new boolean[9];
        for(int k=0;k!=9;++k){
            possibilities[k]=true;
        }
        Random rand = new Random();
        for (int i = 0; i != size; ++i) {
            for (int j = 0; j != size; ++j) {
                if (board[i][j] < 0) {
                    temp = rand.nextInt(9) + 1;
                    board[i][j] = temp;
                    while (checkNumber(i, j, temp)) {
                        possibilities[temp-1]=false;
                        if(!possibilities[0]&&!possibilities[1]&&!possibilities[2]&&!possibilities[3]&&!possibilities[4]&&!possibilities[5]&&!possibilities[6]&&!possibilities[7]&&!possibilities[8]){
                            return false;
                        }
                        temp = rand.nextInt(9) + 1;
                        board[i][j] = temp;
                    }
                    for(int k=0;k!=9;++k){
                        possibilities[k]=true;
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
        String toReturn = (size + 1) + " " + (size + 1) + "\n";
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
                        toReturn = toReturn + "R " + (start + 1) + " " + (i + 1) + " " + sum + " " + length + "\n";
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
                        toReturn = toReturn + "C " + (i + 1) + " " + (start + 1) + " " + sum + " " + length + "\n";
                    }
                }
                end++;
            }
        }
        return toReturn;
    }
}
