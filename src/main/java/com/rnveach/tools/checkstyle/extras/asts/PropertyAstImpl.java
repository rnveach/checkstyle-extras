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

package com.rnveach.tools.checkstyle.extras.asts;

import org.antlr.v4.runtime.Token;

/**
 * The implementation of {@link PropertyAST}. This should only be directly used
 * to create custom AST nodes and in 'PropertyAstVisitor.java'.
 */
public final class PropertyAstImpl implements PropertyAST {

    /** Constant to indicate if not calculated the child count. */
    private static final int NOT_INITIALIZED = Integer.MIN_VALUE;

    /** The line number. **/
    private int lineNo = NOT_INITIALIZED;
    /** The column number. **/
    private int columnNo = NOT_INITIALIZED;
    /** The type of this PropertyAST. */
    private int type;
    /** Text of this PropertyAST. */
    private String text;

    /** Number of children. */
    private int childCount = NOT_INITIALIZED;

    /** First sibling of this PropertyAST. */
    private PropertyAstImpl nextSibling;
    /** Previous sibling. */
    private PropertyAstImpl previousSibling;

    /** First child of this PropertyAST. */
    private PropertyAstImpl firstChild;
    /** The parent token. */
    private PropertyAstImpl parent;

    /**
     * Initializes this PropertyAstImpl.
     *
     * @param tokenType the type of this PropertyAstImpl
     * @param tokenText the text of this PropertyAstImpl
     */
    public void initialize(int tokenType, String tokenText) {
        type = tokenType;
        text = tokenText;
    }

    /**
     * Initializes this PropertyAstImpl.
     *
     * @param token the token to generate this PropertyAstImpl from
     */
    public void initialize(Token token) {
        lineNo = token.getLine();
        columnNo = token.getCharPositionInLine();
        type = token.getType();
        text = token.getText();
    }

    @Override
    public int getLineNo() {
        int resultNo = -1;

        if (lineNo == NOT_INITIALIZED) {
            resultNo = firstChild.getLineNo();
        }
        if (resultNo == -1) {
            resultNo = lineNo;
        }
        return resultNo;
    }

    /**
     * Set line number.
     *
     * @param lineNo line number.
     */
    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    @Override
    public int getColumnNo() {
        int resultNo = -1;

        if (columnNo == NOT_INITIALIZED) {
            resultNo = firstChild.getColumnNo();
        }
        if (resultNo == -1) {
            resultNo = columnNo;
        }
        return resultNo;
    }

    /**
     * Set column number.
     *
     * @param columnNo column number.
     */
    public void setColumnNo(int columnNo) {
        this.columnNo = columnNo;
    }

    @Override
    public int getType() {
        return type;
    }

    /**
     * Sets the type of this AST.
     *
     * @param type the token type of this PropertyAstImpl
     */
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getText() {
        return text;
    }

    /**
     * Sets the text for this PropertyAstImpl.
     *
     * @param text the text field of this PropertyAstImpl
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Add next sibling, pushes other siblings back.
     *
     * @param ast PropertyAST object.
     */
    public void addNextSibling(PropertyAST ast) {
        clearChildCountCache(parent);
        if (ast != null) {
            // parent is set in setNextSibling
            final PropertyAstImpl sibling = nextSibling;
            final PropertyAstImpl astImpl = (PropertyAstImpl) ast;

            if (sibling != null) {
                astImpl.setNextSibling(sibling);
                sibling.previousSibling = astImpl;
            }

            astImpl.previousSibling = this;
            setNextSibling(astImpl);
        }
    }

    /**
     * Sets the next sibling of this AST.
     *
     * @param nextSibling the PropertyAST to set as sibling
     */
    public void setNextSibling(PropertyAST nextSibling) {
        clearChildCountCache(parent);
        this.nextSibling = (PropertyAstImpl) nextSibling;
        if (nextSibling != null && parent != null) {
            ((PropertyAstImpl) nextSibling).setParent(parent);
        }
        if (nextSibling != null) {
            ((PropertyAstImpl) nextSibling).previousSibling = this;
        }
    }

    @Override
    public PropertyAstImpl getNextSibling() {
        return nextSibling;
    }

