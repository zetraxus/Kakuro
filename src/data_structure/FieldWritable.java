package data_structure;

public class FieldWritable {

    final int OPTIONS = 9; // 1,2,3,...,9
    int storedValue = -1;

    private boolean[] possibilities;
    private byte possibilitiesCount;
    private State state;
    private FieldInfo rowFieldInfo;
    private FieldInfo columnFieldInfo;

    public FieldWritable() {
        possibilities = new boolean[OPTIONS];
        for (int i = 0; i < OPTIONS; ++i)
            possibilities[i] = true; // all options (1-9) are possible on every FieldWritable at start game
        possibilitiesCount = OPTIONS; // options (1-9) are available
        state = State.UNFILLED;
    }

    public FieldWritable(FieldWritable oldField) {
        possibilities = new boolean[OPTIONS];
        possibilitiesCount = oldField.getPossibilitiesCount();
        for (int i = 0; i < OPTIONS; ++i) {
            possibilities[i] = oldField.getPossibility(i);
        }
        state = oldField.getState();
        rowFieldInfo = oldField.getRowFieldInfo();
        columnFieldInfo = oldField.getColumnFieldInfo();
    }

    public void revokePossibility(int i) {
        if (i >= 1 && i <= 9 && possibilities[i - 1] == true) {
            possibilities[i - 1] = false;
            possibilitiesCount--;
        }
    }

    public int getValue() {
        if (state == State.FILLED) {
            if (storedValue == -1)
                updateStoredValue();
            return storedValue;
        }
        return -1;
    }

    public void setValue(int value) {
        for (int i = 0; i < OPTIONS; ++i) {
            possibilities[i] = false;
        }
        possibilities[value - 1] = true;

        state = State.FILLED;
        storedValue = value;
        possibilitiesCount = 1;
    }

    private void updateStoredValue() {
        for (int j = 0; j < 9; ++j) {
            if (possibilities[j] == true) {
                storedValue = j + 1;
            }
        }
    }

    public boolean[] getPossibilities() {
        return possibilities;
    }

    public void setPossibilities(boolean[] inPossibilities) {
        possibilitiesCount = 0;
        for (int j = 0; j < 9; ++j) {
            possibilities[j] = inPossibilities[j];
            if (possibilities[j])
                possibilitiesCount++;
        }
        if (possibilitiesCount == 1) state = State.FILLED;
        if (possibilitiesCount == 0) state = State.UNFILLED;//TODO error
        storedValue = -1;
    }

    public boolean getPossibility(int index) {
        return possibilities[index];
    }

    public byte getPossibilitiesCount() {
        return possibilitiesCount;
    }

    public FieldInfo getRowFieldInfo() {
        return rowFieldInfo;
    }

    public void setRowFieldInfo(FieldInfo rowFieldInfo) {
        this.rowFieldInfo = rowFieldInfo;
    }

    public FieldInfo getColumnFieldInfo() {
        return columnFieldInfo;
    }

    public void setColumnFieldInfo(FieldInfo columnFieldInfo) {
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
        storedValue = -1;
    }

    public enum State {
        FILLED,
        UNFILLED
    }
}
