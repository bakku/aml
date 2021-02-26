grammar AML;

program          : (library | function | expression+)+ ;
library          : LIBRARY function+ END ;
function         : FUNCTION IDENTIFIER '(' params ')' expression+ END ;
params           : IDENTIFIER* ;
expression       : (ifcond | assignment SEMICOLON) ;
ifcond           : IF logicEquivalence THEN thenBranch ELSE elseBranch END ;
thenBranch       : expression+ ;
elseBranch       : expression+ ;
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

SEMICOLON  : ';' ;
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
THEN       : 'then' ;
ELSE       : 'else' ;
NUMBER     : [0-9,]+ ;
IDENTIFIER : [a-zA-Z] [a-zA-Z0-9]* ;
WHITESPACE : (' ' | '\t') -> skip ;
COMMENT    : '--' .*? NEWLINE -> skip;
NEWLINE    : '\r'? '\n' -> skip;