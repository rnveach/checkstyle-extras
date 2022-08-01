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

package com.rnveach.tools.checkstyle.extras.checks.property;

import static com.rnveach.tools.checkstyle.extras.checks.property.IndentationPropertyCheck.MSG_KEY;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.internal.AbstractExtraModuleTestSupport;

public class IndentationPropertyCheckTest extends AbstractExtraModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/rnveach/tools/checkstyle/extras/checks/property";
    }

    @Test
    public void testDefault() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IndentationPropertyCheck.class);

        verify(config, getPath("InputIndentationProperty.properties"),
                CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testStrictDefault() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IndentationPropertyCheck.class);
        config.addProperty("forceStrictIndentation", "true");

        verify(config, getPath("InputIndentationProperty.properties"),
                CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testCompletelyBad() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IndentationPropertyCheck.class);

        verify(config, getPath("InputIndentationPropertyCompletelyBad.properties"),
                CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testStrictCompletelyBad() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IndentationPropertyCheck.class);
        config.addProperty("forceStrictIndentation", "true");

        final String[] expected = {
                "1:19: " + getCheckMessage(MSG_KEY, 18, 0),
                "2:19: " + getCheckMessage(MSG_KEY, 18, 0),
                "3:19: " + getCheckMessage(MSG_KEY, 18, 0),
                "4:19: " + getCheckMessage(MSG_KEY, 18, 0),
                "5:19: " + getCheckMessage(MSG_KEY, 18, 0),
                "6:23: " + getCheckMessage(MSG_KEY, 22, 4),
                "7:19: " + getCheckMessage(MSG_KEY, 18, 0),
        };

        verify(config, getPath("InputIndentationPropertyCompletelyBad.properties"), expected);
    }

}
