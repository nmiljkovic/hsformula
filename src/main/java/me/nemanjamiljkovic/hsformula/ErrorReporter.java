package me.nemanjamiljkovic.hsformula;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.LinkedList;
import java.util.List;

public class ErrorReporter {
    public class Error {
        private String message;
        private int line;
        private int column;

        public Error(String message, int line, int column) {
            this.message = message;
            this.line = line;
            this.column = column;
        }

        public String getMessage() {
            return message;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }
    }

    private List<Error> errors = new LinkedList<>();

    public void newError(ParserRuleContext ctx, String message) {
        this.errors.add(new Error(
            message,
            ctx.start.getLine(),
            ctx.start.getStartIndex()
        ));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
