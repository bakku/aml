package dev.bakku.aml.language.nodes.sets;

import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.*;

public class AMLSetBuilderNode extends AMLBaseNode {
    @Child
    private AMLBaseNode initializer;
    private AMLLambda lambda;

    public AMLSetBuilderNode(String identifier, AMLBaseNode initializer, AMLBaseNode body) {
        this.initializer = initializer;
        this.lambda = new AMLLambda(body, new String[] { identifier });
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        var initObj = initializer.executeGeneric(frame);

        if (!(initObj instanceof AMLSet)) {
            throw new AMLRuntimeException("only sets are allowed as set builder initializer");
        }

        var set = ((AMLSet) initObj).stream()
            .filter(o -> invokeBody(o).isTrue())
            .toArray(AMLObject[]::new);

        return AMLSet.of(set);
    }

    private AMLBoolean invokeBody(AMLObject obj) {
        var result = lambda.invoke(obj);

        if (result instanceof AMLError) {
            throw new AMLRuntimeException(((AMLError) result).getMessage());
        }

        if (!(result instanceof AMLBoolean)) {
            throw new AMLRuntimeException("body of set builder must return a boolean");
        }

        return (AMLBoolean) result;
    }
}
