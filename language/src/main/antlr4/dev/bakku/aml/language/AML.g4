grammar AML;

program      : expression+ ;
expression   : intersection ;
intersection : set '∩' set ;
set          : '{' (NUMBER ',')* NUMBER '}' ;

NUMBER     : [0-9]+ ;
WHITESPACE : ' ' -> skip ;