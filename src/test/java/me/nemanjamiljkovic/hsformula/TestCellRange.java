package me.nemanjamiljkovic.hsformula;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCellRange {

    private static class TestCase {
        CellRange left;
        CellRange right;
        boolean compatible;

        TestCase(CellRange left, CellRange right, boolean compatible) {
            this.left = left;
            this.right = right;
            this.compatible = compatible;
        }
    }

    private static final TestCase[] testCases = {
        new TestCase(
            CellRange.fromString("B1", "B"),
            CellRange.fromString("C1", "C"),
            true
        ),
        new TestCase(
            CellRange.fromString("B1", "B5"),
            CellRange.fromString("C2", "C6"),
            true
        ),
        new TestCase(
            CellRange.fromString("B1", "C5"),
            CellRange.fromString("D2", "E6"),
            true
        ),
        new TestCase(
            CellRange.fromString("B1", "B5"),
            CellRange.fromString("C2", "C7"),
            false
        ),
        new TestCase(
            CellRange.fromString("B1", "B5"),
            CellRange.fromString("C2", "C"),
            false
        ),
        new TestCase(
            CellRange.fromString("B1", "B"),
            CellRange.fromString("C2", "C"),
            false
        ),
        new TestCase(
            CellRange.fromString("B1", "B"),
            CellRange.fromString("C2", "D"),
            false
        ),
    };

    @Test
    public void testCompatibleWith() {
        for (TestCase testCase : testCases) {
            assertEquals(
                testCase.compatible,
                testCase.left.compatibleWith(testCase.right)
            );
        }
    }
}
