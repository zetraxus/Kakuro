package data_structure;

public class field2D { // we need that class to gather information about rows and column in one fieldWritable

    private fieldInfo column;
    private fieldInfo row;
    private fieldWritable writable;
    private Type type;

    public field2D() {
        this.type = Type.BLANK;
    }

    public fieldWritable setAsWritable() {

        if (this.type != Type.WRITABLE) {
            this.type = Type.WRITABLE;
            this.writable = new fieldWritable();
        }
        return this.writable;
    }

    public fieldInfo getColumn() {
        return column;
    }

    public void setColumn(fieldInfo column) { // TODO: (kam193) Maybe we should raise exception, when operation is not permitted
        this.column = column;
        if (this.type == Type.BLANK)
            this.type = Type.INFOCOLUMN;
        else if (this.type == Type.INFOROW)
            this.type = Type.INFOCOLUMNANDROW;
    }

    public fieldInfo getRow() {
        return row;
    }

    public void setRow(fieldInfo row) {
        this.row = row;
        if (this.type == Type.BLANK)
            this.type = Type.INFOROW;
        else if (this.type == Type.INFOCOLUMN)
            this.type = Type.INFOCOLUMNANDROW;
    }

    public fieldWritable getWritable() {
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
        WRITABLE, // fieldWritable which should be filled
        INFOCOLUMN, // info about column, row is null
        INFOROW, // info about row, column is null
        INFOCOLUMNANDROW, // info about column and row
        BLANK // cannot fill, we should ignore it
    }
}
