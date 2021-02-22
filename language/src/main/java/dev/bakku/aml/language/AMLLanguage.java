package dev.bakku.aml.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import dev.bakku.aml.language.nodes.AMLRootNode;
import dev.bakku.aml.language.parser.AMLSyntaxTreeParser;

/**
 * For context policy see:
 *  https://www.graalvm.org/truffle/javadoc/com/oracle/truffle/api/TruffleLanguage.html
 *  https://www.graalvm.org/truffle/javadoc/org/graalvm/polyglot/Context.html
 *  https://www.graalvm.org/truffle/javadoc/com/oracle/truffle/api/TruffleLanguage.ContextPolicy.html
 *
 *  Open questions:
 *   Has the file type detector any relevance?
 */
@TruffleLanguage.Registration(id = AMLLanguage.ID, name = AMLLanguage.NAME,
    defaultMimeType = AMLLanguage.MIME_TYPE, characterMimeTypes = AMLLanguage.MIME_TYPE,
    contextPolicy = TruffleLanguage.ContextPolicy.EXCLUSIVE, fileTypeDetectors = AMLFileDetector.class)
public class AMLLanguage extends TruffleLanguage<AMLContext> {
    public static final String ID = "aml";
    public static final String NAME = "AML";
    public static final String MIME_TYPE = "application/x-aml";

    @Override
    protected AMLContext createContext(Env env) {
        return new AMLContext();
    }

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        var node = AMLSyntaxTreeParser.parseTree(request.getSource());
        return Truffle.getRuntime().createCallTarget(new AMLRootNode(this, node));
    }
}