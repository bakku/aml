grammar AML;

program          : (library | function | (expression NEWLINE+))+ ;
library          : LIBRARY NEWLINE? function+ END NEWLINE?;
function         : FUNCTION IDENTIFIER '(' params ')' NEWLINE* (expression NEWLINE+)+ END NEWLINE*;
params           : IDENTIFIER* ;
expression       : (ifcond | assignment) ;
ifcond           : IF logicOr expression+ ELSE expression+ END ;
assignment       : (IDENTIFIER '←' expression) | logicEquivalence ;
logicEquivalence : logicImplication ('⇔' logicImplication)* ;
logicImplication : logicOr ('⇒' logicOr)* ;
logicOr          : logicXOr ('∨' logicXOr)* ;
logicXOr         : logicAnd ('⊕' logicAnd)* ;
logicAnd         : equality ('∧' equality)* ;
equality         : comparison ((EQ | NEQ) comparison)* ;
comparison       : quantification | numComparison | setComparison ;

// Quantification
quantification   : universal | existential | uniqueness ;
universal        : '∀' '(' IDENTIFIER '∈' IDENTIFIER ':' logicEquivalence ')' ;
existential      : '∃' '(' IDENTIFIER '∈' IDENTIFIER ':' logicEquivalence ')' ;
uniqueness       : '∃!' '(' IDENTIFIER '∈' IDENTIFIER ':' logicEquivalence ')' ;

// Numeric rules
numComparison    : term ((LT | GT | LTE | GTE) term)* ;
term             : factor ((PLUS | MINUS) factor)* ;
factor           : exponentiation ((MULTIPLY | DIVIDE | MOD) exponentiation)* ;
exponentiation   : fraction ('^' fraction)* ;
fraction         : numUnary ('/' numUnary)? ;
numUnary         : numNegation | factorial | floor | ceil | numPrimary ;
numNegation      : '-' numPrimary ;
factorial        : numPrimary '!' ;
floor            : '⌊' numPrimary '⌋' ;
ceil             : '⌈' numPrimary '⌉' ;
numPrimary       : call | number | IDENTIFIER | '(' expression ')' ;
number           : NUMBER ('.' NUMBER)? ;

// Set rules
setComparison    : setOperations (('⊂' | '⊄' | '⊆' | '⊈' | '⊃' | '⊅' | '⊇' | '⊉') setOperations)* ;
setOperations    : setUnary (('∪' | '∩' | '\\') setUnary)* ;
setUnary         : cardinality | setPrimary ;
cardinality      : '|' setPrimary '|' ;
setPrimary       : call | setLiteral | setEllipsis | IDENTIFIER | '(' expression ')' ;
setLiteral       : '{' ((NUMBER ',')* NUMBER)? '}' ;
setEllipsis      : '{' NUMBER ',' '...' ',' NUMBER '}' ;

call             : IDENTIFIER '(' params ')' ;

EQ         : '=' ;
NEQ        : '≠' ;
LT         : '<' ;
LTE        : '≤' ;
GT         : '>' ;
GTE        : '≥' ;
PLUS       : '+' ;
MINUS      : '-' ;
MULTIPLY   : '·' ;
DIVIDE     : '÷' ;
MOD        : 'mod' ;
LIBRARY    : 'library' ;
END        : 'end' ;
FUNCTION   : 'function' ;
IF         : 'if' ;
ELSE       : 'else' ;
NUMBER     : [0-9,]+ ;
IDENTIFIER : [a-zA-Z] [a-zA-Z0-9]* ;
WHITESPACE : (' ' | '\t') -> skip ;
COMMENT    : '--' .*? NEWLINE -> skip;
NEWLINE    : '\r'? '\n';