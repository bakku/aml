package dev.bakku.aml.language.nodes.logic;

import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.*;

import java.util.function.Function;
import java.util.stream.Stream;

public abstract class AMLQuantificationNode extends AMLBaseNode {
    @Child
    protected AMLBaseNode initializer;
    private AMLLambda lambda;

    protected AMLQuantificationNode(String identifier, AMLBaseNode initializer, AMLBaseNode body) {
        this.initializer = initializer;
        this.lambda = new AMLLambda(body, new String[] { identifier });
    }

    protected AMLBoolean execute(VirtualFrame frame, Function<Stream<AMLObject>, Boolean> quantification) {
        var initObj = initializer.executeGeneric(frame);

        if (!(initObj instanceof AMLSet)) {
            throw new AMLRuntimeException("only sets are allowed as quantification initializer");
        }

        return AMLBoolean.of(quantification.apply(((AMLSet) initObj).stream()));
    }

    protected AMLBoolean invokeBody(AMLObject obj) {
        var result = lambda.invoke(obj);

        if (result instanceof AMLError) {
            throw new AMLRuntimeException(((AMLError) result).getMessage());
        }

        if (!(result instanceof AMLBoolean)) {
            throw new AMLRuntimeException("body of quantification must return a boolean");
        }

        return (AMLBoolean) result;
    }
}
