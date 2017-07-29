# JsonParser
This is mainly designed to validate json file and raise appropriate error messages and exceptions.
Java is used as programming language to develop this project.

JSON is built on two structures:

>   A collection of name/value pairs. In various languages, this is realized as an object, record, struct, dictionary, hash table, keyed list, or associative array.
>   An ordered list of values. In most languages, this is realized as an array, vector, list, or sequence.

GRAMMAR of json file:

object->{}|{ members } 
members->pair|pair , members
pair->string : value
array->[]|[ elements ]
elements->value|value , elements
value->string|number|object|array|true|false|null
string->""|"chars"
chars->char|char chars
char->any-Unicode-character-except-"-or-\-or-control-character |
    \" |
    \\ |
    \/ |
    \b |
    \f |
    \n |
    \r |
    \t
   
number->int|int frac|int exp|int frac exp 
int->digit|digit1-9 digits
   
frac->.digits
exp->edigits
digits->digit|digit digits
e-> e|e+|e-|E|E+|E-

