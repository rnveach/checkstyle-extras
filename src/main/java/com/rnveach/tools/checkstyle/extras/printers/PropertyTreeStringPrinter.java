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

package com.rnveach.tools.checkstyle.extras.printers;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.rnveach.tools.checkstyle.extras.asts.PropertyAST;
import com.rnveach.tools.checkstyle.extras.parsers.PropertyParser;
import com.rnveach.tools.checkstyle.extras.utils.PropertyAstUtil;

/** Class for printing AST to String. */
public final class PropertyTreeStringPrinter {

    /** Newline pattern. */
    private static final Pattern NEWLINE = Pattern.compile("\n");
    /** Return pattern. */
    private static final Pattern RETURN = Pattern.compile("\r");
    /** Tab pattern. */
    private static final Pattern TAB = Pattern.compile("\t");

    /** OS specific line separator. */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /** Prevent instances. */
    private PropertyTreeStringPrinter() {
        // no code
    }

    /**
     * Parse a file and print the parse tree.
     *
     * @param file the file to print.
     * @return the AST of the file in String form.
     * @throws IOException if the file could not be read.
     * @throws CheckstyleException if the file fails to parse.
     */
    public static String printFileAst(File file) throws IOException, CheckstyleException {
        return printTree(PropertyParser.parseFile(file));
    }

    /**
     * Prints full tree of the PropertyAST.
     *
     * @param ast root PropertyAST
     * @return Full tree
     */
    private static String printTree(PropertyAST ast) {
        final StringBuilder messageBuilder = new StringBuilder(1024);
        PropertyAST node = ast;
        while (node != null) {
            messageBuilder.append(getIndentation(node)).append(getNodeInfo(node))
                    .append(LINE_SEPARATOR);
            messageBuilder.append(printTree(node.getFirstChild()));
            node = node.getNextSibling();
        }
        return messageBuilder.toString();
    }

    /**
     * Print branch info from root down to given {@code node}.
     *
     * @param node last item of the branch
     * @return branch as string
     */
    public static String printBranch(PropertyAST node) {
        final String result;
        if (node == null) {
            result = "";
        }
        else {
            result = printBranch(node.getParent()) + getIndentation(node) + getNodeInfo(node)
                    + LINE_SEPARATOR;
        }
        return result;
    }

    /**
     * Get string representation of the node as token name, node text, line
     * number and column number.
     *
     * @param node PropertyAST
     * @return node info
     */
    private static String getNodeInfo(PropertyAST node) {
        return PropertyAstUtil.getTokenName(node.getType()) + " -> "
                + escapeAllControlChars(node.getText()) + " [" + node.getLineNo() + ':'
                + node.getColumnNo() + ']';
    }

    /**
     * Get indentation for an AST node.
     *
     * @param ast the AST to get the indentation for.
     * @return the indentation in String format.
     */
    private static String getIndentation(PropertyAST ast) {
        final boolean isLastChild = ast.getNextSibling() == null;
        PropertyAST node = ast;
        final StringBuilder indentation = new StringBuilder(1024);
        while (node.getParent() != null) {
            node = node.getParent();
            if (node.getParent() == null) {
                if (isLastChild) {
                    // only ASCII symbols must be used due to
                    // problems with running tests on Windows
                    indentation.append("`--");
                }
                else {
                    indentation.append("|--");
                }
            }
            else {
                if (node.getNextSibling() == null) {
                    indentation.insert(0, "    ");
                }
                else {
                    indentation.insert(0, "|   ");
                }
            }
        }
        return indentation.toString();
    }

    /**
     * Replace all control chars with escaped symbols.
     *
     * @param text the String to process.
     * @return the processed String with all control chars escaped.
     */
    private static String escapeAllControlChars(String text) {
        final String textWithoutNewlines = NEWLINE.matcher(text).replaceAll("\\\\n");
        final String textWithoutReturns = RETURN.matcher(textWithoutNewlines).replaceAll("\\\\r");
        return TAB.matcher(textWithoutReturns).replaceAll("\\\\t");
    }

}
