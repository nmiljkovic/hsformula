package me.nemanjamiljkovic.hsformula;

import org.apache.commons.csv.CSVRecord;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Spreadsheet {

    private String cells[][];
    private int rows, columns;

    public Spreadsheet(String[][] cells) {
        this.cells = cells;
        this.rows = this.cells.length;
        this.columns = this.cells[0].length;
    }

    public String valueAt(CellReference cell) {
        return cells[cell.getRowIndex()][cell.getColumnIndex()].trim();
    }

    public Double numberAt(CellReference cell) {
        try {
            return Double.valueOf(valueAt(cell));
        } catch (Exception e) {
            String message = String.format("Value at %s is not a number", cell.toString());
            throw new IllegalArgumentException(message);
        }
    }

    public void store(CellReference cell, String value) {
        int rowIndex = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();
        this.cells[rowIndex][columnIndex] = value;
    }

    public void store(CellReference cell, Double value) {
        store(cell, value.toString());
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public String[] getRow(int i) {
        return this.cells[i];
    }

    public String[][] getCells() {
        return this.cells;
    }

    public Iterator<CellReference> iterator(CellRange range) {
        CellReference from = range.getFrom();
        CellReference to = range.getTo();

        List<CellReference> cells = new LinkedList<>();
        int toRowIndex = to.hasRowIndex() ? to.getRowIndex() : getRows() - 1;
        for (int row = from.getRowIndex(); row <= toRowIndex; row++) {
            for (int column = from.getColumnIndex(); column <= to.getColumnIndex(); column++) {
                cells.add(new CellReference(column, row));
            }
        }
        return cells.iterator();
    }

    public static Spreadsheet fromCSV(Iterator<CSVRecord> records) {
        int columns = 0;
        List<String[]> cells = new LinkedList<>();
        while (records.hasNext()) {
            CSVRecord record = records.next();
            if (columns == 0) {
                columns = record.size();
            } else if (record.size() != columns) {
                throw new IllegalArgumentException("Each row must have same number of columns");
            }
            String[] row = new String[columns];
            for (int i = 0; i < columns; i++) {
                row[i] = record.get(i);
            }
            cells.add(row);
        }
        String[][] spreadsheetCells = new String[cells.size()][];
        cells.toArray(spreadsheetCells);
        return new Spreadsheet(spreadsheetCells);
    }
}
