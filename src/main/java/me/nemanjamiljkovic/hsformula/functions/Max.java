package me.nemanjamiljkovic.hsformula.functions;

import me.nemanjamiljkovic.hsformula.*;
import me.nemanjamiljkovic.hsformula.analysis.ResultKind;
import me.nemanjamiljkovic.hsformula.analysis.ResultType;

import java.util.Iterator;
import java.util.List;

public class Max implements Function {

    @Override
    public ResultType typeCheck(ResultType arguments, ErrorReporter reporter) {
        Arguments.assertSingleRangeArgument(arguments, reporter);
        return new ResultType(arguments.getCtx(), ResultKind.Number);
    }

    @Override
    public Object execute(List<Object> arguments, Spreadsheet spreadsheet) {
        CellRange range = (CellRange) arguments.get(0);
        double max = Double.MIN_VALUE;
        Iterator<CellReference> iterator = spreadsheet.iterator(range);
        while (iterator.hasNext()) {
            CellReference cell = iterator.next();
            max = Double.max(max, spreadsheet.numberAt(cell));
        }
        return max;
    }
}
