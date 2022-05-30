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

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.asts.XmlAST;
import com.rnveach.tools.checkstyle.extras.checks.AbstractXmlCheck;
import com.rnveach.tools.checkstyle.extras.tokens.XmlTokenTypes;

/** Checks the order of attributes for specific elements. */
public final class AttributeOrderXmlCheck extends AbstractXmlCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "attribute.order";

    /** Specify the element(s) to match on. */
    private Pattern element = CommonUtil.createPattern("$^");

    /** Specify the order of attributes. */
    private List<String> attributeOrder = Arrays.asList();

    /**
     * Setter to specify the element(s) to match on.
     *
     * @param element user's element(s) to match on.
     */
    public void setElement(Pattern element) {
        this.element = element;
    }

    /**
     * Setter to specify the order by attributes.
     *
     * @param attributeOrder user's attribute orders.
     */
    public void setAttributeOrder(String... attributeOrder) {
        this.attributeOrder = Arrays.asList(attributeOrder);
    }

    @Override
    public int[] getRequiredTokens() {
        return new int[] {
                XmlTokenTypes.PROLOG, //
                XmlTokenTypes.START_ELEMENT, //
                XmlTokenTypes.EMPTY_ELEMENT, //
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
    public void visitToken(XmlAST ast) {
        final String elementName;

        if (ast.getType() == XmlTokenTypes.PROLOG) {
            elementName = ast.getText();
        }
        else {
            elementName = ast.findFirst(XmlTokenTypes.NAME).getText();
        }

        if (element.matcher(elementName).matches()) {
            validateAttributes(ast);
        }
    }

    /**
     * Checks order of attributes in element.
     *
     * @param ast Element to examine.
     */
    private void validateAttributes(XmlAST ast) {
        int maxIndexFound = 0;

        for (XmlAST attribute = ast
                .findFirst(XmlTokenTypes.ATTRIBUTE); attribute != null; attribute = attribute
                        .getNextSibling()) {
            if (attribute.getType() != XmlTokenTypes.ATTRIBUTE) {
                continue;
            }

            final String name = attribute.findFirst(XmlTokenTypes.NAME).getText();
            final int index = attributeOrder.indexOf(name);

            if (index != -1) {
                if (index < maxIndexFound) {
                    log(attribute, MSG_KEY, attributeOrder.toString());
                }
                else {
                    maxIndexFound = index;
                }
            }
        }
    }

}
