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

    public boolean isPossibleToSolve() {
        List<List<FieldWritable>> possibilities = this.collectAllPossibilities();
//        List<int[]> possibleCombinations = new ArrayList<>();
        boolean yes = false;
        for (int[] combination : PossiblesSumCombinations.getPossiblesSumsCombinations(this.sum, this.fieldCount)) {
            if (isPossibleCombination(combination, 0, possibilities, new ArrayList<>())) {
                yes = true;
                break;//possibleCombinations.add(combination);
            }
        }
        if (!yes)
            return false;
        int filledValue = 0;
        int numOfFilled = 0;
        for (FieldWritable i : fields) {
            if (i.getState() == FieldWritable.State.FILLED) {
                filledValue += i.getValue();
                numOfFilled += 1;
            }
        }
        if (this.sum > filledValue) {
            boolean[] hej = PossiblesSumCombinations.getPossibilities(this.sum - filledValue, this.fieldCount - numOfFilled);
            for (FieldWritable i : fields) {
                if (i.getState() != FieldWritable.State.FILLED) {
                    boolean[] o = i.getPossibilities();
                    boolean[] co = new boolean[9];
                    for (int j = 0; j != 9; ++j)
                        if (o[j] && hej[j]) co[j] = true;
//                    i.setPossibilities(new boolean[]{o[0]&hej[0],o[1]&hej[1],o[2]&hej[2],o[3]&hej[3],o[4]&hej[4],o[5]&hej[5],o[6]&hej[6],o[7]&hej[7],o[8]&hej[8]});
                    i.setPossibilities(co);
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
