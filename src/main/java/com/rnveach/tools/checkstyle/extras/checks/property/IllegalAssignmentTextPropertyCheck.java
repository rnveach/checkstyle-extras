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
 * Checks specified assignment text to ensure only allowed texts are used. By
 * default only equal sign is allowed.
 */
public final class IllegalAssignmentTextPropertyCheck extends AbstractPropertyCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "illegal.assignment.text";

    /** Specify set of tokens which are allowed for assignments. */
    private int[] allowedAssignments = new int[] {
            PropertyTokenTypes.EQUALS,
    };

    @Override
    public int[] getRequiredTokens() {
        return new int[] {
                PropertyTokenTypes.ASSIGNMENT,
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
     * Setter to specify set of tokens which are allowed for assignments.
     *
     * @param allowedAssignmentsParam tokens which are allowed for assignments.
     */
    public void setAllowedAssignments(String... allowedAssignmentsParam) {
        allowedAssignments = new int[allowedAssignmentsParam.length];

        for (int index = 0; index < allowedAssignmentsParam.length; index++) {
            allowedAssignments[index] = PropertyAstUtil.getTokenId(allowedAssignmentsParam[index]);
        }
    }

    @Override
    public void visitToken(PropertyAST ast) {
        final PropertyAST firstChild = ast.getFirstChild();
        final int assignmentToken = firstChild.getType();
        boolean found = false;

        for (int allowed : allowedAssignments) {
            if (allowed == assignmentToken) {
                found = true;
                break;
            }
        }

        if (!found) {
            log(ast, MSG_KEY, firstChild.getText());
        }
    }

}
