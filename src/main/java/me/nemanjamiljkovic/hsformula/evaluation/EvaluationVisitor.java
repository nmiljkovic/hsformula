package me.nemanjamiljkovic.hsformula.evaluation;

import me.nemanjamiljkovic.hsformula.*;
import me.nemanjamiljkovic.hsformula.functions.Function;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.LinkedList;
import java.util.List;

import static me.nemanjamiljkovic.hsformula.HSFormulaParser.*;

public class EvaluationVisitor implements HSFormulaVisitor<Object> {
    private Spreadsheet spreadsheet;

    public EvaluationVisitor(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
    }

    @Override
    public Object visitFormula(FormulaContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public Object visitNumber(NumberContext ctx) {
        double value = Double.valueOf(ctx.NUMBER().getText());
        if (ctx.sign != null && ctx.sign.getText().equals("-")) {
            value = -value;
        }
        return value;
    }

    @Override
    public Object visitString(StringContext ctx) {
        String str = ctx.STRING().getText();
        return str.substring(1, str.length() - 1);
    }

    @Override
    public Object visitCell(CellContext ctx) {
        return CellReference.fromString(ctx.getText());
    }

    @Override
    public Object visitRange(RangeContext ctx) {
        return CellRange.fromString(
            ctx.from.getText(),
            ctx.to.getText()
        );
    }

    @Override
    public Object visitArithAddOrSub(ArithAddOrSubContext ctx) {
        return visitArith(ctx.op, ctx.left, ctx.right);
    }

    @Override
    public Object visitArithMulOrDiv(ArithMulOrDivContext ctx) {
        return visitArith(ctx.op, ctx.left, ctx.right);
    }

    private Object visitArith(Token op, ExpressionContext leftOp, ExpressionContext rightOp) {
        double left = resolveOperandValue(leftOp.accept(this));
        double right = resolveOperandValue(rightOp.accept(this));

        switch (op.getText()) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "*":
                return left * right;
            case "/":
                return left / right;
            default:
                throw new UnsupportedOperationException(
                    String.format("Unknown operation %s", op.getText())
                );
        }
    }

    private double resolveOperandValue(Object operand) {
        if (operand instanceof Double) {
            return (double) operand;
        } else if (operand instanceof CellReference) {
            return spreadsheet.numberAt((CellReference) operand);
        }
        String message = String.format("Expected operand to be number or CellReference, got %s", operand.getClass().getSimpleName());
        throw new RuntimeException(message);
    }

    @Override
    public Object visitFunctionCall(FunctionCallContext ctx) {
        String functionName = ctx.identifier.getText();
        Function fn = HSFormula.FUNCTIONS.get(functionName);
        if (fn == null) {
            throw new RuntimeException(String.format("Function %s has not been defined", functionName));
        }
        List<Object> arguments = (List<Object>) ctx.arguments().accept(this);
        return fn.execute(arguments, spreadsheet);
    }

    @Override
    public Object visitGroup(GroupContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public Object visitArguments(ArgumentsContext ctx) {
        List<Object> arguments = new LinkedList<>();
        for (ExpressionContext argumentContext : ctx.expression()) {
            arguments.add(argumentContext.accept(this));
        }
        return arguments;
    }

    @Override
    public Object visit(ParseTree tree) {
        return tree.accept(this);
    }

    @Override
    public Object visitChildren(RuleNode node) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Object visitTerminal(TerminalNode node) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Object visitErrorNode(ErrorNode node) {
        throw new UnsupportedOperationException("not implemented");
    }
}
