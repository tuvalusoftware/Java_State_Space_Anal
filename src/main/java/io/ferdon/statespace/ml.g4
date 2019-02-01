grammar ml;


prog : ifelsesyntax
     | ifsyntax
     | token
     | condition
     | expr
     ;

ifelsesyntax:
    IF condition THEN
        (ifsyntax | ifelsesyntax | statement)
    ELSE
        (ifsyntax | ifelsesyntax| statement)
    ;
ifsyntax:
    IF condition THEN
        (ifelsesyntax | ifsyntax | statement)
    ;
statement: expr
       | ID '=' expr
       | token
       ;


expr: expr ('*'|'/') expr
    | expr ('+'|'-') expr
    | INT
    | REAL
    | STRING
    | ID
    |'(' expr ')'
    ;

condition:
    |expr (GT | LESS | EQUAL | GTandEQUAL | EQUALandLESS | NotEQUAL) expr
    |condition (AND | OR) condition
    | NOT condition
    |'(' condition ')'
    ;

token: |'(' expr (',' expr)* ')'
       | '(' ')'
       | INT '`' token
     ;


IF : 'if';
ELSE: 'else';
THEN: 'then';

GT : '>';
LESS : '<';
EQUAL: '=';
GTandEQUAL : '>=';
EQUALandLESS : '<=';
NotEQUAL : '<>';

AND : 'andalso';
OR : 'oralso';
NOT : 'not';

ID : [a-zA-Z]+ ;
INT : [0-9]+ ;
REAL : [0-9]* '.' [0-9]+ ;
STRING : '"' [a-zA-Z0-9]+ '"';
NEWLINE : 'r'? '\n';
WS : [ \t\\]+ -> skip ;