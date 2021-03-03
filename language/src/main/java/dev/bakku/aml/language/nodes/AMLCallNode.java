package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLError;
import dev.bakku.aml.language.runtime.types.AMLInvokable;

public class AMLCallNode extends AMLBaseNode {
    private MaterializedFrame globalFrame;
    private String identifier;

    @Children
    private AMLBaseNode[] arguments;

    public AMLCallNode(MaterializedFrame globalFrame, String identifier, AMLBaseNode[] arguments) {
        this.globalFrame = globalFrame;
        this.identifier = identifier;
        this.arguments = arguments;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] evaledArgs = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            evaledArgs[i] = this.arguments[i].executeGeneric(frame);
        }

        var func = retrieveFunction(frame);
        var result = func.invoke(evaledArgs);

        if (result instanceof AMLError) {
            throw new AMLRuntimeException(((AMLError) result).getMessage());
        }

        return result;
    }

    private AMLInvokable retrieveFunction(VirtualFrame localFrame) {
        var obj = AMLReadVariableNode.tryRead(localFrame, globalFrame, identifier);

        if (!(obj instanceof AMLInvokable)) {
            throw new AMLRuntimeException("attempt to call something which is not a function");
        }

        return (AMLInvokable) obj;
    }
}
