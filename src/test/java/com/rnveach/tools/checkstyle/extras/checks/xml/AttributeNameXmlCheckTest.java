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

import static com.rnveach.tools.checkstyle.extras.checks.xml.AttributeNameXmlCheck.MSG_KEY;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.internal.AbstractExtraModuleTestSupport;

public class AttributeNameXmlCheckTest extends AbstractExtraModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/rnveach/tools/checkstyle/extras/checks/xml";
    }

    @Test
    public void testDefault() throws Exception {
        final DefaultConfiguration config = createModuleConfig(AttributeNameXmlCheck.class);
        final String format = "^[a-z][a-zA-Z0-9]*$";

        final String[] expected = {
                "3:31: " + getCheckMessage(MSG_KEY, "attribute_2", format),
        };

        verify(config, getPath("InputAttributeNameXml.xml"), expected);
    }

    @Test
    public void testFormat() throws Exception {
        final DefaultConfiguration config = createModuleConfig(AttributeNameXmlCheck.class);
        config.addProperty("format", "");

        verify(config, getPath("InputAttributeNameXml.xml"), CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testFormat2() throws Exception {
        final DefaultConfiguration config = createModuleConfig(AttributeNameXmlCheck.class);
        final String format = "version|encoding|_2";
        config.addProperty("format", format);

        final String[] expected = {
                "3:12: " + getCheckMessage(MSG_KEY, "attribute1", format),
        };

        verify(config, getPath("InputAttributeNameXml.xml"), expected);
    }

}
