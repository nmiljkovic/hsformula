package me.nemanjamiljkovic.hsformula.analysis;

import org.antlr.v4.runtime.ParserRuleContext;

public class ResultType {

    private ParserRuleContext ctx;
    private ResultKind kind;
    private Object value;

    public ResultType(ParserRuleContext ctx, ResultKind kind) {
        this(ctx, kind, null);
    }

    public ResultType(ParserRuleContext ctx, ResultKind kind, Object value) {
        this.ctx = ctx;
        this.kind = kind;
        this.value = value;
    }

    public ParserRuleContext getCtx() {
        return ctx;
    }

    public ResultKind getKind() {
        return kind;
    }

    public Object getValue() {
        return value;
    }

}
