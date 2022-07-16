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

import com.rnveach.tools.checkstyle.extras.asts.XmlAstImpl;
import com.rnveach.tools.checkstyle.extras.grammars.XmlLanguageParser;
import com.rnveach.tools.checkstyle.extras.grammars.XmlLanguageParser.EmptyElementContext;
import com.rnveach.tools.checkstyle.extras.grammars.XmlLanguageParser.EndElementContext;
import com.rnveach.tools.checkstyle.extras.grammars.XmlLanguageParser.StartElementContext;
import com.rnveach.tools.checkstyle.extras.grammars.XmlLanguageParserVisitor;
import com.rnveach.tools.checkstyle.extras.tokens.XmlTokenTypes;
import com.rnveach.tools.checkstyle.extras.utils.XmlAstUtil;

/**
 * Visitor class used to build XML AST from the parse tree produced by
 * {@link XmlLanguageParser}.
 */
public final class XmlAstVisitor extends AbstractParseTreeVisitor<XmlAstImpl>
        implements XmlLanguageParserVisitor<XmlAstImpl> {

    @Override
    public XmlAstImpl visitDocument(XmlLanguageParser.DocumentContext ctx) {
        final XmlAstImpl document;
        final boolean isEmptyFile = ctx.children.size() == 1;
        if (isEmptyFile) {
            document = null;
        }
        else {
            document = createImaginary(XmlTokenTypes.DOCUMENT);
            // last child is 'EOF', we do not include this token in AST
            processChildren(document, ctx.children.subList(0, ctx.children.size() - 1));
        }
        return document;
    }

    @Override
    public XmlAstImpl visitProlog(XmlLanguageParser.PrologContext ctx) {
        return create(ctx, XmlTokenTypes.PROLOG);
    }

    @Override
    public XmlAstImpl visitContent(XmlLanguageParser.ContentContext ctx) {
        return create(ctx, XmlTokenTypes.CONTENT);
    }

    @Override
    public XmlAstImpl visitElement(XmlLanguageParser.ElementContext ctx) {
        return create(ctx, XmlTokenTypes.ELEMENT);
    }

    @Override
    public XmlAstImpl visitStartElement(StartElementContext ctx) {
        return create(ctx, XmlTokenTypes.START_ELEMENT);
    }

    @Override
    public XmlAstImpl visitEndElement(EndElementContext ctx) {
        return create(ctx, XmlTokenTypes.END_ELEMENT);
    }

    @Override
    public XmlAstImpl visitEmptyElement(EmptyElementContext ctx) {
        return create(ctx, XmlTokenTypes.EMPTY_ELEMENT);
    }

    @Override
    public XmlAstImpl visitReference(XmlLanguageParser.ReferenceContext ctx) {
        return create(ctx, XmlTokenTypes.REFERENCE);
    }

    @Override
    public XmlAstImpl visitAttribute(XmlLanguageParser.AttributeContext ctx) {
        return create(ctx, XmlTokenTypes.ATTRIBUTE);
    }

    @Override
    public XmlAstImpl visitChardata(XmlLanguageParser.ChardataContext ctx) {
        return flattenedTree(ctx);
    }

    @Override
    public XmlAstImpl visitMisc(XmlLanguageParser.MiscContext ctx) {
        return create(ctx, XmlTokenTypes.MISC);
    }

    /**
     * Builds the AST for a particular node, then returns a "flattened" tree of
     * siblings. This method should be used in rule contexts such as
     * {@code variableDeclarators}, where we have both terminals and
     * non-terminals.
     *
     * @param ctx the ParserRuleContext to base tree on
     * @return flattened XmlAstImpl
     */
    private XmlAstImpl flattenedTree(ParserRuleContext ctx) {
        final XmlAstImpl dummyNode = new XmlAstImpl();
        processChildren(dummyNode, ctx.children);
        return dummyNode.getFirstChild();
    }

    /**
     * Create a XmlAstImpl from a given composite. This method should be used
     * for composite nodes.
     *
     * @param ctx the context of the current parsing
     * @param tokenType the token type of this XmlAstImpl
     * @return new XmlAstImpl of given type
     */
    private XmlAstImpl create(ParserRuleContext ctx, int tokenType) {
        final XmlAstImpl ast = createImaginary(tokenType);
        processChildren(ast, ctx.children);
        return ast;
    }

    /**
     * Create a XmlAstImpl from a given TerminalNode. This method should be used
     * for terminal nodes, i.e. {@code @}.
     *
     * @param node the TerminalNode to build the XmlAstImpl from
     * @return new XmlAstImpl of given type
     */
    private static XmlAstImpl create(TerminalNode node) {
        return create((Token) node.getPayload());
    }

    /**
     * Create a XmlAstImpl from a given token. This method should be used for
     * terminal nodes when we are building an AST for a specific token,
     * regardless of position.
     *
     * @param token the token to build the XmlAstImpl from
     * @return new XmlAstImpl of given type
     */
    private static XmlAstImpl create(Token token) {
        final XmlAstImpl xmlAst = new XmlAstImpl();
        xmlAst.initialize(token);
        return xmlAst;
    }

    /**
     * Create a XmlAstImpl from a given token and token type. This method should
     * be used for imaginary nodes only where the text on the RHS matches the
     * text on the LHS.
     *
     * @param tokenType the token type of this XmlAstImpl
     * @return new XmlAstImpl of given type
     */
    private static XmlAstImpl createImaginary(int tokenType) {
        final XmlAstImpl xmlAst = new XmlAstImpl();
        xmlAst.setType(tokenType);
        xmlAst.setText(XmlAstUtil.getTokenName(tokenType));
        return xmlAst;
    }

    /**
     * Adds all the children from the given ParseTree or
     * XmlLanguageParserContext list to the parent XmlAstImpl.
     *
     * @param parent the XmlAstImpl to add children to
     * @param children the list of children to add
     */
    private void processChildren(XmlAstImpl parent, List<? extends ParseTree> children) {
        if (children != null) {
            children.forEach(child -> {
                if (child instanceof TerminalNode) {
                    // Child is a token, create a new XmlAstImpl and add it to
                    // parent
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
}
