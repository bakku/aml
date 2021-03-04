package dev.bakku.aml.language.nodes.functions;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLFunction;

import java.util.Arrays;
import java.util.Objects;

public class AMLDefineFunctionNode extends AMLBaseNode {
    private String name;
    private String[] arguments;
    private MaterializedFrame globalFrame;
    private FrameSlot frameSlot;

    @Child
    private AMLBaseNode bodyNode;

    public AMLDefineFunctionNode(String name, String[] arguments, AMLBaseNode bodyNode,
                                 MaterializedFrame globalFrame, FrameSlot frameSlot) {
        this.name = name;
        this.arguments = arguments;
        this.bodyNode = bodyNode;
        this.globalFrame = globalFrame;
        this.frameSlot = frameSlot;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        if (hasDuplicateArgumentNames()) {
            throw new AMLRuntimeException("function with duplicate argument names");
        }

        var func =  new AMLFunction(
            this.name,
            this.bodyNode,
            arguments
        );

        var slot = this.globalFrame.getFrameDescriptor().findFrameSlot(this.name);

        try {
            Objects.requireNonNull(this.globalFrame.getObject(slot));
            throw new AMLRuntimeException("redefinition of function " + this.name);
        } catch (FrameSlotTypeException | NullPointerException e) {
            // Should happen
        }

        this.globalFrame.setObject(slot, func);
        return func;
    }

    private boolean hasDuplicateArgumentNames() {
        return Arrays.stream(this.arguments).distinct().count() != this.arguments.length;
    }
}
