package data_structure;

public class Field2D { // we need that class to gather information about rows and column in one FieldWritable

    private FieldInfo column;
    private FieldInfo row;
    private FieldWritable writable;
    private Type type;

    public Field2D() {
        this.type = Type.BLANK;
    }

    public Field2D(Field2D oldField) {
        type = oldField.type;
        if (type == Type.WRITABLE)
            writable = new FieldWritable(oldField.writable);
        if (type == Type.INFOCOLUMN || type == Type.INFOCOLUMNANDROW)
            column = new FieldInfo(oldField.column);
        if (type == Type.INFOROW || type == Type.INFOCOLUMNANDROW)
            row = new FieldInfo(oldField.row);
    }

    public FieldWritable setAsWritable() {
        if (this.type != Type.WRITABLE) {
            this.type = Type.WRITABLE;
            this.writable = new FieldWritable();
        }
        return this.writable;
    }

    public FieldInfo getColumn() {
        return column;
    }

    public void setColumn(FieldInfo column) { // TODO: (kam193) Maybe we should raise exception, when operation is not permitted
        this.column = column;
        if (this.type == Type.BLANK)
            this.type = Type.INFOCOLUMN;
        else if (this.type == Type.INFOROW)
            this.type = Type.INFOCOLUMNANDROW;
    }

    public FieldInfo getRow() {
        return row;
    }

    public void setRow(FieldInfo row) {
        this.row = row;
        if (this.type == Type.BLANK)
            this.type = Type.INFOROW;
        else if (this.type == Type.INFOCOLUMN)
            this.type = Type.INFOCOLUMNANDROW;
    }

    public FieldWritable getWritable() {
        return writable;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        if (this.type == Type.BLANK) {
            return "[##########]";
        } else if (this.type == Type.WRITABLE) {
            return writable.toString();
        } else if (this.type == Type.INFOCOLUMN) {
            return String.format("[%d\\#######]", this.column.getSum());
        } else if (this.type == Type.INFOCOLUMNANDROW) {
            return String.format("[%d\\%d####]", this.column.getSum(), this.row.getSum());
        } else {
            return String.format("[\\%d#######]", this.row.getSum());
        }
    }

    enum Type {
        WRITABLE, // FieldWritable which should be filled
        INFOCOLUMN, // info about column, row is null
        INFOROW, // info about row, column is null
        INFOCOLUMNANDROW, // info about column and row
        BLANK // cannot fill, we should ignore it
    }
}