    /**
     * Add previous sibling.
     *
     * @param ast PropertyAST object.
     */
    public void addPreviousSibling(PropertyAST ast) {
        clearChildCountCache(parent);
        if (ast != null) {
            // parent is set in setNextSibling or parent.setFirstChild
            final PropertyAstImpl previousSiblingNode = previousSibling;
            final PropertyAstImpl astImpl = (PropertyAstImpl) ast;

            if (previousSiblingNode != null) {
                astImpl.previousSibling = previousSiblingNode;
                previousSiblingNode.setNextSibling(astImpl);
            }
            else if (parent != null) {
                parent.setFirstChild(astImpl);
            }

            astImpl.setNextSibling(this);
            previousSibling = astImpl;
        }
    }

    @Override
    public PropertyAST getPreviousSibling() {
        return previousSibling;
    }

    /**
     * Adds a new child to the current AST.
     *
     * @param child PropertyAST to add as child
     */
    public void addChild(PropertyAST child) {
        clearChildCountCache(this);
        if (child != null) {
            final PropertyAstImpl astImpl = (PropertyAstImpl) child;
            astImpl.setParent(this);
            astImpl.previousSibling = (PropertyAstImpl) getLastChild();
        }
        PropertyAST temp = firstChild;
        if (temp == null) {
            firstChild = (PropertyAstImpl) child;
        }
        else {
            while (temp.getNextSibling() != null) {
                temp = temp.getNextSibling();
            }

            ((PropertyAstImpl) temp).setNextSibling(child);
        }
    }

    /**
     * Removes a child from the current AST.
     *
     * @param child PropertyAST to remove as child
     */
    public void removeChild(PropertyAST child) {
        clearChildCountCache(this);
        if (firstChild == child) {
            firstChild = (PropertyAstImpl) child.getNextSibling();
        }
        else {
            ((PropertyAstImpl) child.getPreviousSibling()).setNextSibling(child.getNextSibling());
        }

    }

    /**
     * Sets the first child of this AST.
     *
     * @param firstChild the PropertyAST to set as first child
     */
    public void setFirstChild(PropertyAST firstChild) {
        clearChildCountCache(this);
        this.firstChild = (PropertyAstImpl) firstChild;
        if (firstChild != null) {
            ((PropertyAstImpl) firstChild).setParent(this);
        }
    }

    @Override
    public boolean hasChildren() {
        return firstChild != null;
    }

    @Override
    public int getChildCount() {
        // lazy init
        if (childCount == NOT_INITIALIZED) {
            childCount = 0;
            PropertyAST child = firstChild;

            while (child != null) {
                childCount += 1;
                child = child.getNextSibling();
            }
        }
        return childCount;
    }

    @Override
    public int getChildCount(int tokenType) {
        int count = 0;
        for (PropertyAST ast = firstChild; ast != null; ast = ast.getNextSibling()) {
            if (ast.getType() == tokenType) {
                count++;
            }
        }
        return count;
    }

    @Override
    public PropertyAstImpl getFirstChild() {
        return firstChild;
    }

    @Override
    public PropertyAST getLastChild() {
        PropertyAstImpl ast = firstChild;
        while (ast != null && ast.nextSibling != null) {
            ast = ast.nextSibling;
        }
        return ast;
    }

    @Override
    public PropertyAST getParent() {
        return parent;
    }

    /**
     * Set the parent token.
     *
     * @param parent the parent token
     */
    private void setParent(PropertyAstImpl parent) {
        PropertyAstImpl instance = this;
        do {
            instance.parent = parent;
            instance = instance.nextSibling;
        } while (instance != null);
    }

    @Override
    public PropertyAST findFirst(int findType) {
        PropertyAST returnValue = null;

        for (PropertyAST ast = firstChild; ast != null; ast = ast.getNextSibling()) {
            if (ast.getType() == findType) {
                returnValue = ast;
                break;
            }
        }

        return returnValue;
    }

    /**
     * Clears the child count for the ast instance.
     *
     * @param ast The ast to clear.
     */
    private static void clearChildCountCache(PropertyAstImpl ast) {
        if (ast != null) {
            ast.childCount = NOT_INITIALIZED;
        }
    }

    @Override
    public String toString() {
        return text + "[" + getLineNo() + "x" + getColumnNo() + "]";
    }

}
