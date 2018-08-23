package me.nemanjamiljkovic.hsformula.analysis;

import me.nemanjamiljkovic.hsformula.*;
import me.nemanjamiljkovic.hsformula.functions.Function;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.stream.Collectors;

import static me.nemanjamiljkovic.hsformula.HSFormulaParser.*;

public class AnalysisVisitor implements HSFormulaVisitor<ResultType> {

    private ErrorReporter reporter = new ErrorReporter();

    public List<ErrorReporter.Error> getErrors() {
        return reporter.getErrors();
    }

    @Override
    public ResultType visitFormula(FormulaContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public ResultType visitNumber(NumberContext ctx) {
        return new ResultType(ctx, ResultKind.Number);
    }

    @Override
    public ResultType visitString(StringContext ctx) {
        return new ResultType(ctx, ResultKind.String);
    }

    @Override
    public ResultType visitCell(CellContext ctx) {
        CellReference cell = CellReference.fromString(ctx.CELL().getText());
        return new ResultType(ctx, ResultKind.Cell, cell);
    }

    @Override
    public ResultType visitGroup(GroupContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public ResultType visitRange(RangeContext ctx) {
        try {
            CellRange range = CellRange.fromString(
                ctx.from.getText(),
                ctx.to.getText()
            );
            return new ResultType(ctx, ResultKind.Range, range);
        } catch (Exception e) {
            this.reporter.newError(ctx, e.getMessage());
            return new ResultType(ctx, ResultKind.Range);
        }
    }

    @Override
    public ResultType visitArithMulOrDiv(ArithMulOrDivContext ctx) {
        return visitArithExpression(ctx, ctx.left, ctx.right);
    }

    @Override
    public ResultType visitArithAddOrSub(ArithAddOrSubContext ctx) {
        return visitArithExpression(ctx, ctx.left, ctx.right);
    }

    private ResultType visitArithExpression(ExpressionContext ctx, ExpressionContext leftOp, ExpressionContext rightOp) {
        ResultType left = leftOp.accept(this);
        ResultType right = rightOp.accept(this);
        ResultKind leftKind = left.getKind();
        ResultKind rightKind = right.getKind();

        if (leftKind != ResultKind.Cell &&
            leftKind != ResultKind.Number) {
            String message = String.format(
                "Operand must be a Number or Cell, got %s",
                leftKind.toString()
            );
            this.reporter.newError(left.getCtx(), message);
        }

        if (rightKind != ResultKind.Cell &&
            rightKind != ResultKind.Number) {
            String message = String.format(
                "Operand must be a Number or Cell, got %s",
                rightKind.toString()
            );
            this.reporter.newError(right.getCtx(), message);
        }
        return new ResultType(ctx, ResultKind.Number);
    }

    @Override
    public ResultType visitFunctionCall(FunctionCallContext ctx) {
        String functionName = ctx.identifier.getText().toUpperCase();
        Function function = HSFormula.FUNCTIONS.get(functionName);
        if (function == null) {
            this.reporter.newError(ctx,
                String.format("Function %s does not exist", functionName)
            );
            return null;
        }
        ResultType arguments = ctx.arguments().accept(this);
        return function.typeCheck(arguments, reporter);
    }

    @Override
    public ResultType visitArguments(ArgumentsContext ctx) {
        List<ResultType> type = ctx.expression().stream()
            .map(argument -> argument.accept(this))
            .collect(Collectors.toList());
        return new ResultType(ctx, ResultKind.List, type);
    }

    @Override
    public ResultType visit(ParseTree tree) {
        return tree.accept(this);
    }

    @Override
    public ResultType visitChildren(RuleNode node) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public ResultType visitTerminal(TerminalNode node) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public ResultType visitErrorNode(ErrorNode node) {
        throw new UnsupportedOperationException("not implemented");
    }
}
