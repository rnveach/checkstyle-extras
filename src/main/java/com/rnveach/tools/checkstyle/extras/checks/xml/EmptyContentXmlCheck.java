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

package com.rnveach.tools.checkstyle.extras.checks.xml;

import com.rnveach.tools.checkstyle.extras.asts.XmlAST;
import com.rnveach.tools.checkstyle.extras.checks.AbstractXmlCheck;
import com.rnveach.tools.checkstyle.extras.tokens.XmlTokenTypes;

/**
 * Checks for elements with an open and close tag but have no content in them.
 * These type of elements should be switched to be self-closing.
 */
public final class EmptyContentXmlCheck extends AbstractXmlCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "empty.content";

    @Override
    public int[] getRequiredTokens() {
        return new int[] {
                XmlTokenTypes.START_ELEMENT,
        };
    }

    @Override
    public int[] getDefaultTokens() {
        return getRequiredTokens();
    }

    @Override
    public int[] getAcceptableTokens() {
        return getRequiredTokens();
    }

    @Override
    public void visitToken(XmlAST ast) {
        if (ast.getNextSibling().getType() == XmlTokenTypes.END_ELEMENT) {
            log(ast, MSG_KEY, ast.findFirst(XmlTokenTypes.NAME).getText());
        }
    }

}
