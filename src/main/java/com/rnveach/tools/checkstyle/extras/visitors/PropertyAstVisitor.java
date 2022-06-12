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

package com.rnveach.tools.checkstyle.extras.visitors;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.rnveach.tools.checkstyle.extras.asts.PropertyAST;
import com.rnveach.tools.checkstyle.extras.asts.PropertyAstImpl;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageLexer;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.AssignmentContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.CommentContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.ContinuationContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.DeclContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.EscapedCharacterContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.FileContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.KeyContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.KeyNoSpaceContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.RowContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.SpaceAssignmentContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.SpaceDeclContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.ValueContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.ValueNoAssignmentContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.ValueTextContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParser.ValueTextNoAssignmentContext;
import com.rnveach.tools.checkstyle.extras.grammars.PropertyLanguageParserVisitor;
import com.rnveach.tools.checkstyle.extras.tokens.PropertyTokenTypes;
import com.rnveach.tools.checkstyle.extras.utils.PropertyAstUtil;

/**
 * Visitor class used to build property AST from the parse tree produced by
 * {@link PropertyLanguageParser}.
 */
public final class PropertyAstVisitor extends AbstractParseTreeVisitor<PropertyAstImpl>
        implements PropertyLanguageParserVisitor<PropertyAstImpl> {

    @Override
    public PropertyAstImpl visitFile(FileContext ctx) {
        final PropertyAstImpl file;
        final boolean isEmptyFile = ctx.children.size() == 1;
        if (isEmptyFile) {
            file = null;
        }
        else {
            file = createImaginary(PropertyTokenTypes.FILE);
            // last child is 'EOF', we do not include this token in AST
            processChildren(file, ctx.children.subList(0, ctx.children.size() - 1));
        }
        return file;
    }

    @Override
    public PropertyAstImpl visitRow(RowContext ctx) {
        return create(ctx, PropertyTokenTypes.ROW);
    }

    @Override
    public PropertyAstImpl visitDecl(DeclContext ctx) {
        return create(ctx, PropertyTokenTypes.DECL);
    }

    @Override
    public PropertyAstImpl visitKey(KeyContext ctx) {
        return create(ctx, PropertyTokenTypes.KEY);
    }

    @Override
    public PropertyAstImpl visitAssignment(AssignmentContext ctx) {
        return create(ctx, PropertyTokenTypes.ASSIGNMENT);
    }

    @Override
    public PropertyAstImpl visitValue(ValueContext ctx) {
        final PropertyAstImpl ast = create(ctx, PropertyTokenTypes.VALUE);
        final PropertyAST lastChild = ast.getLastChild();
        if (lastChild.getType() == PropertyLanguageLexer.EOF) {
            ast.removeChild(lastChild);
        }
        return ast;
    }

    @Override
    public PropertyAstImpl visitValueText(ValueTextContext ctx) {
        final PropertyAstImpl ast = createImaginary(PropertyTokenTypes.VALUE_TEXT);
        processChildrenAndCombineText(ast, ctx.children);
        return ast;
    }

    @Override
    public PropertyAstImpl visitContinuation(ContinuationContext ctx) {
        final PropertyAstImpl ast = create(ctx, PropertyTokenTypes.CONTINUATION);
        final PropertyAST lastChild = ast.getLastChild();
        if (lastChild.getType() == PropertyLanguageLexer.EOF) {
            ast.removeChild(lastChild);
        }
        return ast;
    }

    @Override
    public PropertyAstImpl visitComment(CommentContext ctx) {
        final PropertyAstImpl ast = createImaginary(PropertyTokenTypes.COMMENT);
        processChildrenAndCombineText(ast, ctx.children);
        return ast;
    }

    @Override
    public PropertyAstImpl visitSpaceDecl(SpaceDeclContext ctx) {
        // rename for easier token tree
        return create(ctx, PropertyTokenTypes.DECL);
    }

    @Override
    public PropertyAstImpl visitSpaceAssignment(SpaceAssignmentContext ctx) {
        // rename for easier token tree
        return create(ctx, PropertyTokenTypes.ASSIGNMENT);
    }

    @Override
    public PropertyAstImpl visitKeyNoSpace(KeyNoSpaceContext ctx) {
        // rename for easier token tree, parent is already key
        return flattenedTree(ctx);
    }

    @Override
    public PropertyAstImpl visitValueNoAssignment(ValueNoAssignmentContext ctx) {
        // rename for easier token tree
        final PropertyAstImpl ast = createImaginary(PropertyTokenTypes.VALUE);
        processChildrenAndCombineValueText(ast, ctx.children);
        return ast;
    }

    @Override
    public PropertyAstImpl visitValueTextNoAssignment(ValueTextNoAssignmentContext ctx) {
        // rename for easier token tree
        return create(ctx, PropertyTokenTypes.VALUE_TEXT);
    }

    @Override
    public PropertyAstImpl visitEscapedCharacter(EscapedCharacterContext ctx) {
        final PropertyAstImpl ast = create(ctx, PropertyTokenTypes.ESCAPED_CHARACTER);
        final PropertyAstImpl character = ast.getFirstChild().getNextSibling();

        if (character.getType() == PropertyTokenTypes.TEXT
                || character.getType() == PropertyTokenTypes.WS) {
            final String originalText = character.getText();

            if (originalText.length() > 1) {
                character.setText(originalText.substring(0, 1));

                final PropertyAstImpl splitAst = new PropertyAstImpl();
                splitAst.initialize(character.getType(), originalText.substring(1));
                splitAst.setLineNo(character.getLineNo());
                splitAst.setColumnNo(character.getColumnNo() + 1);

                ast.setNextSibling(splitAst);
            }
        }

        return ast;
    }

    /**
     * Builds the AST for a particular node, then returns a "flattened" tree of
     * siblings. This method should be used in rule contexts such as
     * {@code variableDeclarators}, where we have both terminals and
     * non-terminals.
     *
     * @param ctx the ParserRuleContext to base tree on
     * @return flattened PropertyAstImpl
     */
    private PropertyAstImpl flattenedTree(ParserRuleContext ctx) {
        final PropertyAstImpl dummyNode = new PropertyAstImpl();
        processChildren(dummyNode, ctx.children);
        return dummyNode.getFirstChild();
    }

    /**
     * Create a PropertyAstImpl from a given composite. This method should be
     * used for composite nodes.
     *
     * @param ctx the context of the current parsing
     * @param tokenType the token type of this PropertyAstImpl
     * @return new PropertyAstImpl of given type
     */
    private PropertyAstImpl create(ParserRuleContext ctx, int tokenType) {
        final PropertyAstImpl ast = createImaginary(tokenType);
        processChildren(ast, ctx.children);
        return ast;
    }

    /**
     * Create a PropertyAstImpl from a given TerminalNode. This method should be
     * used for terminal nodes, i.e. {@code @}.
     *
     * @param node the TerminalNode to build the PropertyAstImpl from
     * @return new PropertyAstImpl of given type
     */
    private static PropertyAstImpl create(TerminalNode node) {
        return create((Token) node.getPayload());
    }

    /**
     * Create a PropertyAstImpl from a given token. This method should be used
     * for terminal nodes when we are building an AST for a specific token,
     * regardless of position.
     *
     * @param token the token to build the PropertyAstImpl from
     * @return new PropertyAstImpl of given type
     */
    private static PropertyAstImpl create(Token token) {
        final PropertyAstImpl propertyAst = new PropertyAstImpl();
        propertyAst.initialize(token);
        return propertyAst;
    }

    /**
     * Create a PropertyAstImpl from a given token and token type. This method
     * should be used for imaginary nodes only where the text on the RHS matches
     * the text on the LHS.
     *
     * @param tokenType the token type of this PropertyAstImpl
     * @return new PropertyAstImpl of given type
     */
    private static PropertyAstImpl createImaginary(int tokenType) {
        final PropertyAstImpl propertyAst = new PropertyAstImpl();
        propertyAst.setType(tokenType);
        propertyAst.setText(PropertyAstUtil.getTokenName(tokenType));
        return propertyAst;
    }

    /**
     * Adds all the children from the given ParseTree or JavaParserContext list
     * to the parent PropertyAstImpl.
     *
     * @param parent the PropertyAstImpl to add children to
     * @param children the list of children to add
     */
    private void processChildren(PropertyAstImpl parent, List<? extends ParseTree> children) {
        if (children != null) {
            children.forEach(child -> {
                if (child instanceof TerminalNode) {
                    // Child is a token, create a new PropertyAstImpl and add it
                    // to parent
                    parent.addChild(create((TerminalNode) child));
                }
                else {
                    // Child is another rule context; visit it, create token,
                    // and add to parent
                    parent.addChild(visit(child));
                }
            });
        }
    }

    /**
     * Adds the children and combines various texts from the given ParseTree or
     * JavaParserContext list to the parent DetailAstImpl.
     *
     * @param parent the PropertyAstImpl to add children to
     * @param children the list of children to add
     */
    private void processChildrenAndCombineText(PropertyAstImpl parent,
            List<? extends ParseTree> children) {
        PropertyAstImpl lastNewChild = null;

        for (ParseTree child : children) {
            final PropertyAstImpl newChild;
            if (child instanceof TerminalNode) {
                // Child is a token, create a new PropertyAstImpl and add it
                // to parent
                newChild = create((TerminalNode) child);
            }
            else {
                // Child is another rule context; visit it, create token,
                // and add to parent
                newChild = visit(child);
            }

            if (newChild.getType() == PropertyTokenTypes.WS) {
                newChild.setType(PropertyTokenTypes.TEXT);
            }

            if (lastNewChild != null && lastNewChild.getType() == PropertyTokenTypes.TEXT
                    && newChild.getType() == PropertyTokenTypes.TEXT) {
                lastNewChild.setText(lastNewChild.getText() + newChild.getText());
            }
            else {
                parent.addChild(newChild);
                lastNewChild = newChild;
            }
        }
    }

    /**
     * Adds the children and combines multiple value texts from the given
     * ParseTree or JavaParserContext list to the parent DetailAstImpl.
     *
     * @param parent the PropertyAstImpl to add children to
     * @param children the list of children to add
     */
    private void processChildrenAndCombineValueText(PropertyAstImpl parent,
            List<? extends ParseTree> children) {
        PropertyAstImpl lastNewChild = null;

        for (ParseTree child : children) {
            final PropertyAstImpl newChild;
            if (child instanceof TerminalNode) {
                // Child is a token, create a new PropertyAstImpl and add it
                // to parent
                newChild = create((TerminalNode) child);
            }
            else {
                // Child is another rule context; visit it, create token,
                // and add to parent
                newChild = visit(child);
            }

            if (lastNewChild != null && lastNewChild.getType() == PropertyTokenTypes.VALUE_TEXT
                    && newChild.getType() == PropertyTokenTypes.VALUE_TEXT) {
                final PropertyAstImpl lastNewChildLastChild = (PropertyAstImpl) lastNewChild
                        .getLastChild();
                PropertyAstImpl newChildFirstChild = newChild.getFirstChild();

                if (lastNewChildLastChild.getType() == PropertyTokenTypes.TEXT
                        && newChildFirstChild.getType() == PropertyTokenTypes.TEXT) {
                    lastNewChildLastChild.setText(
                            lastNewChildLastChild.getText() + newChildFirstChild.getText());

                    newChildFirstChild = newChildFirstChild.getNextSibling();
                }

                lastNewChildLastChild.addChild(newChildFirstChild);
            }
            else {
                parent.addChild(newChild);
                lastNewChild = newChild;
            }
        }
    }

}
