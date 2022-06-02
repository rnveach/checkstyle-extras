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

package com.rnveach.tools.checkstyle.extras.checks.property;

import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.asts.PropertyAST;
import com.rnveach.tools.checkstyle.extras.checks.AbstractPropertyCheck;
import com.rnveach.tools.checkstyle.extras.tokens.PropertyTokenTypes;
import com.rnveach.tools.checkstyle.extras.utils.PropertyAstUtil;
import com.rnveach.tools.checkstyle.extras.utils.StringUtil;

/** Checks for illegal tokens. */
public final class IllegalTokenPropertyCheck extends AbstractPropertyCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "illegal.token";

    @Override
    public int[] getDefaultTokens() {
        return CommonUtil.EMPTY_INT_ARRAY;
    }

    @Override
    public int[] getAcceptableTokens() {
        return PropertyAstUtil.getAllTokenIds();
    }

    @Override
    public int[] getRequiredTokens() {
        return CommonUtil.EMPTY_INT_ARRAY;
    }

    @Override
    public void visitToken(PropertyAST ast) {
        log(ast, MSG_KEY, convertToString(ast));
    }

    /**
     * Converts given AST node to string representation.
     *
     * @param ast node to be represented as string
     * @return string representation of AST node
     */
    private static String convertToString(PropertyAST ast) {
        final String tokenText;
        switch (ast.getType()) {
        case PropertyTokenTypes.COMMENT:
        case PropertyTokenTypes.COMMENT_BLOCK:
            tokenText = StringUtil.escapeAllControlChars(ast.getText());
            break;
        default:
            tokenText = ast.getText();
            break;
        }
        return tokenText;
    }

}
