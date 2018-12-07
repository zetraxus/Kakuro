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

    public void setPossibilities(boolean[] possibilities) {
        this.possibilities = possibilities;
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

    public void changePossibility(byte indeks, boolean newValue){

        if(newValue != possibilities[indeks]) {
            possibilities[indeks] = newValue;

            if(newValue == true)
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
}
