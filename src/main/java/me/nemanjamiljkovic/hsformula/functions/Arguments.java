package me.nemanjamiljkovic.hsformula.functions;

import me.nemanjamiljkovic.hsformula.CellRange;
import me.nemanjamiljkovic.hsformula.CellReference;
import me.nemanjamiljkovic.hsformula.ErrorReporter;
import me.nemanjamiljkovic.hsformula.analysis.ResultKind;
import me.nemanjamiljkovic.hsformula.analysis.ResultType;

import java.util.List;

class Arguments {

    static CellRange assertRangeArgument(ResultType argument, ErrorReporter reporter) {
        if (argument.getKind() == ResultKind.Range) {
            return (CellRange) argument.getValue();
        }
        reporter.newError(argument.getCtx(), "Argument must be a cell range");
        return null;
    }

    static CellReference assertCellArgument(ResultType argument, ErrorReporter reporter) {
        if (argument.getKind() == ResultKind.Cell) {
            return (CellReference) argument.getValue();
        }
        reporter.newError(argument.getCtx(), "Argument must be a cell reference");
        return null;
    }

    static boolean assertNumberArgument(ResultType argument, ErrorReporter reporter) {
        if (argument.getKind() == ResultKind.Number) {
            return true;
        }
        reporter.newError(argument.getCtx(), "Argument must be a number");
        return false;
    }

    static boolean assertStringArgument(ResultType argument, ErrorReporter reporter) {
        if (argument.getKind() == ResultKind.String) {
            return true;
        }
        reporter.newError(argument.getCtx(), "Argument must be a string");
        return false;
    }

    static List<ResultType> assertArguments(ResultType arguments, int size, ErrorReporter reporter) {
        if (arguments.getKind() != ResultKind.List) {
            reporter.newError(arguments.getCtx(), "Arguments must be a list");
            return null;
        }

        List<ResultType> args = (List<ResultType>) arguments.getValue();
        if (args.size() != size) {
            String message = String.format("Expected %d argument(s), got %d", size, args.size());
            reporter.newError(arguments.getCtx(), message);
            return null;
        }
        return args;
    }

    static void assertSingleRangeArgument(ResultType arguments, ErrorReporter reporter) {
        List<ResultType> argumentTypes = assertArguments(arguments, 1, reporter);
        if (argumentTypes != null) {
            assertRangeArgument(argumentTypes.get(0), reporter);
        }
    }
}
