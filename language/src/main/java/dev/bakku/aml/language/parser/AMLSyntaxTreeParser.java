package dev.bakku.aml.language.parser;

import com.oracle.truffle.api.source.Source;
import dev.bakku.aml.language.AMLContext;
import dev.bakku.aml.language.AMLLexer;
import dev.bakku.aml.language.AMLParser;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class AMLSyntaxTreeParser {
    public static AMLBaseNode parseTree(AMLContext ctx, Source source) {
        var listener = new AMLErrorListener();
        var code = source.getCharacters().toString();
        var lexer = new AMLLexer(CharStreams.fromString(code));
        lexer.addErrorListener(listener);
        var stream = new CommonTokenStream(lexer);
        var parser = new AMLParser(stream);
        parser.addErrorListener(listener);
        var tree = parser.program();

        if (listener.hasErrorOccurred()) {
            throw new AMLParserException("parsing completed with errors");
        }

        var visitor = new AMLAntlrVisitor(ctx);
        return visitor.visitProgram(tree);
    }
}
