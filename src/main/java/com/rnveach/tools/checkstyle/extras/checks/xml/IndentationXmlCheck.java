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

package com.rnveach.tools.checkstyle.extras.checks.xml;

import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.asts.XmlAST;
import com.rnveach.tools.checkstyle.extras.checks.AbstractXmlCheck;
import com.rnveach.tools.checkstyle.extras.tokens.XmlTokenTypes;
import com.rnveach.tools.checkstyle.extras.utils.XmlAstUtil;

/** Checks correct indentation of XML file. */
public final class IndentationXmlCheck extends AbstractXmlCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "indentation.error";

    /** Default indentation amount. */
    private static final int DEFAULT_INDENTATION = 4;

    /**
     * Enforce strict indentation.
     */
    private boolean forceStrictIndentation;

    /**
     * Specify how far continuation line should be indented when line is
     * wrapped.
     */
    private int wrappingIndentation = DEFAULT_INDENTATION;

    /**
     * Specify how far next line should be indented when moving inside a nested
     * token.
     */
    private int nestedIndentation = DEFAULT_INDENTATION;

    /** Variable used to tracked expected column position for indentation. */
    private int expectedColumnNo;
    /**
     * Variable used to track the last line processed to avoid repeating lines.
     */
    private int lastLineNo;

    /**
     * Setter to enforce strict indentation.
     *
     * @param forceStrictIndentation user's value of forceStrictCondition.
     */
    public void setForceStrictIndentation(boolean forceStrictIndentation) {
        this.forceStrictIndentation = forceStrictIndentation;
    }

    /**
     * Setter to specify how far continuation line should be indented when line
     * is wrapped.
     *
     * @param wrappingIndentation user's value of wrappingIndentation.
     */
    public void setWrappingIndentation(int wrappingIndentation) {
        this.wrappingIndentation = wrappingIndentation;
    }

    /**
     * Setter to specify how far next line should be indented when moving inside
     * a nested token.
     *
     * @param nestedIndentation user's value of nestedIndentation.
     */
    public void setNestedIndentation(int nestedIndentation) {
        this.nestedIndentation = nestedIndentation;
    }

    @Override
    public int[] getRequiredTokens() {
        return XmlAstUtil.getAllTokenIds();
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
    public void beginTree(XmlAST rootAst) {
        expectedColumnNo = 0;
        lastLineNo = -1;
    }

    @Override
    public void visitToken(XmlAST ast) {
        final int increaseBefore = getIndentationIncreaseBefore(ast);
        if (increaseBefore != 0) {
            expectedColumnNo += increaseBefore;
        }

        if (ast.getLineNo() != lastLineNo) {
            // on a new line

            // ignore text, and ASTs that don't start the line
            if (ast.getType() != XmlTokenTypes.TEXT && getLineStart(ast) == ast.getColumnNo()) {
                final int actualColumnNo = getExpandedTabsColumnNo(ast);

                if (forceStrictIndentation && actualColumnNo != expectedColumnNo
                        || !forceStrictIndentation && actualColumnNo < expectedColumnNo) {
                    log(ast, MSG_KEY, actualColumnNo, expectedColumnNo);
                }
            }

            lastLineNo = ast.getLineNo();
        }

        final int increaseAfter = getIndentationIncreaseAfter(ast);
        if (increaseAfter != 0) {
            expectedColumnNo += increaseAfter;
        }
    }

    @Override
    public void leaveToken(XmlAST ast) {
        final int decrease = getIndentationDecrease(ast);
        if (decrease != 0) {
            expectedColumnNo -= decrease;
        }
    }

    /**
     * Retrieve the indentation level increase before checking the indentation.
     *
     * @param ast The AST to examine.
     * @return The increase in the indentation level.
     */
    private int getIndentationIncreaseBefore(XmlAST ast) {
        final int result;

        switch (ast.getType()) {
        case XmlTokenTypes.CONTENT:
            result = nestedIndentation;
            break;
        default:
            result = 0;
            break;
        }

        return result;
    }

    /**
     * Retrieve the indentation level increase after checking the indentation.
     *
     * @param ast The AST to examine.
     * @return The increase in the indentation level.
     */
    private int getIndentationIncreaseAfter(XmlAST ast) {
        final int result;

        switch (ast.getType()) {
        case XmlTokenTypes.XML_DECL_OPEN:
        case XmlTokenTypes.OPEN:
            result = wrappingIndentation;
            break;
        default:
            result = 0;
            break;
        }

        return result;
    }

    /**
     * Retrieve the indentation level decrease after we pass the {@code ast}.
     *
     * @param ast The AST to examine.
     * @return The decrease in the indentation level.
     */
    private int getIndentationDecrease(XmlAST ast) {
        final int result;

        switch (ast.getType()) {
        case XmlTokenTypes.XML_DECL_CLOSE:
        case XmlTokenTypes.CLOSE:
        case XmlTokenTypes.SLASH_CLOSE:
            result = wrappingIndentation;
            break;
        case XmlTokenTypes.CONTENT:
            result = nestedIndentation;
            break;
        default:
            result = 0;
            break;
        }

        return result;
    }

    /**
     * Get the start of the line for the given expression.
     *
     * @param ast the expression to find the start of the line for
     * @return the start of the line for the given expression
     */
    private int getLineStart(XmlAST ast) {
        final String line = getLine(ast.getLineNo() - 1);
        return getLineStart(line);
    }

    /**
     * Get the start of the specified line.
     *
     * @param line the specified line number
     * @return the start of the specified line
     */
    private static int getLineStart(String line) {
        int index = 0;
        while (Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        return index;
    }

    /**
     * Get the column number for the start of a given line, expanding tabs out
     * into spaces in the process.
     *
     * @param ast the node to find the start of
     * @return the column number for the start of the line
     */
    private int getExpandedTabsColumnNo(XmlAST ast) {
        final String line = getLine(ast.getLineNo() - 1);

        return CommonUtil.lengthExpandedTabs(line, ast.getColumnNo(), getTabWidth());
    }

}
