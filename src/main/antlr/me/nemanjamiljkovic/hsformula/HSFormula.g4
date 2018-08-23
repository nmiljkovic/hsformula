grammar HSFormula;

formula
    : expression
    ;

expression
    : left=expression op=('*'|'/') right=expression # arithMulOrDiv
    | left=expression op=('+'|'-') right=expression # arithAddOrSub
    | LPAREN expression RPAREN  # group

    | CELL                  # cell
    | from=CELL ':' to=CELL # range

    | identifier=IDENTIFIER LPAREN arguments RPAREN # functionCall

    | sign=('+'|'-')? NUMBER # number
    | STRING                 # string
    ;

arguments
    : expression (',' expression)*
    ;

CELL
    : [a-zA-Z][0-9]*
    ;

IDENTIFIER
    : [a-zA-Z]+
    ;

NUMBER
    : [0-9]+('.'[0-9]+)?
    ;

LPAREN
    : '('
    ;

RPAREN
    : ')'
    ;

STRING
    : '"' StringCharacters? '"'
    ;

fragment StringCharacters
    : StringCharacter+
    ;

fragment StringCharacter
    : ~["\n\r]
    ;

EOL
    : [\r\n]+ -> skip
    ;

WS
    : [ \t] -> skip
    ;
