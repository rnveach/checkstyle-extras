///////////////////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code and other text files for adherence to a set of rules.
// Copyright (C) 2001-2022 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
///////////////////////////////////////////////////////////////////////////////////////////////

lexer grammar XmlLanguageLexer;

tokens {
    // Root of AST
    DOCUMENT,
    PROLOG, CONTENT,
    ELEMENT, START_ELEMENT, END_ELEMENT, EMPTY_ELEMENT,
    REFERENCE, ATTRIBUTE,
    CHAR_DATA, MISC
}

@header {
import com.puppycrawl.tools.checkstyle.grammar.CrAwareLexerSimulator;
}

@lexer::members {
    /**
     * We need to create a different constructor in order to use our
     * own implementation of the LexerATNSimulator. This is the
     * reason for the unused 'crAwareConstructor' argument.
     *
     * @param input the character stream to tokenize
     * @param crAwareConstructor dummy parameter
     */
    public XmlLanguageLexer(CharStream input, boolean crAwareConstructor) {
      super(input);
      _interp = new CrAwareLexerSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }
}

//
// Everything OUTSIDE of a tag
//

COMMENT     :   '<!--' .*? '-->' ;
CDATA       :   '<![CDATA[' .*? ']]>' ;
/** Scarf all DTD stuff, Entity Declarations like <!ENTITY ...>,
 *  and Notation Declarations <!NOTATION ...>
 */
DTD         :   '<!' .*? '>'            -> skip ;
ENTITY_REF  :   '&' NAME ';' ;
CHAR_REF    :   '&#' DIGIT+ ';'
            |   '&#x' HEXDIGIT+ ';'
            ;
SEA_WS      :   (' '|'\t'|'\r'? '\n')+  -> skip ;

OPEN          :   '<'                     -> pushMode(INSIDE) ;
XML_DECL_OPEN :   '<?xml' S               -> pushMode(INSIDE) ;
SPECIAL_OPEN  :   '<?' NAME               -> more, pushMode(PROC_INSTR) ;

TEXT        :   ~[<&]+ ;        // match any 16 bit char other than < and &

//
// Everything INSIDE of a tag
//

mode INSIDE;

CLOSE         :   '>'                     -> popMode ;
XML_DECL_CLOSE:   '?>'                    -> popMode ;
SLASH_CLOSE   :   '/>'                    -> popMode ;
SLASH         :   '/' ;
EQUALS        :   '=' ;
STRING        :   '"' ~[<"]* '"'
              |   '\'' ~[<']* '\''
              ;
NAME          :   NameStartChar NameChar* ;
S             :   [ \t\r\n]               -> skip ;

fragment
HEXDIGIT    :   [a-fA-F0-9] ;

fragment
DIGIT       :   [0-9] ;

fragment
NameChar    :   NameStartChar
            |   '-' | '_' | '.' | DIGIT
            |   '\u00B7'
            |   '\u0300'..'\u036F'
            |   '\u203F'..'\u2040'
            ;

fragment
NameStartChar
            :   [:a-zA-Z]
            |   '\u2070'..'\u218F'
            |   '\u2C00'..'\u2FEF'
            |   '\u3001'..'\uD7FF'
            |   '\uF900'..'\uFDCF'
            |   '\uFDF0'..'\uFFFD'
            ;

//
// Handle <? ... ?>
//

mode PROC_INSTR;

PI          :   '?>'                    -> popMode ; // close <?...?>
IGNORE      :   .                       -> more ;
