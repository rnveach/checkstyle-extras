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

import java.util.HashSet;
import java.util.Set;

import com.rnveach.tools.checkstyle.extras.asts.PropertyAST;
import com.rnveach.tools.checkstyle.extras.checks.AbstractPropertyCheck;
import com.rnveach.tools.checkstyle.extras.tokens.PropertyTokenTypes;
import com.rnveach.tools.checkstyle.extras.utils.PropertyAstUtil;

/** Checks that no 2 keys are duplicated. */
public final class DuplicateKeyPropertyCheck extends AbstractPropertyCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "duplicate.key";

    /** Set of previous keys found in the file. */
    private Set<String> previousKeys = new HashSet<>();

    @Override
    public int[] getRequiredTokens() {
        return new int[] {
                PropertyTokenTypes.KEY,
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
    public void beginTree(PropertyAST rootAst) {
        previousKeys.clear();
    }

    @Override
    public void visitToken(PropertyAST ast) {
        final String key = PropertyAstUtil.getStringFromAst(ast);

        if (!previousKeys.add(key)) {
            log(ast, MSG_KEY, key);
        }
    }

}
