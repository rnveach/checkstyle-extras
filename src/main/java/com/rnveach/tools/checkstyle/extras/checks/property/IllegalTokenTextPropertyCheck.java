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

import java.util.Objects;
import java.util.regex.Pattern;

import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.asts.PropertyAST;
import com.rnveach.tools.checkstyle.extras.checks.AbstractPropertyCheck;
import com.rnveach.tools.checkstyle.extras.tokens.PropertyTokenTypes;

/**
 * Checks specified tokens text for matching an illegal pattern. By default no
 * tokens are specified.
 */
public final class IllegalTokenTextPropertyCheck extends AbstractPropertyCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "illegal.token.text";

    /**
     * Define the message which is used to notify about violations; if empty
     * then the default message is used.
     */
    private String message = "";

    /** The format string of the regexp. */
    private String formatString = "^$";

    /** Define the RegExp for illegal pattern. */
    private Pattern format = Pattern.compile(formatString);

    /** Control whether to ignore case when matching. */
    private boolean ignoreCase;

    @Override
    public int[] getDefaultTokens() {
        return CommonUtil.EMPTY_INT_ARRAY;
    }

    @Override
    public int[] getAcceptableTokens() {
        return new int[] {
                PropertyTokenTypes.TEXT,
        };
    }

    @Override
    public int[] getRequiredTokens() {
        return CommonUtil.EMPTY_INT_ARRAY;
    }

    @Override
    public void visitToken(PropertyAST ast) {
        final String text = ast.getText();
        if (format.matcher(text).find()) {
            String customMessage = message;
            if (customMessage.isEmpty()) {
                customMessage = MSG_KEY;
            }
            log(ast, customMessage, formatString);
        }
    }

    /**
     * Setter to define the message which is used to notify about violations; if
     * empty then the default message is used.
     *
     * @param message custom message which should be used to report about
     *        violations.
     */
    public void setMessage(String message) {
        this.message = Objects.requireNonNullElse(message, "");
    }

    /**
     * Setter to define the RegExp for illegal pattern.
     *
     * @param format a {@code String} value
     */
    public void setFormat(String format) {
        formatString = format;
        updateRegexp();
    }

    /**
     * Setter to control whether to ignore case when matching.
     *
     * @param caseInsensitive true if the match is case insensitive.
     */
    public void setIgnoreCase(boolean caseInsensitive) {
        ignoreCase = caseInsensitive;
        updateRegexp();
    }

    /**
     * Updates the {@link #format} based on the values from
     * {@link #formatString} and {@link #ignoreCase}.
     */
    private void updateRegexp() {
        final int compileFlags;
        if (ignoreCase) {
            compileFlags = Pattern.CASE_INSENSITIVE;
        }
        else {
            compileFlags = 0;
        }
        format = CommonUtil.createPattern(formatString, compileFlags);
    }

}
