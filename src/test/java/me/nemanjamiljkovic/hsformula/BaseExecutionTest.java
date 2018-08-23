package me.nemanjamiljkovic.hsformula;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public abstract class BaseExecutionTest {

    private String input;
    private Object output;

    public BaseExecutionTest(String input, Object output) {
        this.input = input;
        this.output = output;
    }

    abstract Spreadsheet getSpreadsheet();

    @Test
    public void test() {
        String parseFailMsg = String.format("Error while parsing '%s'", input);
        String analyzeFailMsg = String.format("Error while analyzing '%s'", input);
        String errorFailMsg = String.format("Error while executing '%s'", input);

        ParseResult parseResult = HSFormula.parse(input);
        ParseTree ast = parseResult.getAst();
        assertFalse(parseFailMsg, parseResult.hasParseErrors());

        List<ErrorReporter.Error> errors = HSFormula.analyze(ast);
        assertEquals(analyzeFailMsg, 0, errors.size());

        Object result = HSFormula.execute(ast, getSpreadsheet());
        assertEquals(errorFailMsg, output, result);
    }
}
