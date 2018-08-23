package me.nemanjamiljkovic.hsformula;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestPrettyPrintVisitor {
    private static class TestCase {
        String input;
        String expected;

        TestCase(String input, String expected) {
            this.input = input;
            this.expected = expected;
        }
    }

    private static TestCase[] testCases = {
        new TestCase(
            "1.2+2*3+(1--4)",
            "1.2 + 2 * 3 + (1 - -4)"
        ),
        new TestCase(
            "sum(B1:b3)+5",
            "SUM(B1:B3) + 5"
        ),
        new TestCase(
            "b1+b2-c3",
            "B1 + B2 - C3"
        ),
        new TestCase(
            " \" Test string %d  %f \"   ",
            "\" Test string %d  %f \""
        )
    };

    @Test
    public void testPrettyPrinter() {
        for (TestCase testCase : testCases) {
            ParseResult parseResult = HSFormula.parse(testCase.input);
            ParseTree ast = parseResult.getAst();

            assertFalse(String.format("Failed parsing: %s", testCase.input), parseResult.hasParseErrors());
            String output = HSFormula.prettyPrint(ast);
            assertEquals(testCase.expected, output);
        }
    }
}
