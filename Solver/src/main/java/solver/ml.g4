grammar ml;


prog : ifelsesyntax
     | token
     | multipleToken
     | condition
     | expr
     ;

ifelsesyntax:
    IF condition THEN
        (ifelsesyntax | token)
    ELSE
        (ifelsesyntax | token)
    ;




expr: expr ('*'|'/') expr
    | expr ('+'|'-') expr
    | STRING Append STRING
    | INT
    | REAL
    | ID
    | STRING
    |'(' expr ')'
    ;

condition:
    |expr (GT | LESS | EQUAL | GTandEQUAL | EQUALandLESS | NotEQUAL) expr
    |condition (AND | OR) condition
    | NOT condition
    |'(' condition ')'
    ;

multipleToken:
       | INT '`' token
       ;

token: |'(' expr (',' expr)* ')'
       | Empty
       | '(' ')'

     ;


IF : 'if';
ELSE: 'else';
THEN: 'then';


Empty : 'empty';
GT : '>';
LESS : '<';
EQUAL: '=';
GTandEQUAL : '>=';
EQUALandLESS : '<=';
NotEQUAL : '<>';
Append : '^';
AND : 'andalso';
OR : 'orelse';
NOT : 'not';

ID : [a-zA-Z]+ ;
INT : [0-9]+ ;
REAL : [0-9]* '.' [0-9]+ ;
STRING : '"' [a-zA-Z0-9]* '"';
NEWLINE : 'r'? '\n';
WS : [ \t\\]+ -> skip ;
