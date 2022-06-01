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

package com.rnveach.tools.checkstyle.extras.tokens;

import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageLexer;

/**
 * Contains the constants for all the tokens contained in the AST.
 *
 * <p>
 * Implementation detail: This class has been introduced to break the circular
 * dependency between packages.
 * </p>
 */
public final class PropertyTokenTypes {

    /** File. */
    public static final int FILE = PropertyLanguageLexer.FILE;

    /** Row. */
    public static final int ROW = PropertyLanguageLexer.ROW;

    /** Decl. */
    public static final int DECL = PropertyLanguageLexer.DECL;

    /** Key. */
    public static final int KEY = PropertyLanguageLexer.KEY;

    /** Assignment. */
    public static final int ASSIGNMENT = PropertyLanguageLexer.ASSIGNMENT;

    /** Value. */
    public static final int VALUE = PropertyLanguageLexer.VALUE;

    /** Value. */
    public static final int VALUE_TEXT = PropertyLanguageLexer.VALUE_TEXT;

    /** Comment. */
    public static final int COMMENT = PropertyLanguageLexer.COMMENT;

    /** Equals. */
    public static final int EQUALS = PropertyLanguageLexer.EQUALS;

    /** Colon. */
    public static final int COLON = PropertyLanguageLexer.COLON;

    /** Exclamation. */
    public static final int EXCLAMATION = PropertyLanguageLexer.EXCLAMATION;

    /** Pound. */
    public static final int POUND = PropertyLanguageLexer.POUND;

    /** Backlash. */
    public static final int BACKSLASH = PropertyLanguageLexer.BACKSLASH;

    /** Double backlash. */
    public static final int DOUBLE_BACKSLASH = PropertyLanguageLexer.DOUBLE_BACKSLASH;

    /** Text. */
    public static final int TEXT = PropertyLanguageLexer.TEXT;

    /** String. */
    public static final int STRING = PropertyLanguageLexer.STRING;

    /** Continuation. */
    public static final int CONTINUATION = PropertyLanguageLexer.CONTINUATION;

    /** Terminator. */
    public static final int TERMINATOR = PropertyLanguageLexer.TERMINATOR;

    /** Comment block. */
    public static final int COMMENT_BLOCK = PropertyLanguageLexer.COMMENT_BLOCK;

    /** Stop instances being created. **/
    private PropertyTokenTypes() {
    }

}
