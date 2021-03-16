grammar AML;

program          : (function | expression+)+ ;
function         : IDENTIFIER COLON LEFT_PAREN params RIGHT_PAREN RIGHT_ARROW expression ;
params           : IDENTIFIER? | (IDENTIFIER COMMA)+ IDENTIFIER ;
expression       : (ifcond | assignment) SEMICOLON ;
ifcond           : IF logicEquivalence COLON thenBranch OTHERWISE COLON elseBranch ;
thenBranch       : (ifcond | composition | logicEquivalence) ;
elseBranch       : (ifcond | composition | logicEquivalence) ;
assignment       : (IDENTIFIER LEFT_ARROW assignment) | composition | logicEquivalence;
composition      : IDENTIFIER COMPOSE IDENTIFIER ;
logicEquivalence : logicImplication (EQUIVALENCE logicImplication)* ;
logicImplication : logicOr (IMPLICATION logicOr)* ;
logicOr          : logicXOr (OR logicXOr)* ;
logicXOr         : logicAnd (XOR logicAnd)* ;
logicAnd         : equality (AND equality)* ;
equality         : negation ((EQ | NEQ) negation)* ;
negation         : comparison | (NEGATION comparison) ;
comparison       : quantification | numComparison | setComparison ;

// Quantification
quantification   : universal | existential | uniqueness ;
universal        : UNIVERSAL LEFT_PAREN IDENTIFIER ELEMENT setPrimary COLON logicEquivalence RIGHT_PAREN ;
existential      : EXISTENTIAL LEFT_PAREN IDENTIFIER ELEMENT setPrimary COLON logicEquivalence RIGHT_PAREN ;
uniqueness       : UNIQUENESS LEFT_PAREN IDENTIFIER ELEMENT setPrimary COLON logicEquivalence RIGHT_PAREN ;

// Numeric rules
numComparison    : term ((LT | GT | LTE | GTE) term)* ;
term             : factor ((PLUS | MINUS) factor)* ;
factor           : exponentiation ((MULTIPLY | DIVIDE | MOD) exponentiation)* ;
exponentiation   : fraction (POWER fraction)* ;
fraction         : numUnary (FORWARD_SLASH numUnary)? ;
numUnary         : numNegation | factorial | floor | ceil | numPrimary ;
numNegation      : MINUS numPrimary ;
factorial        : numPrimary EXCLAMATION_MARK ;
floor            : LEFT_FLOOR numPrimary RIGHT_FLOOR ;
ceil             : LEFT_CEIL numPrimary RIGHT_CEIL ;
numPrimary       : call | number | IDENTIFIER | LEFT_PAREN logicEquivalence RIGHT_PAREN;
number           : NUMBER (DOT NUMBER)? ;

// Set rules
setComparison    : setOperations ((SUBSET | NOT_SUBSET | SUBSET_EQ | NOT_SUBSET_EQ | SUPERSET | NOT_SUPERSET | SUPERSET_EQ | NOT_SUPERSET_EQ) setOperations)* ;
setOperations    : setUnary ((UNION | INTERSECTION | BACKWARD_SLASH) setUnary)* ;
setUnary         : cardinality | setPrimary ;
cardinality      : BAR setPrimary BAR ;
setPrimary       : call | setLiteral | setEllipsis | setBuilder | IDENTIFIER | LEFT_PAREN logicEquivalence RIGHT_PAREN ;
setBuilder       : LEFT_BRACE IDENTIFIER ELEMENT setPrimary BAR logicEquivalence RIGHT_BRACE ;
setLiteral       : LEFT_BRACE ((logicEquivalence COMMA)* logicEquivalence)? RIGHT_BRACE ;
setEllipsis      : LEFT_BRACE numUnary COMMA ELLIPSIS COMMA numUnary RIGHT_BRACE ;

call             : IDENTIFIER LEFT_PAREN arguments RIGHT_PAREN ;
arguments        : logicEquivalence? | (logicEquivalence COMMA)+ logicEquivalence ;

COMPOSE          : '∘' ;
EQUIVALENCE      : '⇔' ;
IMPLICATION      : '⇒' ;
OR               : '∨' ;
XOR              : '⊕' ;
AND              : '∧' ;
NEGATION         : '¬' ;
ELEMENT          : '∈' ;
UNIVERSAL        : '∀' ;
EXISTENTIAL      : '∃' ;
UNIQUENESS       : EXISTENTIAL EXCLAMATION_MARK ;
POWER            : '^' ;
FORWARD_SLASH    : '/' ;
BACKWARD_SLASH   : '\\' ;
INTERSECTION     : '∩' ;
UNION            : '∪' ;
SUBSET           : '⊂' ;
NOT_SUBSET       : '⊄' ;
SUBSET_EQ        : '⊆' ;
NOT_SUBSET_EQ    : '⊈' ;
SUPERSET         : '⊃' ;
NOT_SUPERSET     : '⊅' ;
SUPERSET_EQ      : '⊇' ;
NOT_SUPERSET_EQ  : '⊉' ;
COMMA            : ',' ;
EXCLAMATION_MARK : '!' ;
LEFT_FLOOR       : '⌊' ;
RIGHT_FLOOR      : '⌋' ;
LEFT_CEIL        : '⌈' ;
RIGHT_CEIL       : '⌉' ;
DOT              : '.' ;
ELLIPSIS         : DOT DOT DOT ;
LEFT_BRACE       : '{' ;
RIGHT_BRACE      : '}' ;
LEFT_PAREN       : '(' ;
RIGHT_PAREN      : ')' ;
BAR              : '|' ;
RIGHT_ARROW      : '→' ;
LEFT_ARROW       : '←' ;
COLON            : ':' ;
SEMICOLON        : ';' ;
EQ               : '=' ;
NEQ              : '≠' ;
LT               : '<' ;
LTE              : '≤' ;
GT               : '>' ;
GTE              : '≥' ;
PLUS             : '+' ;
MINUS            : '-' ;
MULTIPLY         : '·' ;
DIVIDE           : '÷' ;
MOD              : 'mod' ;
IF               : 'if' ;
OTHERWISE        : 'otherwise' ;
NUMBER           : [0-9]+ ;
IDENTIFIER       : LETTER (LETTER | [0-9])* ;
LETTER           : [a-zA-Z] | GREEK_LETTERS | SYMBOLS ;
GREEK_LETTERS    : '\u0391'..'\u03CE' ;
SYMBOLS          : '\u22A4' | '\u22A5' | '\'' ;
WHITESPACE       : (' ' | '\t') -> skip ;
COMMENT          : '--' .*? NEWLINE -> skip;
NEWLINE          : '\r'? '\n' -> skip;