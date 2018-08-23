package me.nemanjamiljkovic.hsformula;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestCellReference {

    @Test
    public void testFromString() {
        CellReference c15 = CellReference.fromString("c15");
        assertEquals(2, c15.getColumnIndex());
        assertEquals(14, c15.getRowIndex());
        assertTrue(c15.hasRowIndex());

        CellReference bCellInfinite = CellReference.fromString("B");
        assertEquals(1, bCellInfinite.getColumnIndex());
        assertEquals(-1, bCellInfinite.getRowIndex());
        assertFalse(bCellInfinite.hasRowIndex());
    }
}
