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
        possibilitiesCount = 9; // options (1-9) are available
        state = State.UNFILLED;
    }

    public void setRowFieldInfo(fieldInfo rowFieldInfo) {
        this.rowFieldInfo = rowFieldInfo;
    }

    public void setColumnFieldInfo(fieldInfo columnFieldInfo) {
        this.columnFieldInfo = columnFieldInfo;
    }

    public void setValue(int value) {
        for (int i = 0; i < OPTIONS; ++i) {
            possibilities[i] = i == value - 1;
        }

        state = State.FILLED;
        possibilitiesCount = 1;
    }

    public void setPossibilities(boolean[] possibilities) {
        this.possibilities = possibilities;
        possibilitiesCount = 0;
        for (boolean i : possibilities) {
            if (i) possibilitiesCount++;
        }
        if (possibilitiesCount == 1) state = State.FILLED;
        if (possibilitiesCount == 0) state = State.UNFILLED;//TODO error
    }

    public int revokePossibility(int i) {
        if (1 <= i && i <= 9 && possibilities[i - 1] == true) {
            possibilities[i - 1] = false;
            possibilitiesCount--;
        }
        if (possibilitiesCount == 1) {
            //state = State.FILLED;
        }
        return getValue();
    }

    public int getValue() {
        if (state == State.FILLED) {
            int newValue = 0;
            for (int j = 0; j < 9; ++j) {
                if (possibilities[j] == true)
                    newValue = j + 1;
            }
            return newValue;
        }
        return -1;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean[] getPossibilities() {
        return possibilities;
    }

    public byte getPossibilitiesCount() {
        return possibilitiesCount;
    }

    public State getState() {
        return state;
    }

    public fieldInfo getRowFieldInfo() {
        return rowFieldInfo;
    }

    public void changePossibility(byte index, boolean newValue) {

        if (newValue != possibilities[index]) {
            possibilities[index] = newValue;

            if (newValue == true)
                ++possibilitiesCount;
            else
                --possibilitiesCount;
        }
    }

    public fieldInfo getColumnFieldInfo() {
        return columnFieldInfo;
    }

    enum State {
        FILLED,
        UNFILLED
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

}
