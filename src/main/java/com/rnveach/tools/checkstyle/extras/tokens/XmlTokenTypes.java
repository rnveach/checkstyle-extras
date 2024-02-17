///////////////////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code and other text files for adherence to a set of rules.
// Copyright (C) 2001-2024 the original author or authors.
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

package com.rnveach.tools.checkstyle.extras.tokens;

import com.rnveach.tools.checkstyle.extras.grammars.XmlLanguageLexer;

/**
 * Contains the constants for all the tokens contained in the AST.
 *
 * <p>
 * Implementation detail: This class has been introduced to break the circular
 * dependency between packages.
 * </p>
 */
public final class XmlTokenTypes {

    /** Document. */
    public static final int DOCUMENT = XmlLanguageLexer.DOCUMENT;

    /** Prolog. */
    public static final int PROLOG = XmlLanguageLexer.PROLOG;

    /** Content. */
    public static final int CONTENT = XmlLanguageLexer.CONTENT;

    /** Element. */
    public static final int ELEMENT = XmlLanguageLexer.ELEMENT;

    /** Start element. */
    public static final int START_ELEMENT = XmlLanguageLexer.START_ELEMENT;

    /** End element. */
    public static final int END_ELEMENT = XmlLanguageLexer.END_ELEMENT;

    /** Empty element. */
    public static final int EMPTY_ELEMENT = XmlLanguageLexer.EMPTY_ELEMENT;

    /** Reference. */
    public static final int REFERENCE = XmlLanguageLexer.REFERENCE;

    /** Attribute. */
    public static final int ATTRIBUTE = XmlLanguageLexer.ATTRIBUTE;

    /** Char data. */
    public static final int CHAR_DATA = XmlLanguageLexer.CHAR_DATA;

    /** Misc. */
    public static final int MISC = XmlLanguageLexer.MISC;

    /** Comment. */
    public static final int COMMENT = XmlLanguageLexer.COMMENT;

    /** Cdata. */
    public static final int CDATA = XmlLanguageLexer.CDATA;

    /** DTD. */
    public static final int DTD = XmlLanguageLexer.DTD;

    /** Entity ref. */
    public static final int ENTITY_REF = XmlLanguageLexer.ENTITY_REF;

    /** Char ref. */
    public static final int CHAR_REF = XmlLanguageLexer.CHAR_REF;

    /** Sea ws. */
    public static final int SEA_WS = XmlLanguageLexer.SEA_WS;

    /** Open. */
    public static final int OPEN = XmlLanguageLexer.OPEN;

    /** Xml decl open. */
    public static final int XML_DECL_OPEN = XmlLanguageLexer.XML_DECL_OPEN;

    /** Text. */
    public static final int TEXT = XmlLanguageLexer.TEXT;

    /** Close. */
    public static final int CLOSE = XmlLanguageLexer.CLOSE;

    /** Xml decl close. */
    public static final int XML_DECL_CLOSE = XmlLanguageLexer.XML_DECL_CLOSE;

    /** Slash close. */
    public static final int SLASH_CLOSE = XmlLanguageLexer.SLASH_CLOSE;

    /** Slash. */
    public static final int SLASH = XmlLanguageLexer.SLASH;

    /** Equals. */
    public static final int EQUALS = XmlLanguageLexer.EQUALS;

    /** String. */
    public static final int STRING = XmlLanguageLexer.STRING;

    /** Name. */
    public static final int NAME = XmlLanguageLexer.NAME;

    /** S. */
    public static final int S = XmlLanguageLexer.S;

    /** Pi. */
    public static final int PI = XmlLanguageLexer.PI;

    /** Stop instances being created. **/
    private XmlTokenTypes() {
    }

}
