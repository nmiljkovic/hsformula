package me.nemanjamiljkovic.hsformula;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import static me.nemanjamiljkovic.hsformula.HSFormulaParser.*;

public class DebugPrintVisitor implements HSFormulaVisitor<String> {
    private List<String> methods = new LinkedList<>();

    private String getPrefix() {
        StringJoiner joiner = new StringJoiner(" -> ");
        methods.forEach(joiner::add);
        return joiner.toString();
    }

    private void println(String method, String text) {
        System.out.println(method + ": " + text);
    }

    private void enter(ParserRuleContext ctx) {
        String method = ctx.getClass().getSimpleName().replace("Context", "");
        methods.add(method);
        println(getPrefix(), ctx.getText());
    }

    private void exit(String output) {
        println(getPrefix() + " result", output);
        if (methods.size() > 0) {
            methods.remove(methods.size() - 1);
        }
    }

    @Override
    public String visitFormula(FormulaContext ctx) {
        enter(ctx);
        String output = ctx.expression().accept(this);
        exit(output);
        return output;
    }

    @Override
    public String visitNumber(NumberContext ctx) {
        enter(ctx);

        Token sign = ctx.sign;
        String number = ctx.NUMBER().getText();
        if (sign != null && sign.getText().equals("-")) {
            return String.format("-%s", number);
        }

        exit(number);
        return number;
    }

    @Override
    public String visitString(StringContext ctx) {
        enter(ctx);
        String str = ctx.STRING().getText();
        exit(str);
        return str;
    }

    @Override
    public String visitCell(CellContext ctx) {
        enter(ctx);
        String cell = ctx.CELL().getText().toUpperCase();
        exit(cell);
        return cell;
    }

    @Override
    public String visitGroup(GroupContext ctx) {
        enter(ctx);
        String group = String.format("(%s)", ctx.expression().accept(this));
        exit(group);
        return group;
    }

    @Override
    public String visitRange(RangeContext ctx) {
        enter(ctx);
        String range = String.format("%s:%s",
            ctx.from.getText().toUpperCase(),
            ctx.to.getText().toUpperCase()
        );
        exit(range);
        return range;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public String visitArithMulOrDiv(ArithMulOrDivContext ctx) {
        enter(ctx);
        String op = ctx.op.getText();
        String left = ctx.left.accept(this);
        String right = ctx.right.accept(this);
        String expression = String.format("%s %s %s",
            left, op, right
        );
        exit(expression);
        return expression;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public String visitArithAddOrSub(ArithAddOrSubContext ctx) {
        enter(ctx);
        String op = ctx.op.getText();
        String left = ctx.left.accept(this);
        String right = ctx.right.accept(this);
        String expression = String.format("%s %s %s",
            left, op, right
        );
        exit(expression);
        return expression;
    }

    @Override
    public String visitFunctionCall(FunctionCallContext ctx) {
        enter(ctx);

        String functionName = ctx.identifier.getText().toUpperCase();
        String arguments = ctx.arguments().accept(this);
        String functionCall = String.format("%s(%s)", functionName, arguments);

        exit(functionCall);
        return functionCall;
    }

    @Override
    public String visitArguments(ArgumentsContext ctx) {
        enter(ctx);

        StringJoiner output = new StringJoiner(", ");
        for (ExpressionContext argumentContext : ctx.expression()) {
            output.add(argumentContext.accept(this));
        }
        String arguments = output.toString();

        exit(arguments);
        return arguments;
    }

    @Override
    public String visit(ParseTree tree) {
        return tree.accept(this);
    }

    @Override
    public String visitChildren(RuleNode node) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String visitTerminal(TerminalNode node) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String visitErrorNode(ErrorNode node) {
        throw new UnsupportedOperationException("not implemented");
    }
}
