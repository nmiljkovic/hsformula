package me.nemanjamiljkovic.hsformula;

import me.nemanjamiljkovic.hsformula.analysis.AnalysisVisitor;
import me.nemanjamiljkovic.hsformula.evaluation.EvaluationVisitor;
import me.nemanjamiljkovic.hsformula.functions.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.nemanjamiljkovic.hsformula.HSFormulaParser.FormulaContext;

public class HSFormula {

    public static ParseResult parse(String input) {
        CodePointCharStream stream = CharStreams.fromString(input);
        return parse(stream);
    }

    public static ParseResult parse(CharStream stream) {
        HSFormulaLexer lexer = new HSFormulaLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HSFormulaParser parser = new HSFormulaParser(tokens);
        parser.removeErrorListeners();
        ParseErrorListener errorListener = new ParseErrorListener();
        parser.addErrorListener(errorListener);
        FormulaContext ast = parser.formula();
        return new ParseResult(ast, errorListener.getErrors());
    }

    public static String prettyPrint(ParseTree ast) {
        PrettyPrintVisitor visitor = new PrettyPrintVisitor();
        return visitor.visit(ast);
    }

    public static List<ErrorReporter.Error> analyze(ParseTree ast) {
        AnalysisVisitor visitor = new AnalysisVisitor();
        visitor.visit(ast);
        return visitor.getErrors();
    }

    public static Object execute(ParseTree ast, Spreadsheet spreadsheet) {
        EvaluationVisitor visitor = new EvaluationVisitor(spreadsheet);
        return visitor.visit(ast);
    }

    public static final Map<String, Function> FUNCTIONS;

    static {
        FUNCTIONS = new HashMap<>();
        FUNCTIONS.put("SUM", new Sum());
        FUNCTIONS.put("MIN", new Min());
        FUNCTIONS.put("MAX", new Max());
        FUNCTIONS.put("COUNTIF", new CountIf());
        FUNCTIONS.put("COUNT", new Count());
        FUNCTIONS.put("SUMBY", new SumBy());
    }
}
