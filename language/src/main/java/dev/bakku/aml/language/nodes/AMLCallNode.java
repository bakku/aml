package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLError;
import dev.bakku.aml.language.runtime.types.AMLFunction;

import java.util.Objects;

public class AMLCallNode extends AMLBaseNode {
    private MaterializedFrame globalFrame;
    private FrameSlot slot;

    @Children
    private AMLBaseNode[] arguments;

    public AMLCallNode(MaterializedFrame globalFrame, FrameSlot slot, AMLBaseNode[] arguments) {
        this.globalFrame = globalFrame;
        this.slot = slot;
        this.arguments = arguments;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] evaledArgs = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            evaledArgs[i] = this.arguments[i].executeGeneric(frame);
        }

        try {
            var func = Objects.requireNonNull(this.globalFrame.getObject(this.slot));

            if (!(func instanceof AMLFunction)) {
                throw new AMLRuntimeException("attempt to call something which is not a function");
            }

            var result = ((AMLFunction) func).invoke(evaledArgs);

            if (result instanceof AMLError) {
                throw new AMLRuntimeException(((AMLError) result).getMessage());
            }

            return result;
        } catch (FrameSlotTypeException | NullPointerException e) {
            throw new AMLRuntimeException("function " + this.slot.getIdentifier().toString() + " does not exist");
        }
    }
}
