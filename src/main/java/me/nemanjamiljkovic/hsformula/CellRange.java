package me.nemanjamiljkovic.hsformula;

import java.util.Objects;

public class CellRange {
    private static final CellRange emptyRange = new CellRange(
        new CellReference(0, 0),
        new CellReference(0, -1)
    );

    private CellReference from;
    private CellReference to;

    public CellRange(CellReference from, CellReference to) {
        this.from = from;
        this.to = to;

        int horizontalSize = to.getColumnIndex() - from.getColumnIndex() + 1;
        if (horizontalSize <= 0) {
            throw new IllegalArgumentException("Range must not go backwards");
        }

        if (!from.hasRowIndex()) {
            throw new IllegalArgumentException("From cell must have a row index");
        }

        if (to.hasRowIndex()) {
            int verticalSize = to.getRowIndex() - from.getRowIndex() + 1;
            if (verticalSize <= 0) {
                throw new IllegalArgumentException("Range must not go backwards");
            }
        }
    }

    public static CellRange fromString(String from, String to) {
        CellReference fromCell = CellReference.fromString(from);
        CellReference toCell = CellReference.fromString(to);
        return new CellRange(fromCell, toCell);
    }

    public static CellRange empty() {
        return emptyRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellRange cellRange = (CellRange) o;
        return Objects.equals(from, cellRange.from) &&
            Objects.equals(to, cellRange.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    public CellReference getFrom() {
        return from;
    }

    public CellReference getTo() {
        return to;
    }

    public CellRange toHorizontalRange() {
        return new CellRange(
            from,
            new CellReference(
                from.getColumnIndex(),
                to.getRowIndex()
            )
        );
    }

    public boolean compatibleWith(CellRange other) {
        // Test if horizontal length is the same
        int hs = to.getColumnIndex() - from.getColumnIndex();
        int otherHS = other.to.getColumnIndex() - other.from.getColumnIndex();
        if (hs != otherHS) {
            return false;
        }

        // Both must either have row index for the end of the range
        // or both must be infinite
        if (to.hasRowIndex() != other.to.hasRowIndex()) {
            return false;
        }

        // If row index for end is provided, sizes must match
        if (to.hasRowIndex()) {
            int vs = to.getRowIndex() - from.getRowIndex();
            int otherVS = other.to.getRowIndex() - other.from.getRowIndex();
            return vs == otherVS;
        }

        // Infinite column, from row index must match
        return from.getRowIndex() == other.from.getRowIndex();
    }

    @Override
    public String toString() {
        return from.toString() + ":" + to.toString();
    }
}
