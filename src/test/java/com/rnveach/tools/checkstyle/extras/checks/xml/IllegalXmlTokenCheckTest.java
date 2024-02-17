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

import static com.rnveach.tools.checkstyle.extras.checks.xml.IllegalTokenXmlCheck.MSG_KEY;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.internal.AbstractExtraModuleTestSupport;

public class IllegalXmlTokenCheckTest extends AbstractExtraModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/rnveach/tools/checkstyle/extras/checks/xml";
    }

    @Test
    public void testDefault() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenXmlCheck.class);

        verify(config, getPath("InputIllegalXmlToken.xml"), CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testViolation() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenXmlCheck.class);
        config.addProperty("tokens", "ATTRIBUTE");

        final String[] expected = {
                "1:7: " + getCheckMessage(MSG_KEY, "ATTRIBUTE"),
                "1:21: " + getCheckMessage(MSG_KEY, "ATTRIBUTE"),
        };

        verify(config, getPath("InputIllegalXmlToken.xml"), expected);
    }

    @Test
    public void testCommentViolation() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenXmlCheck.class);
        config.addProperty("tokens", "COMMENT");

        final String[] expected = {
                "3:3: " + getCheckMessage(MSG_KEY, "<!-- comment -->"),
        };

        verify(config, getPath("InputIllegalXmlToken.xml"), expected);
    }

}
