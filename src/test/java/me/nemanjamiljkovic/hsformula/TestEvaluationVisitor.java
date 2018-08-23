package me.nemanjamiljkovic.hsformula;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestEvaluationVisitor extends BaseExecutionTest {

    public TestEvaluationVisitor(String input, Object output) {
        super(input, output);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"1+2*3", 7d},
            {"(1+2)*3", 9d},
            {"2*B1+A2*B2", 16d},
            {"2*SUM(A1:B2)", 20d},
            {"2*SUM(A1:A1)", 2d},
            {"2*SUM(A1:A)", 18d},
            {"MIN(A2:B3)", 3d},
            {"MAX(A2:B3)", 6d},
            {"COUNTIF(A1:A, \"3\")", 1d},
        });
    }

    private Spreadsheet ss = new Spreadsheet(new String[][]{
        {"1", "2"},
        {"3", "4"},
        {"5", "6"},
    });

    @Override
    Spreadsheet getSpreadsheet() {
        return ss;
    }
}
