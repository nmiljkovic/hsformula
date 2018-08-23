package me.nemanjamiljkovic.hsformula.functions;

import me.nemanjamiljkovic.hsformula.CellRange;
import me.nemanjamiljkovic.hsformula.CellReference;
import me.nemanjamiljkovic.hsformula.ErrorReporter;
import me.nemanjamiljkovic.hsformula.Spreadsheet;
import me.nemanjamiljkovic.hsformula.analysis.ResultKind;
import me.nemanjamiljkovic.hsformula.analysis.ResultType;

import java.util.Iterator;
import java.util.List;

public class Count implements Function {
    @Override
    public ResultType typeCheck(ResultType arguments, ErrorReporter reporter) {
        List<ResultType> args = Arguments.assertArguments(arguments, 1, reporter);
        if (args != null) {
            Arguments.assertRangeArgument(args.get(0), reporter);
        }
        return new ResultType(arguments.getCtx(), ResultKind.Number);
    }

    @Override
    public Object execute(List<Object> arguments, Spreadsheet spreadsheet) {
        CellRange range = (CellRange) arguments.get(0);
        Iterator<CellReference> iterator = spreadsheet.iterator(range);
        double count = 0;
        while (iterator.hasNext()) {
            CellReference cell = iterator.next();
            if (!spreadsheet.valueAt(cell).isEmpty()) {
                count++;
            }
        }
        return count;
    }
}
