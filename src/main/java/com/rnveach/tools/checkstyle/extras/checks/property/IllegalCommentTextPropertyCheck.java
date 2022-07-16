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

package com.rnveach.tools.checkstyle.extras.checks.property;

import com.rnveach.tools.checkstyle.extras.asts.PropertyAST;
import com.rnveach.tools.checkstyle.extras.checks.AbstractPropertyCheck;
import com.rnveach.tools.checkstyle.extras.tokens.PropertyTokenTypes;
import com.rnveach.tools.checkstyle.extras.utils.PropertyAstUtil;

/**
 * Checks specified comment text to ensure only allowed texts are used. By
 * default only pound sign is allowed.
 */
public final class IllegalCommentTextPropertyCheck extends AbstractPropertyCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "illegal.comment.text";

    /** Specify set of tokens which are allowed for comments. */
    private int[] allowedComments = new int[] {
            PropertyTokenTypes.POUND,
    };

    @Override
    public int[] getRequiredTokens() {
        return new int[] {
                PropertyTokenTypes.COMMENT,
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

    /**
     * Setter to specify set of tokens which are allowed for comments.
     *
     * @param allowedCommentsParam tokens which are allowed for comments.
     */
    public void setAllowedAssignments(String... allowedCommentsParam) {
        allowedComments = new int[allowedCommentsParam.length];

        for (int index = 0; index < allowedCommentsParam.length; index++) {
            allowedComments[index] = PropertyAstUtil.getTokenId(allowedCommentsParam[index]);
        }
    }

    @Override
    public void visitToken(PropertyAST ast) {
        final PropertyAST firstChild = ast.getFirstChild();
        final int commentToken = firstChild.getType();
        boolean found = false;

        for (int allowed : allowedComments) {
            if (allowed == commentToken) {
                found = true;
                break;
            }
        }

        if (!found) {
            log(ast, MSG_KEY, firstChild.getText());
        }
    }

}
