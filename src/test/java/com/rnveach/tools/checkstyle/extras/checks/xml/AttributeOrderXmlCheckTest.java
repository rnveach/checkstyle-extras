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

import static com.rnveach.tools.checkstyle.extras.checks.xml.AttributeOrderXmlCheck.MSG_KEY;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.internal.AbstractExtraModuleTestSupport;

public class AttributeOrderXmlCheckTest extends AbstractExtraModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/rnveach/tools/checkstyle/extras/checks/xml";
    }

    @Test
    public void testDefault() throws Exception {
        final DefaultConfiguration config = createModuleConfig(AttributeOrderXmlCheck.class);

        verify(config, getPath("InputAttributeOrderXml.xml"), CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testOrdered() throws Exception {
        final DefaultConfiguration config = createModuleConfig(AttributeOrderXmlCheck.class);
        config.addProperty("element", "element\\d+");
        config.addProperty("attributeOrder",
                "attribute1, attribute2, attribute3, attribute4, attribute5");

        verify(config, getPath("InputAttributeOrderXml.xml"), CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testMissingOrder() throws Exception {
        final DefaultConfiguration config = createModuleConfig(AttributeOrderXmlCheck.class);
        config.addProperty("element", "element\\d+");
        final String order = "attribute1, attribute2, attribute4, attribute3, attribute5";
        config.addProperty("attributeOrder", order);

        final String[] expected = {
                "6:55: " + getCheckMessage(MSG_KEY, "[" + order + "]"),
                "7:27: " + getCheckMessage(MSG_KEY, "[" + order + "]"),
        };

        verify(config, getPath("InputAttributeOrderXml.xml"), expected);
    }

    @Test
    public void testWrongOrder() throws Exception {
        final DefaultConfiguration config = createModuleConfig(AttributeOrderXmlCheck.class);
        config.addProperty("element", "element\\d+");
        final String order = "attribute5, attribute4, attribute3, attribute2, attribute1";
        config.addProperty("attributeOrder", order);

        final String[] expected = {
                "6:27: " + getCheckMessage(MSG_KEY, "[" + order + "]"),
                "6:41: " + getCheckMessage(MSG_KEY, "[" + order + "]"),
                "6:55: " + getCheckMessage(MSG_KEY, "[" + order + "]"),
                "6:69: " + getCheckMessage(MSG_KEY, "[" + order + "]"),
                "7:27: " + getCheckMessage(MSG_KEY, "[" + order + "]"),
        };

        verify(config, getPath("InputAttributeOrderXml.xml"), expected);
    }

    @Test
    public void testWrongOrderPartial() throws Exception {
        final DefaultConfiguration config = createModuleConfig(AttributeOrderXmlCheck.class);
        config.addProperty("element", "element\\d+");
        final String order = "attribute3, attribute2";
        config.addProperty("attributeOrder", order);

        final String[] expected = {
                "6:41: " + getCheckMessage(MSG_KEY, "[" + order + "]"),
        };

        verify(config, getPath("InputAttributeOrderXml.xml"), expected);
    }

    @Test
    public void testWrongOrderSpecificElement() throws Exception {
        final DefaultConfiguration config = createModuleConfig(AttributeOrderXmlCheck.class);
        config.addProperty("element", "element5");
        final String order = "attribute5, attribute4, attribute3, attribute2, attribute1";
        config.addProperty("attributeOrder", order);

        final String[] expected = {
                "7:27: " + getCheckMessage(MSG_KEY, "[" + order + "]"),
        };

        verify(config, getPath("InputAttributeOrderXml.xml"), expected);
    }

}
