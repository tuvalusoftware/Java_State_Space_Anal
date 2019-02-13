grammar ml;


prog : ifelsesyntax
     | token
     | oneToken
     | condition
     | expr
     ;

ifelsesyntax:
    IF condition THEN
        (ifelsesyntax | token | oneToken)
    ELSE
        (ifelsesyntax | token | oneToken)
    ;




expr: expr ('*'|'/') expr
    | expr ('+'|'-') expr
    | STRING Append STRING
    | INT
    | REAL
    | ID
    | STRING
    |'(' expr')'
    ;

condition:
    |expr (GT | LESS | EQUAL | GTandEQUAL | EQUALandLESS | NotEQUAL) expr
    |condition (AND | OR) condition
    | NOT condition
    |'(' condition ')'
    ;

oneToken:
          '1`' token
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
