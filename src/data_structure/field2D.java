package data_structure;

public class field2D { // we need that class to gather information about rows and column in one fieldWritable

    private fieldInfo column;
    private fieldInfo row;
    private fieldWritable writable;
    private Type type;

    public field2D(fieldInfo fieldInfo, Type type) {
        this.type = type;
        if (type == Type.INFOCOLUMN)
            column = fieldInfo;
        else
            row = fieldInfo;
    }

    public field2D(fieldInfo column, fieldInfo row) {
        this.column = column;
        this.row = row;
        this.type = Type.INFOCOLUMNANDROW;
    }

    public field2D(fieldWritable writable) {
        this.writable = writable;
        this.type = Type.WRITABLE;
    }

    public field2D(Type type) {
        this.type = type;
    }

    public fieldInfo getColumn() {
        return column;
    }

    public fieldInfo getRow() {
        return row;
    }

    public fieldWritable getWritable() {
        return writable;
    }

    public Type getType() {
        return type;
    }

    enum Type {
        WRITABLE, // fieldWritable which should be filled
        INFOCOLUMN, // info about column, row is null
        INFOROW, // info about row, column is null
        INFOCOLUMNANDROW, // info about column and row
        BLANK // cannot fill, we should ignore it
    }
}
