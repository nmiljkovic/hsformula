package me.nemanjamiljkovic.hsformula.functions;

import me.nemanjamiljkovic.hsformula.CellRange;
import me.nemanjamiljkovic.hsformula.CellReference;
import me.nemanjamiljkovic.hsformula.ErrorReporter;
import me.nemanjamiljkovic.hsformula.Spreadsheet;
import me.nemanjamiljkovic.hsformula.analysis.ResultKind;
import me.nemanjamiljkovic.hsformula.analysis.ResultType;

import java.util.Iterator;
import java.util.List;

public class CountIf implements Function {
    @Override
    public ResultType typeCheck(ResultType arguments, ErrorReporter reporter) {
        List<ResultType> argumentTypes = Arguments.assertArguments(arguments, 2, reporter);
        if (argumentTypes != null) {
            Arguments.assertRangeArgument(argumentTypes.get(0), reporter);
            Arguments.assertStringArgument(argumentTypes.get(1), reporter);
        }
        return new ResultType(arguments.getCtx(), ResultKind.Number);
    }

    @Override
    public Object execute(List<Object> arguments, Spreadsheet spreadsheet) {
        CellRange range = (CellRange) arguments.get(0);
        String expected = (String) arguments.get(1);
        Iterator<CellReference> cellIter = spreadsheet.iterator(range);

        double count = 0;
        while (cellIter.hasNext()) {
            CellReference cell = cellIter.next();
            String value = spreadsheet.valueAt(cell);
            if (value.equals(expected)) {
                count++;
            }
        }
        return count;
    }
}
