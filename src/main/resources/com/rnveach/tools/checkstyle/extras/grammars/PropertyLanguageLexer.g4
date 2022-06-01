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

lexer grammar PropertyLanguageLexer;

tokens {
    // Root of AST
    FILE,
    ROW,
    DECL,
    KEY,ASSIGNMENT,VALUE,VALUE_TEXT,
    COMMENT
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
    public PropertyLanguageLexer(CharStream input, boolean crAwareConstructor) {
      super(input);
      _interp = new CrAwareLexerSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }
}

EQUALS         : '=' ;
COLON          : ':' ;
EXCLAMATION    : '!' ;
POUND          : '#' ;
BACKSLASH      : '\\' ;

TEXT           : [a-zA-Z0-9 @._/,%{}-]+ ;

STRING         : '"' ('""'|~'"')* '"' ; // quote-quote is an escaped quote

COMMENT_BLOCK  : ' '* (POUND | EXCLAMATION) ~[\r\n]* ;

TERMINATOR     : [\r\n]+ ;

CONTINUATION   : BACKSLASH TERMINATOR ;
