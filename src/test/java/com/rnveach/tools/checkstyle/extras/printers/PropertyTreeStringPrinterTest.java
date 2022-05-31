package com.rnveach.tools.checkstyle.extras.printers;

import org.junit.jupiter.api.Test;

import com.rnveach.tools.checkstyle.extras.internal.AbstractTreeTestSupport;

public class PropertyTreeStringPrinterTest extends AbstractTreeTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/rnveach/tools/checkstyle/extras/printers";
    }

    @Test
    public void testParseFile() throws Exception {
        verifyPropertyAst(getPath("ExpectedProperty.txt"), getPath("Input.properties"));
    }

    @Test
    public void testEmpty() throws Exception {
        verifyPropertyAst(getPath("ExpectedEmptyProperty.txt"), getPath("InputEmpty.properties"));
    }

}
