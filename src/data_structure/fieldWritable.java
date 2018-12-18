package data_structure;

public class fieldWritable {

    final int OPTIONS = 9; // 1,2,3,...,9

    private boolean[] possibilities;
    private byte possibilitiesCount;
    private State state;
    private fieldInfo rowFieldInfo;
    private fieldInfo columnFieldInfo;

    public fieldWritable() {

        possibilities = new boolean[OPTIONS];
        for (int i = 0; i < OPTIONS; ++i)
            possibilities[i] = true; // all options (1-9) are possible on every fieldWritable at start game
        possibilitiesCount = OPTIONS; // options (1-9) are available
        state = State.UNFILLED;
    }

    public void revokePossibility(int i) {
        if (i >= 1 && i <= 9 && possibilities[i - 1] == true) {
            possibilities[i - 1] = false;
            possibilitiesCount--;
        }
        //if(possibilitiesCount==1)state=State.FILLED;
    }

    public int getValue() {
        if (state == State.FILLED) {
            int newValue = 0;
            for (int j = 0; j < 9; ++j) {
                if (possibilities[j] == true) {
                    newValue = j + 1;
                    return newValue;
                }
            }
        }
        return -1;
    }

    public void setValue(int value) {
        for (int i = 0; i < OPTIONS; ++i) {
            possibilities[i] = false;
        }
        possibilities[value - 1] = true;

        state = State.FILLED;
        possibilitiesCount = 1;
    }

    public boolean[] getPossibilities() {
        return possibilities;
    }

    public void setPossibilities(boolean[] inPossibilities) {
        possibilities = new boolean[OPTIONS];
        possibilitiesCount = 0;
        for (int j = 0; j < 9; ++j) {
            possibilities[j] = inPossibilities[j];
        }
        for (boolean i : possibilities) {
            if (i) possibilitiesCount++;
        }
        if (possibilitiesCount == 1) state = State.FILLED;
        if (possibilitiesCount == 0) state = State.UNFILLED;//TODO error
    }

    public boolean getPossibility(int index) {
        return possibilities[index];
    }

    public byte getPossibilitiesCount() {
        return possibilitiesCount;
    }

    public fieldInfo getRowFieldInfo() {
        return rowFieldInfo;
    }

    public void setRowFieldInfo(fieldInfo rowFieldInfo) {
        this.rowFieldInfo = rowFieldInfo;
    }

    public fieldInfo getColumnFieldInfo() {
        return columnFieldInfo;
    }

    public void setColumnFieldInfo(fieldInfo columnFieldInfo) {
        this.columnFieldInfo = columnFieldInfo;
    }

    @Override
    public String toString() {
        String toReturn;
        if (state == State.UNFILLED)
            toReturn = "N";
        else
            toReturn = "Y";
        for (boolean i : possibilities) {
            if (i) toReturn = toReturn + "1";
            else toReturn = toReturn + "0";
        }
        return "[" + toReturn + "]";
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    enum State {
        FILLED,
        UNFILLED
    }


}
