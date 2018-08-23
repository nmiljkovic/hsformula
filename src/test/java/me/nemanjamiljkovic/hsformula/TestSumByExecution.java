package me.nemanjamiljkovic.hsformula;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestSumByExecution extends BaseExecutionTest {

    public TestSumByExecution(String input, Object output) {
        super(input, output);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"SUMBY(A1:B, 1, A1)", 3d},
            {"SUMBY(A1:B, 1, A2)", 13d},
            {"SUMBY(A1:B, 1, A3)", 6d},
        });
    }

    private Spreadsheet ss = new Spreadsheet(new String[][]{
        {"Label 1", "1"},
        {"Label 2", "4"},
        {"Label 3", "6"},
        {"Label 2", "9"},
        {"Label 1", "2"},
    });

    @Override
    Spreadsheet getSpreadsheet() {
        return ss;
    }
}
