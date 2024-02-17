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

import static com.rnveach.tools.checkstyle.extras.checks.xml.IndentationXmlCheck.MSG_KEY;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.internal.AbstractExtraModuleTestSupport;

public class IndentationXmlCheckTest extends AbstractExtraModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/rnveach/tools/checkstyle/extras/checks/xml";
    }

    @Test
    public void testDefault() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IndentationXmlCheck.class);

        verify(config, getPath("InputIndentationXml.xml"), CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testStrictDefault() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IndentationXmlCheck.class);
        config.addProperty("forceStrictIndentation", "true");

        verify(config, getPath("InputIndentationXml.xml"), CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testCompletelyBad() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IndentationXmlCheck.class);

        verify(config, getPath("InputIndentationXmlCompletelyBad.xml"),
                CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testStrictCompletelyBad() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IndentationXmlCheck.class);
        config.addProperty("forceStrictIndentation", "true");

        final String[] expected = {
                "1:19: " + getCheckMessage(MSG_KEY, 18, 0),
                "2:19: " + getCheckMessage(MSG_KEY, 18, 0),
                "3:23: " + getCheckMessage(MSG_KEY, 22, 4),
                "4:23: " + getCheckMessage(MSG_KEY, 22, 4),
                "5:23: " + getCheckMessage(MSG_KEY, 22, 4),
                "6:23: " + getCheckMessage(MSG_KEY, 22, 4),
                "7:27: " + getCheckMessage(MSG_KEY, 26, 8),
                "8:23: " + getCheckMessage(MSG_KEY, 22, 4),
                "9:19: " + getCheckMessage(MSG_KEY, 18, 0),
        };

        verify(config, getPath("InputIndentationXmlCompletelyBad.xml"), expected);
    }

    @Test
    public void testStrictNoSpacing() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IndentationXmlCheck.class);
        config.addProperty("forceStrictIndentation", "true");

        final String[] expected = {
                "2:1: " + getCheckMessage(MSG_KEY, 0, 4), //
                "3:1: " + getCheckMessage(MSG_KEY, 0, 4), //
                "4:1: " + getCheckMessage(MSG_KEY, 0, 4), //
                "6:1: " + getCheckMessage(MSG_KEY, 0, 4), //
                "7:1: " + getCheckMessage(MSG_KEY, 0, 4), //
                "8:1: " + getCheckMessage(MSG_KEY, 0, 4), //
                "9:1: " + getCheckMessage(MSG_KEY, 0, 4), //
                "10:1: " + getCheckMessage(MSG_KEY, 0, 8),
                "11:1: " + getCheckMessage(MSG_KEY, 0, 4),
                "12:1: " + getCheckMessage(MSG_KEY, 0, 4),
                "14:1: " + getCheckMessage(MSG_KEY, 0, 4),
                "15:1: " + getCheckMessage(MSG_KEY, 0, 4),
                "17:1: " + getCheckMessage(MSG_KEY, 0, 4),
                "18:1: " + getCheckMessage(MSG_KEY, 0, 8),
                "19:1: " + getCheckMessage(MSG_KEY, 0, 8),
                "20:1: " + getCheckMessage(MSG_KEY, 0, 8),
                "21:1: " + getCheckMessage(MSG_KEY, 0, 4),
                "22:1: " + getCheckMessage(MSG_KEY, 0, 8),
        };

        verify(config, getPath("InputIndentationXmlNoSpacing.xml"), expected);
    }

    @Test
    public void testStrictNoSpacingCorrected() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IndentationXmlCheck.class);
        config.addProperty("forceStrictIndentation", "true");

        verify(config, getPath("InputIndentationXmlNoSpacingCorrected.xml"),
                CommonUtil.EMPTY_STRING_ARRAY);
    }

}
