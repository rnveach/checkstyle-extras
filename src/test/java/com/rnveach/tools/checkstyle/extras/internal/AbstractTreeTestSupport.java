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

package com.rnveach.tools.checkstyle.extras.internal;

import static com.google.common.truth.Truth.assertWithMessage;

import java.io.File;

import com.puppycrawl.tools.checkstyle.AbstractPathTestSupport;
import com.rnveach.tools.checkstyle.extras.printers.PropertyTreeStringPrinter;
import com.rnveach.tools.checkstyle.extras.printers.XmlTreeStringPrinter;

public abstract class AbstractTreeTestSupport extends AbstractPathTestSupport {

    /**
     * Performs verification of the given text ast tree representation.
     *
     * @param expectedTextPrintFileName expected text ast tree representation.
     * @param actualFileName actual text ast tree representation.
     * @throws Exception if exception occurs during verification.
     */
    protected static void verifyXmlAst(String expectedTextPrintFileName, String actualFileName)
            throws Exception {
        final String expectedContents = readFile(expectedTextPrintFileName);

        final String actualContents = toLfLineEnding(
                XmlTreeStringPrinter.printFileAst(new File(actualFileName)));

        assertWithMessage("Generated AST from file should match pre-defined AST")
                .that(actualContents).isEqualTo(expectedContents);
    }

    /**
     * Performs verification of the given text ast tree representation.
     *
     * @param expectedTextPrintFileName expected text ast tree representation.
     * @param actualFileName actual text ast tree representation.
     * @throws Exception if exception occurs during verification.
     */
    protected static void verifyPropertyAst(String expectedTextPrintFileName, String actualFileName)
            throws Exception {
        final String expectedContents = readFile(expectedTextPrintFileName);

        final String actualContents = toLfLineEnding(
                PropertyTreeStringPrinter.printFileAst(new File(actualFileName)));

        assertWithMessage("Generated AST from file should match pre-defined AST")
                .that(actualContents).isEqualTo(expectedContents);
    }

}
