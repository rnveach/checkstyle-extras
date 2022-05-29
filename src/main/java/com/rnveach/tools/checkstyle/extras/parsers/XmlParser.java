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

package com.rnveach.tools.checkstyle.extras.parsers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import com.puppycrawl.tools.checkstyle.CheckstyleParserErrorStrategy;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.rnveach.tools.checkstyle.extras.asts.XmlAST;
import com.rnveach.tools.checkstyle.extras.grammars.XmlLanguageLexer;
import com.rnveach.tools.checkstyle.extras.grammars.XmlLanguageParser;
import com.rnveach.tools.checkstyle.extras.visitors.XmlAstVisitor;

/** Helper methods to parse XML source files. */
public final class XmlParser {
    /** Stop instances being created. **/
    private XmlParser() {
    }

    /**
     * Static helper method to parses a XML source file.
     *
     * @param contents contains the contents of the file
     * @return the root of the AST
     * @throws CheckstyleException if the contents is not a valid XML source
     */
    public static XmlAST parse(FileContents contents) throws CheckstyleException {
        final String fullText = contents.getText().getFullText().toString();
        final CharStream codePointCharStream = CharStreams.fromString(fullText);
        final XmlLanguageLexer lexer = new XmlLanguageLexer(codePointCharStream, true);
        lexer.removeErrorListeners();

        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final XmlLanguageParser parser = new XmlLanguageParser(tokenStream,
                XmlLanguageParser.CLEAR_DFA_LIMIT);
        parser.setErrorHandler(new CheckstyleParserErrorStrategy());
        parser.removeErrorListeners();
        parser.addErrorListener(new CheckstyleErrorListener());

        final XmlLanguageParser.DocumentContext document;
        try {
            document = parser.document();
        }
        catch (IllegalStateException ex) {
            final String exceptionMsg = String.format(Locale.ROOT,
                    "%s occurred while parsing file %s.", ex.getClass().getSimpleName(),
                    contents.getFileName());
            throw new CheckstyleException(exceptionMsg, ex);
        }

        return new XmlAstVisitor().visit(document);
    }

    /**
     * Parse a text and return the parse tree.
     *
     * @param text the text to parse
     * @return the root node of the parse tree
     * @throws CheckstyleException if the text is not a valid XML source
     */
    public static XmlAST parseFileText(FileText text) throws CheckstyleException {
        final FileContents contents = new FileContents(text);
        final XmlAST ast = parse(contents);

        return ast;
    }

    /**
     * Parses XML source file.
     *
     * @param file the file to parse
     * @return XmlAST tree
     * @throws IOException if the file could not be read
     * @throws CheckstyleException if the file is not a valid XML source file
     */
    public static XmlAST parseFile(File file) throws IOException, CheckstyleException {
        final FileText text = new FileText(file.getAbsoluteFile(), StandardCharsets.UTF_8.name());

        return parseFileText(text);
    }

    /**
     * Custom error listener to provide detailed exception message.
     */
    private static class CheckstyleErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                int charPositionInLine, String msg, RecognitionException ex) {
            throw new IllegalStateException(line + ":" + charPositionInLine + ": " + msg, ex);
        }
    }

}
