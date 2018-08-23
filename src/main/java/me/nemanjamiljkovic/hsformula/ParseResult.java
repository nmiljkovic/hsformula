package me.nemanjamiljkovic.hsformula;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class ParseResult {
    private ParseTree ast;
    private List<String> parseErrors;

    public ParseResult(ParseTree ast, List<String> parseErrors) {
        this.ast = ast;
        this.parseErrors = parseErrors;
    }

    public ParseTree getAst() {
        return ast;
    }

    public List<String> getParseErrors() {
        return parseErrors;
    }

    public boolean hasParseErrors() {
        return parseErrors.size() > 0;
    }
}
