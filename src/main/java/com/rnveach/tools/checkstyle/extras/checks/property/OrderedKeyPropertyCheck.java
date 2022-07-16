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

/** Checks that key names are specified . */
public final class OrderedKeyPropertyCheck extends AbstractPropertyCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "unordered.key";

    /** Control whether to ignore case when matching. */
    private boolean ignoreCase;

    /** Previous key found in the file. */
    private String previousKey;

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

    /**
     * Setter to control whether to ignore case when matching.
     *
     * @param ignoreCase {@code true} if the match is case-insensitive.
     */
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    @Override
    public void beginTree(PropertyAST rootAst) {
        previousKey = null;
    }

    @Override
    public void visitToken(PropertyAST ast) {
        final String key = PropertyAstUtil.getStringFromAst(ast);

        if (previousKey != null) {
            final int difference;

            if (ignoreCase) {
                difference = previousKey.compareToIgnoreCase(key);
            }
            else {
                difference = previousKey.compareTo(key);
            }

            if (difference > 0) {
                log(ast, MSG_KEY, key, previousKey);
            }
        }

        previousKey = key;
    }

}
