package dev.bakku.aml.language;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.source.Source;
import dev.bakku.aml.language.nodes.AMLRootNode;
import dev.bakku.aml.language.parser.AMLSyntaxTreeParser;

public class TestHelper {
    public static Object evalCode(String code) {
        var source = Source.newBuilder("aml", code, "testcode")
            .build();
        var node = AMLSyntaxTreeParser.parseTree(new AMLContext(), source);
        return Truffle.getRuntime().createCallTarget(new AMLRootNode(node)).call();
    }
}
