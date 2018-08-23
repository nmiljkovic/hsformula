package me.nemanjamiljkovic.hsformula;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.StringJoiner;

import static me.nemanjamiljkovic.hsformula.HSFormulaParser.*;

public class PrettyPrintVisitor implements HSFormulaVisitor<String> {
    @Override
    public String visitFormula(FormulaContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public String visitNumber(NumberContext ctx) {
        Token sign = ctx.sign;
        String number = ctx.NUMBER().getText();
        if (sign != null && sign.getText().equals("-")) {
            return String.format("-%s", number);
        }
        return number;
    }

    @Override
    public String visitString(StringContext ctx) {
        return ctx.STRING().getText();
    }

    @Override
    public String visitCell(CellContext ctx) {
        return ctx.CELL().getText().toUpperCase();
    }

    @Override
    public String visitGroup(GroupContext ctx) {
        return String.format("(%s)", ctx.expression().accept(this));
    }

    @Override
    public String visitRange(RangeContext ctx) {
        return String.format("%s:%s",
            ctx.from.getText().toUpperCase(),
            ctx.to.getText().toUpperCase()
        );
    }

    @Override
    public String visitArithMulOrDiv(ArithMulOrDivContext ctx) {
        String op = ctx.op.getText();
        return String.format("%s %s %s",
            ctx.left.accept(this),
            op,
            ctx.right.accept(this)
        );
    }

    @Override
    public String visitFunctionCall(FunctionCallContext ctx) {
        String functionName = ctx.identifier.getText().toUpperCase();
        String arguments = ctx.arguments().accept(this);
        return String.format("%s(%s)", functionName, arguments);
    }

    @Override
    public String visitArithAddOrSub(ArithAddOrSubContext ctx) {
        String op = ctx.op.getText();
        return String.format("%s %s %s",
            ctx.left.accept(this),
            op,
            ctx.right.accept(this)
        );
    }

    @Override
    public String visitArguments(ArgumentsContext ctx) {
        StringJoiner output = new StringJoiner(", ");
        for (ExpressionContext argumentContext : ctx.expression()) {
            output.add(argumentContext.accept(this));
        }
        return output.toString();
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
