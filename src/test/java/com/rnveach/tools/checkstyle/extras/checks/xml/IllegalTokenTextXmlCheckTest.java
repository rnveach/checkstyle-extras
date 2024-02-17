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

package com.rnveach.tools.checkstyle.extras.checks.xml;

import static com.rnveach.tools.checkstyle.extras.checks.xml.IllegalTokenTextXmlCheck.MSG_KEY;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.internal.AbstractExtraModuleTestSupport;

public class IllegalTokenTextXmlCheckTest extends AbstractExtraModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/rnveach/tools/checkstyle/extras/checks/xml";
    }

    @Test
    public void testDefault() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextXmlCheck.class);

        verify(config, getPath("InputIllegalTokenTextXml.xml"), CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testViolation() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextXmlCheck.class);
        config.addProperty("tokens", "NAME");
        config.addProperty("format", "element");

        final String[] expected = {
                "3:4: " + getCheckMessage(MSG_KEY, "element"),
                "3:26: " + getCheckMessage(MSG_KEY, "element"),
                "4:4: " + getCheckMessage(MSG_KEY, "element"),
                "4:50: " + getCheckMessage(MSG_KEY, "element"),
        };

        verify(config, getPath("InputIllegalTokenTextXml.xml"), expected);
    }

    @Test
    public void testViolationSpecific() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextXmlCheck.class);
        config.addProperty("tokens", "NAME");
        config.addProperty("format", "element1");

        final String[] expected = {
                "3:4: " + getCheckMessage(MSG_KEY, "element1"),
                "3:26: " + getCheckMessage(MSG_KEY, "element1"),
        };

        verify(config, getPath("InputIllegalTokenTextXml.xml"), expected);
    }

    @Test
    public void testViolationSpecificCase() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextXmlCheck.class);
        config.addProperty("tokens", "NAME");
        config.addProperty("format", "ELEMENT1");

        verify(config, getPath("InputIllegalTokenTextXml.xml"), CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testViolationIgnoreCase() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextXmlCheck.class);
        config.addProperty("tokens", "NAME");
        config.addProperty("format", "ELEMENT1");
        config.addProperty("ignoreCase", "true");

        final String[] expected = {
                "3:4: " + getCheckMessage(MSG_KEY, "ELEMENT1"),
                "3:26: " + getCheckMessage(MSG_KEY, "ELEMENT1"),
        };

        verify(config, getPath("InputIllegalTokenTextXml.xml"), expected);
    }

    @Test
    public void testViolationSpecificMessage() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextXmlCheck.class);
        config.addProperty("tokens", "NAME");
        config.addProperty("format", "element1");
        config.addProperty("message", "My message");

        final String[] expected = {
                "3:4: My message", "3:26: My message",
        };

        verify(config, getPath("InputIllegalTokenTextXml.xml"), expected);
    }

}
