package dev.bakku.aml.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;

import java.util.Objects;

@NodeField(name = "globalFrame", type = MaterializedFrame.class)
@NodeField(name = "identifier", type = String.class)
public abstract class AMLReadVariableNode extends AMLBaseNode {
    public abstract String getIdentifier();
    public abstract MaterializedFrame getGlobalFrame();

    @Specialization
    protected Object read(VirtualFrame frame) {
        return readVar(frame, getGlobalFrame(), getIdentifier());
    }

    public Object readVar(VirtualFrame localFrame,
                                 MaterializedFrame globalFrame, String identifier) {
        try {
            var slot = localFrame.getFrameDescriptor().findFrameSlot(identifier);
            return Objects.requireNonNull(localFrame.getObject(slot));
        } catch (NullPointerException | FrameSlotTypeException ex) {
            // local read failed, try global read
            return globalRead(globalFrame, identifier);
        }
    }

    private Object globalRead(MaterializedFrame globalFrame, String identifier) {
        try {
            var slot = globalFrame.getFrameDescriptor().findFrameSlot(identifier);
            return Objects.requireNonNull(globalFrame.getObject(slot));
        } catch (NullPointerException | FrameSlotTypeException ex) {
            throw new AMLRuntimeException("could not get variable or function: " + identifier);
        }
    }
}
