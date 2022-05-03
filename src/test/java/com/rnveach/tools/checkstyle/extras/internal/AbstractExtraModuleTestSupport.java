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

package com.rnveach.tools.checkstyle.extras.internal;

import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.rnveach.tools.checkstyle.extras.utils.ModuleReflectionExtraUtil;
import com.rnveach.tools.checkstyle.extras.walkers.XmlWalker;

public abstract class AbstractExtraModuleTestSupport extends AbstractModuleTestSupport {
    @Override
    protected void configureChecker(Checker checker, Configuration moduleConfig) throws Exception {
        final Class<?> moduleClass = Class.forName(moduleConfig.getName());

        if (ModuleReflectionExtraUtil.isXmlWalkerCheck(moduleClass)
                || ModuleReflectionExtraUtil.isXmlWalkerFilterModule(moduleClass)) {
            final Configuration config = createRootConfig(createXmlWalkerConfig(moduleConfig));
            checker.configure(config);
        }
        else {
            super.configureChecker(checker, moduleConfig);
        }
    }

    /**
     * Creates {@link DefaultConfiguration} for the {@link XmlWalker} based on
     * the given {@link Configuration} instance.
     *
     * @param config {@link Configuration} instance.
     * @return {@link DefaultConfiguration} for the {@link XmlWalker} based on
     *         the given {@link Configuration} instance.
     */
    protected static DefaultConfiguration createXmlWalkerConfig(Configuration config) {
        final DefaultConfiguration xmlConfig = createModuleConfig(XmlWalker.class);
        xmlConfig.addChild(config);
        return xmlConfig;
    }
}
