package me.nemanjamiljkovic.hsformula;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TestAnalysisVisitor {

    private static class TestCase {
        String input;
        List<String> expected;

        TestCase(String input, List<String> expected) {
            this.input = input;
            this.expected = expected;
        }
    }

    private static TestCase[] arithmeticTests = {
        new TestCase(
            "1+2",
            Collections.emptyList()
        ),
        new TestCase(
            "1+B1",
            Collections.emptyList()
        ),
        new TestCase(
            "C2*B1",
            Collections.emptyList()
        ),
        new TestCase(
            "C2*B1",
            Collections.emptyList()
        ),
        new TestCase(
            "B1*B1:B2",
            Collections.singletonList("1:3:Operand must be a Number or Cell, got Range")
        ),
    };

    private static TestCase[] sumTests = {
        new TestCase(
            "2*SUM(B1:B)",
            Collections.emptyList()
        ),
        new TestCase(
            "2*SUM(B:B)",
            Collections.singletonList("1:6:From cell must have a row index")
        ),
        new TestCase(
            "2*SUM(B1)",
            Collections.singletonList("1:6:Argument must be a cell range")
        ),
        new TestCase(
            "2*SUM(B1:B,B2)",
            Collections.singletonList("1:6:Expected 1 argument(s), got 2")
        ),
    };

    @Test
    public void testArithmetic() {
        runTests(arithmeticTests);
    }

    @Test
    public void testSum() {
        runTests(sumTests);
    }

    private void runTests(TestCase[] testCases) {
        for (TestCase testCase : testCases) {
            ParseResult parseResult = HSFormula.parse(testCase.input);
            List<ErrorReporter.Error> errors = HSFormula.analyze(parseResult.getAst());
            String message = String.format("Mismatch while analyzing '%s'", testCase.input);
            assertEquals(message, testCase.expected.size(), errors.size());
            for (int i = 0; i < testCase.expected.size(); i++) {
                ErrorReporter.Error error = errors.get(i);
                String expected = testCase.expected.get(i);
                String errorStr = String.format("%d:%d:%s", error.getLine(), error.getColumn(), error.getMessage());
                assertEquals(message, expected, errorStr);
            }
        }
    }

}
