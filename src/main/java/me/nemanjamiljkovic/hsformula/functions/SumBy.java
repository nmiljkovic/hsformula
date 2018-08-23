package me.nemanjamiljkovic.hsformula.functions;

import me.nemanjamiljkovic.hsformula.CellRange;
import me.nemanjamiljkovic.hsformula.CellReference;
import me.nemanjamiljkovic.hsformula.ErrorReporter;
import me.nemanjamiljkovic.hsformula.Spreadsheet;
import me.nemanjamiljkovic.hsformula.analysis.ResultKind;
import me.nemanjamiljkovic.hsformula.analysis.ResultType;

import java.util.Iterator;
import java.util.List;

public class SumBy implements Function {
    @Override
    public ResultType typeCheck(ResultType arguments, ErrorReporter reporter) {
        List<ResultType> argumentTypes = Arguments.assertArguments(arguments, 3, reporter);
        if (argumentTypes != null) {
            Arguments.assertRangeArgument(argumentTypes.get(0), reporter);
            Arguments.assertNumberArgument(argumentTypes.get(1), reporter);
            Arguments.assertCellArgument(argumentTypes.get(2), reporter);
        }
        return new ResultType(arguments.getCtx(), ResultKind.Number);
    }

    @Override
    public Object execute(List<Object> arguments, Spreadsheet spreadsheet) {
        CellRange sourceRange = (CellRange) arguments.get(0);
        int valueIndex = ((Double) arguments.get(1)).intValue();
        CellReference searchLabelCell = (CellReference) arguments.get(2);
        String searchLabel = spreadsheet.valueAt(searchLabelCell);

        Iterator<CellReference> source = spreadsheet.iterator(sourceRange.toHorizontalRange());
        double sum = 0;
        while (source.hasNext()) {
            CellReference cell = source.next();
            String labelText = spreadsheet.valueAt(cell);

            if (labelText.equals(searchLabel)) {
                CellReference valueCell = new CellReference(
                    cell.getColumnIndex() + valueIndex,
                    cell.getRowIndex()
                );
                sum += spreadsheet.numberAt(valueCell);
            }
        }

        return sum;
    }
}
