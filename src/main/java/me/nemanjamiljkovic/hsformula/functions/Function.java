package me.nemanjamiljkovic.hsformula.functions;

import me.nemanjamiljkovic.hsformula.ErrorReporter;
import me.nemanjamiljkovic.hsformula.analysis.ResultType;
import me.nemanjamiljkovic.hsformula.Spreadsheet;

import java.util.List;

public interface Function {
    ResultType typeCheck(ResultType arguments, ErrorReporter reporter);

    Object execute(List<Object> arguments, Spreadsheet spreadsheet);
}
