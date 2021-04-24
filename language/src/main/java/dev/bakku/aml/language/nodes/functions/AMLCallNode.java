package dev.bakku.aml.language.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLCallable;
import dev.bakku.aml.language.runtime.types.AMLError;

import java.util.Arrays;

public class AMLCallNode extends AMLBaseNode {
    @Child private AMLBaseNode callableVar;
    @Children private AMLBaseNode[] arguments;

    public AMLCallNode(AMLBaseNode callableVar, AMLBaseNode[] arguments) {
        this.callableVar = callableVar;
        this.arguments = arguments;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        var evaledArgs = Arrays.stream(arguments)
            .map(a -> a.executeGeneric(frame))
            .toArray();

        var result = retrieveFunction(frame).invoke(evaledArgs);

        if (result instanceof AMLError)
            throw new AMLRuntimeException(((AMLError) result).getMessage());

        return result;
    }

    private AMLCallable retrieveFunction(VirtualFrame localFrame) {
        var obj = callableVar.executeGeneric(localFrame);
        if (!(obj instanceof AMLCallable))
            throw new AMLRuntimeException("attempt to call something which is not a function");

        return (AMLCallable) obj;
    }
}
