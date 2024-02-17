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

import org.antlr.v4.runtime.Token;

/**
 * The implementation of {@link XmlAST}. This should only be directly used to
 * create custom AST nodes and in 'XmlAstVisitor.java'.
 */
public final class XmlAstImpl implements XmlAST {

    /** Constant to indicate if not calculated the child count. */
    private static final int NOT_INITIALIZED = Integer.MIN_VALUE;

    /** The line number. **/
    private int lineNo = NOT_INITIALIZED;
    /** The column number. **/
    private int columnNo = NOT_INITIALIZED;
    /** The type of this XmlAST. */
    private int type;
    /** Text of this XmlAST. */
    private String text;

    /** Number of children. */
    private int childCount = NOT_INITIALIZED;

    /** First sibling of this XmlAST. */
    private XmlAstImpl nextSibling;
    /** Previous sibling. */
    private XmlAstImpl previousSibling;

    /** First child of this XmlAST. */
    private XmlAstImpl firstChild;
    /** The parent token. */
    private XmlAstImpl parent;

    /**
     * Initializes this XmlAstImpl.
     *
     * @param tokenType the type of this XmlAstImpl
     * @param tokenText the text of this XmlAstImpl
     */
    public void initialize(int tokenType, String tokenText) {
        type = tokenType;
        text = tokenText;
    }

    /**
     * Initializes this XmlAstImpl.
     *
     * @param token the token to generate this XmlAstImpl from
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
     * @param type the token type of this XmlAstImpl
     */
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getText() {
        return text;
    }

    /**
     * Sets the text for this XmlAstImpl.
     *
     * @param text the text field of this XmlAstImpl
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Add next sibling, pushes other siblings back.
     *
     * @param ast XmlAST object.
     */
    public void addNextSibling(XmlAST ast) {
        clearChildCountCache(parent);
        if (ast != null) {
            // parent is set in setNextSibling
            final XmlAstImpl sibling = nextSibling;
            final XmlAstImpl astImpl = (XmlAstImpl) ast;

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
     * @param nextSibling the XmlAST to set as sibling
     */
    public void setNextSibling(XmlAST nextSibling) {
        clearChildCountCache(parent);
        this.nextSibling = (XmlAstImpl) nextSibling;
        if (nextSibling != null && parent != null) {
            ((XmlAstImpl) nextSibling).setParent(parent);
        }
        if (nextSibling != null) {
            ((XmlAstImpl) nextSibling).previousSibling = this;
        }
    }

    @Override
    public XmlAstImpl getNextSibling() {
        return nextSibling;
    }

    /**
     * Add previous sibling.
     *
     * @param ast XmlAST object.
     */
    public void addPreviousSibling(XmlAST ast) {
        clearChildCountCache(parent);
        if (ast != null) {
            // parent is set in setNextSibling or parent.setFirstChild
            final XmlAstImpl previousSiblingNode = previousSibling;
            final XmlAstImpl astImpl = (XmlAstImpl) ast;

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
    public XmlAST getPreviousSibling() {
        return previousSibling;
    }

    /**
     * Adds a new child to the current AST.
     *
     * @param child to XmlAST to add as child
     */
    public void addChild(XmlAST child) {
        clearChildCountCache(this);
        if (child != null) {
            final XmlAstImpl astImpl = (XmlAstImpl) child;
            astImpl.setParent(this);
            astImpl.previousSibling = (XmlAstImpl) getLastChild();
        }
        XmlAST temp = firstChild;
        if (temp == null) {
            firstChild = (XmlAstImpl) child;
        }
        else {
            while (temp.getNextSibling() != null) {
                temp = temp.getNextSibling();
            }

            ((XmlAstImpl) temp).setNextSibling(child);
        }
    }

    /**
     * Sets the first child of this AST.
     *
     * @param firstChild the XmlAST to set as first child
     */
    public void setFirstChild(XmlAST firstChild) {
        clearChildCountCache(this);
        this.firstChild = (XmlAstImpl) firstChild;
        if (firstChild != null) {
            ((XmlAstImpl) firstChild).setParent(this);
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
            XmlAST child = firstChild;

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
        for (XmlAST ast = firstChild; ast != null; ast = ast.getNextSibling()) {
            if (ast.getType() == tokenType) {
                count++;
            }
        }
        return count;
    }

    @Override
    public XmlAstImpl getFirstChild() {
        return firstChild;
    }

    @Override
    public XmlAST getLastChild() {
        XmlAstImpl ast = firstChild;
        while (ast != null && ast.nextSibling != null) {
            ast = ast.nextSibling;
        }
        return ast;
    }

    @Override
    public XmlAST getParent() {
        return parent;
    }

    /**
     * Set the parent token.
     *
     * @param parent the parent token
     */
    private void setParent(XmlAstImpl parent) {
        XmlAstImpl instance = this;
        do {
            instance.parent = parent;
            instance = instance.nextSibling;
        } while (instance != null);
    }

    @Override
    public XmlAST findFirst(int findType) {
        XmlAST returnValue = null;

        for (XmlAST ast = firstChild; ast != null; ast = ast.getNextSibling()) {
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
    private static void clearChildCountCache(XmlAstImpl ast) {
        if (ast != null) {
            ast.childCount = NOT_INITIALIZED;
        }
    }

    @Override
    public String toString() {
        return text + "[" + getLineNo() + "x" + getColumnNo() + "]";
    }

}
