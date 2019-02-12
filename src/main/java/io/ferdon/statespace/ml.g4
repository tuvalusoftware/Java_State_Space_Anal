grammar ml;


prog : ifelsesyntax
     | token
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
    | expr Concat expr
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
Concat : '^^';
AND : 'andalso';
OR : 'oralso';
NOT : 'not';

ID : [a-zA-Z]+ ;
INT : [0-9]+ ;
REAL : [0-9]* '.' [0-9]+ ;
STRING : '"' [a-zA-Z0-9]+ '"';
NEWLINE : 'r'? '\n';
WS : [ \t\\]+ -> skip ;