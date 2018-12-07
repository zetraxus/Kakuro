package data_structure;

import java.util.Vector;

public class fieldInfo {

    private byte x, y; // location field on gameBoard
    private byte sum;
    private byte fieldCount; // count of fields which should sum to variable sum in this class

    private Vector<fieldWritable> fields; // this fields has to sum up to value of variable sum

    public fieldInfo(byte x, byte y, byte sum, byte fieldCount) {
        this.x = x;
        this.y = y;
        this.sum = sum;
        this.fieldCount = fieldCount;
    }

    public byte getX() {
        return x;
    }

    public byte getY() {
        return y;
    }

    public byte getFieldCount() {
        return fieldCount;
    }

    public Vector<fieldWritable> getFields() {
        return fields;
    }

    public void addWritableFields(fieldWritable fieldWritable) {
        fields.add(fieldWritable);
    }
}
