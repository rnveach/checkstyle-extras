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

import static com.rnveach.tools.checkstyle.extras.checks.property.DuplicateKeyPropertyCheck.MSG_KEY;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.rnveach.tools.checkstyle.extras.internal.AbstractExtraModuleTestSupport;

public class DuplicateKeyPropertyCheckTest extends AbstractExtraModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/rnveach/tools/checkstyle/extras/checks/property";
    }

    @Test
    public void testDefault() throws Exception {
        final DefaultConfiguration config = createModuleConfig(DuplicateKeyPropertyCheck.class);

        final String[] expected = {
                "4:1: " + getCheckMessage(MSG_KEY, "key1"),
                "7:1: " + getCheckMessage(MSG_KEY, "key3"),
                "8:1: " + getCheckMessage(MSG_KEY, "key3"),
                "11:1: " + getCheckMessage(MSG_KEY, "key4"),
        };

        verify(config, getPath("InputDuplicateKeyProperty.properties"), expected);
    }

}
