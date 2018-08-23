package me.nemanjamiljkovic.hsformula;

public class CellReference {
    private static final int maxColumns = 'Z' - 'A';
    private int columnIndex;
    private int rowIndex;

    public CellReference(int columnIndex, int rowIndex) {
        if (columnIndex < 0 || columnIndex > maxColumns) {
            throw new IllegalArgumentException("Column not in range");
        }
        if (rowIndex < -1) {
            throw new IllegalArgumentException("Row is out of range");
        }
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
    }

    public static CellReference fromString(String cell) {
        cell = cell.toUpperCase().trim();
        if (cell.isEmpty()) {
            throw new IllegalArgumentException("Cell cannot be empty");
        }
        int columnIndex = cell.charAt(0) - 'A';

        String rowIndexStr = cell.substring(1);
        if (rowIndexStr.isEmpty()) {
            return new CellReference(columnIndex, -1);
        }
        int rowIndex = Integer.parseInt(rowIndexStr) - 1;
        return new CellReference(columnIndex, rowIndex);
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public boolean hasRowIndex() {
        return rowIndex != -1;
    }

    @Override
    public String toString() {
        char column = (char) (columnIndex + 'A');
        if (hasRowIndex()) {
            return column + Integer.toString(rowIndex + 1);
        } else {
            return String.valueOf(column);
        }
    }
}
