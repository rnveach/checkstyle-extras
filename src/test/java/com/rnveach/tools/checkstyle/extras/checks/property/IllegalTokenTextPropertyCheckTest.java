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

import static com.rnveach.tools.checkstyle.extras.checks.property.IllegalTokenTextPropertyCheck.MSG_KEY;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.rnveach.tools.checkstyle.extras.internal.AbstractExtraModuleTestSupport;

public class IllegalTokenTextPropertyCheckTest extends AbstractExtraModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/rnveach/tools/checkstyle/extras/checks/property";
    }

    @Test
    public void testDefault() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextPropertyCheck.class);

        verify(config, getPath("InputIllegalTokenTextProperty.properties"),
                CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testViolation() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextPropertyCheck.class);
        config.addProperty("tokens", "TEXT");
        config.addProperty("format", "key");

        final String[] expected = {
                "3:1: " + getCheckMessage(MSG_KEY, "key"),
                "3:6: " + getCheckMessage(MSG_KEY, "key"),
                "4:1: " + getCheckMessage(MSG_KEY, "key"),
                "5:1: " + getCheckMessage(MSG_KEY, "key"),
        };

        verify(config, getPath("InputIllegalTokenTextProperty.properties"), expected);
    }

    @Test
    public void testViolationSpecific() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextPropertyCheck.class);
        config.addProperty("tokens", "TEXT");
        config.addProperty("format", "key1");

        final String[] expected = {
                "4:1: " + getCheckMessage(MSG_KEY, "key1"),
        };

        verify(config, getPath("InputIllegalTokenTextProperty.properties"), expected);
    }

    @Test
    public void testViolationSpecificCase() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextPropertyCheck.class);
        config.addProperty("tokens", "TEXT");
        config.addProperty("format", "KEY1");

        verify(config, getPath("InputIllegalTokenTextProperty.properties"),
                CommonUtil.EMPTY_STRING_ARRAY);
    }

    @Test
    public void testViolationIgnoreCase() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextPropertyCheck.class);
        config.addProperty("tokens", "TEXT");
        config.addProperty("format", "KEY1");
        config.addProperty("ignoreCase", "true");

        final String[] expected = {
                "4:1: " + getCheckMessage(MSG_KEY, "KEY1"),
        };

        verify(config, getPath("InputIllegalTokenTextProperty.properties"), expected);
    }

    @Test
    public void testViolationSpecificMessage() throws Exception {
        final DefaultConfiguration config = createModuleConfig(IllegalTokenTextPropertyCheck.class);
        config.addProperty("tokens", "TEXT");
        config.addProperty("format", "key1");
        config.addProperty("message", "My message");

        final String[] expected = {
                "4:1: My message",
        };

        verify(config, getPath("InputIllegalTokenTextProperty.properties"), expected);
    }

}
