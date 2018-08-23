package me.nemanjamiljkovic.hsformula;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.LinkedList;
import java.util.List;

public class ParseErrorListener extends BaseErrorListener {

    private List<String> errors = new LinkedList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        this.errors.add("line " + line + ":" + charPositionInLine + " " + msg);
    }

    public List<String> getErrors() {
        return errors;
    }
}
