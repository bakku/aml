package dev.bakku.aml.language.nodes.functions;

import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLNamedFunction;

import java.util.Arrays;

public class AMLDefineFunctionNode extends AMLBaseNode {
    private String name;
    private String[] parameters;
    private MaterializedFrame globalFrame;
    private AMLBaseNode bodyNode;

    public AMLDefineFunctionNode(String name, String[] parameters,
                                 AMLBaseNode bodyNode, MaterializedFrame globalFrame) {
        this.name = name;
        this.parameters = parameters;
        this.bodyNode = bodyNode;
        this.globalFrame = globalFrame;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        if (hasDuplicateParameterNames()) {
            throw new AMLRuntimeException("function with duplicate argument names");
        }

        var slot = this.globalFrame.getFrameDescriptor().findFrameSlot(this.name);

        if (slot != null) {
            throw new AMLRuntimeException("redefinition of function " + this.name);
        }

        slot = this.globalFrame.getFrameDescriptor().addFrameSlot(this.name);
        var func = new AMLNamedFunction(this.name, this.bodyNode, parameters);

        this.globalFrame.setObject(slot, func);

        return func;
    }

    private boolean hasDuplicateParameterNames() {
        return Arrays.stream(this.parameters).distinct().count() !=
            this.parameters.length;
    }
}
