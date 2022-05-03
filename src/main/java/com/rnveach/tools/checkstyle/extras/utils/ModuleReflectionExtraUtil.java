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

package com.rnveach.tools.checkstyle.extras.utils;

import com.puppycrawl.tools.checkstyle.utils.ModuleReflectionUtil;
import com.rnveach.tools.checkstyle.extras.checks.AbstractXmlCheck;
import com.rnveach.tools.checkstyle.extras.filters.XmlWalkerFilter;

/**
 * Contains utility methods for module reflection.
 */
public final class ModuleReflectionExtraUtil {

    /** Prevent instantiation. */
    private ModuleReflectionExtraUtil() {
    }

    /**
     * Checks whether a class may be considered as a checkstyle module.
     * Checkstyle's modules are non-abstract classes, which are either
     * checkstyle's checks, file sets, filters, file filters, {@code XmlWalker}
     * filters, audit listener, or root module.
     *
     * @param clazz class to check.
     * @return true if the class may be considered as the checkstyle module.
     */
    public static boolean isCheckstyleModule(Class<?> clazz) {
        return ModuleReflectionUtil.isCheckstyleModule(clazz)
                || ModuleReflectionUtil.isValidCheckstyleClass(clazz)
                        && (isXmlWalkerCheck(clazz) || isXmlWalkerFilterModule(clazz));
    }

    /**
     * Checks whether a class may be considered as the XML check which has
     * XmlWalker as a parent. Checkstyle's XML checks are classes which
     * implement 'AbstractXmlCheck' interface.
     *
     * @param clazz class to check.
     * @return true if a class may be considered as the XML check.
     */
    public static boolean isXmlWalkerCheck(Class<?> clazz) {
        return AbstractXmlCheck.class.isAssignableFrom(clazz);
    }

    /**
     * Checks whether a class may be considered as the checkstyle
     * {@code XmlWalker} filter. Checkstyle's {@code XmlWalker} filters are
     * classes which implement 'XmlWalkerFilter' interface.
     *
     * @param clazz class to check.
     * @return true if a class may be considered as the checkstyle
     *         {@code XmlWalker} filter.
     */
    public static boolean isXmlWalkerFilterModule(Class<?> clazz) {
        return XmlWalkerFilter.class.isAssignableFrom(clazz);
    }
}
