package data_structure;

import data_io.PossiblesSumCombinations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class FieldInfo {

    private byte x, y; // location field on gameBoard

    private byte sum;
    private byte fieldCount; // count of fields which should sum to variable sum in this class

    private Vector<FieldWritable> fields = new Vector<>(); // this fields has to sum up to value of variable sum

    public FieldInfo(byte x, byte y, byte sum, byte fieldCount) {
        this.x = x;
        this.y = y;
        this.sum = sum;
        this.fieldCount = fieldCount;
    }

    public FieldInfo(FieldInfo oldField) {
        x = oldField.x;
        y = oldField.y;
        sum = oldField.sum;
        fieldCount = oldField.fieldCount;
    }

    public byte getX() {
        return x;
    }

    public byte getY() {
        return y;
    }

    public byte getSum() {
        return sum;
    }

    public byte getFieldCount() {
        return fieldCount;
    }

    public Vector<FieldWritable> getFields() {
        return fields;
    }

    public void addWritableFields(FieldWritable fieldWritable) {
        fields.add(fieldWritable);
    }

    public boolean isPossibleToSolve(History history) {
        List<List<FieldWritable>> possibilities = this.collectAllPossibilities();
        boolean possibleToSolve = false;
        for (int[] combination : PossiblesSumCombinations.getPossiblesSumsCombinations(this.sum, this.fieldCount)) {
            if (isPossibleCombination(combination, 0, possibilities, new ArrayList<>())) {
                possibleToSolve = true;
                break;
            }
        }
        if (!possibleToSolve)
            return false;
        int filledValue = 0;
        int numOfFilled = 0;
        for (FieldWritable i : fields) {
            if (i.getState() == FieldWritable.State.FILLED) {
                filledValue += i.getValue();
                numOfFilled += 1;
            }
        }
        if (this.sum > filledValue && this.fieldCount - numOfFilled > 1) {
            boolean[] possibilitiesOfLine = PossiblesSumCombinations.getPossibilities(this.sum - filledValue, this.fieldCount - numOfFilled);
            for (FieldWritable i : fields) {
                if (i.getState() != FieldWritable.State.FILLED) {
                    boolean[] possibilitiesOfThisField = i.getPossibilities();
                    i.setPossibilities(new boolean[]{possibilitiesOfThisField[0] & possibilitiesOfLine[0], possibilitiesOfThisField[1] & possibilitiesOfLine[1], possibilitiesOfThisField[2] & possibilitiesOfLine[2], possibilitiesOfThisField[3] & possibilitiesOfLine[3], possibilitiesOfThisField[4] & possibilitiesOfLine[4], possibilitiesOfThisField[5] & possibilitiesOfLine[5], possibilitiesOfThisField[6] & possibilitiesOfLine[6], possibilitiesOfThisField[7] & possibilitiesOfLine[7], possibilitiesOfThisField[8] & possibilitiesOfLine[8]});
                    if (i.getState() == FieldWritable.State.FILLED) {
                        history.add(i.getColumnFieldInfo().getX(), i.getRowFieldInfo().getY());
                    }
                }
            }
        }

        return true;
    }

    private boolean isPossibleCombination(int[] combination, int index, List<List<FieldWritable>> possibilities, List<FieldWritable> excluded) {
        if (index >= combination.length)
            return true;
        for (FieldWritable possibleField : possibilities.get(combination[index] - 1)) {
            if (excluded.contains(possibleField))
                continue;
            excluded.add(possibleField);
            if (isPossibleCombination(combination, index + 1, possibilities, excluded))
                return true;
            excluded.remove(excluded.size() - 1);
        }
        return false;
    }

    private List<List<FieldWritable>> collectAllPossibilities() {
        // For each possibility, collect FieldsWritable which can be filled with this value
        List<List<FieldWritable>> possibilities = new ArrayList<>();
        for (int i = 0; i < 9; ++i)
            possibilities.add(new ArrayList<>());

        for (FieldWritable field : this.fields) {
            for (int i = 0; i < 9; ++i)
                if (field.getPossibility(i))
                    possibilities.get(i).add(field);
        }

        return possibilities;
    }
}
