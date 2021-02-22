package dev.bakku.aml.language.parser;

import dev.bakku.aml.language.AMLLexer;
import dev.bakku.aml.language.AMLParser;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import com.oracle.truffle.api.source.Source;

public class AMLSyntaxTreeParser {
    public static AMLBaseNode parseTree(Source source) {
        var lexer = new AMLLexer(CharStreams.fromString(source.getCharacters().toString()));
        var stream = new CommonTokenStream(lexer);
        var parser = new AMLParser(stream);
        var visitor = new AMLAntlrVisitor();
        return visitor.visitProgram(parser.program());
    }
}
