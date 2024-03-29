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

package com.rnveach.tools.checkstyle.extras.asts;

/**
 * An interface of AST nodes for traversing trees generated from the XML file.
 * The main purpose of this interface is to abstract away ANTLR specific classes
 * from API package so other libraries won't require it.
 *
 * @see <a href="https://www.antlr.org/">ANTLR Website</a>
 */
public interface XmlAST {

    /**
     * Gets line number.
     *
     * @return the line number
     */
    int getLineNo();

    /**
     * Gets column number.
     *
     * @return the column number
     */
    int getColumnNo();

    /**
     * Gets the type of this AST.
     *
     * @return the type.
     */
    int getType();

    /**
     * Gets the text of this AST.
     *
     * @return the text.
     */
    String getText();

    /**
     * Get the next sibling in line after this one.
     *
     * @return the next sibling or null if none.
     */
    XmlAST getNextSibling();

    /**
     * Returns the previous sibling or null if no such sibling exists.
     *
     * @return the previous sibling or null if no such sibling exists.
     */
    XmlAST getPreviousSibling();

    /**
     * Returns whether this AST has any children.
     *
     * @return {@code true} if this AST has any children.
     */
    boolean hasChildren();

    /**
     * Returns the number of child nodes one level below this node. That is,
     * does not recurse down the tree.
     *
     * @return the number of child nodes
     */
    int getChildCount();

    /**
     * Returns the number of direct child ASTs that have the specified type.
     *
     * @param type the AST type to match
     * @return the number of matching AST
     */
    int getChildCount(int type);

    /**
     * Get the first child of this AST.
     *
     * @return the first child or null if none.
     */
    XmlAST getFirstChild();

    /**
     * Gets the last child node.
     *
     * @return the last child node
     */
    XmlAST getLastChild();

    /**
     * Returns the parent AST.
     *
     * @return the parent AST
     */
    XmlAST getParent();

    /**
     * Returns the first child AST that makes a specified type.
     *
     * @param type the AST type to match
     * @return the matching AST, or null if no match
     */
    XmlAST findFirst(int type);

}
