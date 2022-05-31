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

parser grammar XmlLanguageParser;

options { tokenVocab=XmlLanguageLexer; }

@parser::members {

    /**
     * This is the number of files to parse before clearing the parser's
     * DFA states. This number can have a significant impact on performance;
     * we have found 500 files to be a good balance between parser speed and
     * memory usage. This field must be public in order to be accessed and
     * used for {@link XmlLanguageParser#XmlLanguageParser(TokenStream, int)}
     * generated constructor.
     */
    public static final int CLEAR_DFA_LIMIT = 500;

    static int fileCounter = 0;

    /**
     * We create a custom constructor so that we can clear the DFA
     * states upon instantiation of XmlLanguageParser.
     *
     * @param input the token stream to parse
     * @param clearDfaLimit this is the number of files to parse before clearing
     *         the parser's DFA states. This number can have a significant impact
     *         on performance; more frequent clearing of DFA states can lead to
     *         slower parsing but lower memory usage. Conversely, not clearing the
     *         DFA states at all can lead to enormous memory usage, but may also
     *         have a negative effect on memory usage from higher garbage collector
     *         activity.
     */
    public XmlLanguageParser(TokenStream input, int clearDfaLimit) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN , _decisionToDFA, _sharedContextCache);
        fileCounter++;
        if (fileCounter > clearDfaLimit) {
            _interp.clearDFA();
            fileCounter = 0;
        }
    }
}

// https://cs.lmu.edu/~ray/notes/xmlgrammar/

document     :   prolog? misc* element misc* EOF ;

prolog       :   XML_DECL_OPEN attribute* XML_DECL_CLOSE ;

content      :   chardata?
                ((element | reference | CDATA | PI | COMMENT) chardata?)* ;

element      :   startElement endElement
             |   startElement content endElement
             |   emptyElement
             ;

startElement : '<' NAME attribute* '>' ;

endElement   : '<' '/' NAME '>' ;

emptyElement : '<' NAME attribute* '/>' ;

reference    :   ENTITY_REF | CHAR_REF ;

attribute    :   NAME '=' STRING ;

//
// All text that is not markup constitutes the character data of the document.
//

chardata     :   TEXT | SEA_WS ;

misc         :   COMMENT | PI | SEA_WS ;
