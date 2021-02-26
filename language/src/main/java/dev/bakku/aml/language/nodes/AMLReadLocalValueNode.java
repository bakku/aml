package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.runtime.AMLRuntimeException;

import java.util.Objects;

@NodeField(name = "globalFrame", type = MaterializedFrame.class)
@NodeField(name = "identifier", type = String.class)
public abstract class AMLReadLocalValueNode extends AMLBaseNode {
    public abstract String getIdentifier();
    public abstract MaterializedFrame getGlobalFrame();

    @Specialization
    protected Object read(VirtualFrame frame) {
        try {
            var slot = frame.getFrameDescriptor().findFrameSlot(getIdentifier());
            return Objects.requireNonNull(frame.getObject(slot));
        } catch (NullPointerException | FrameSlotTypeException ex) {
            // local read failed, try global read
            return globalRead();
        }
    }

    protected Object globalRead() {
        try {
            var slot = getGlobalFrame().getFrameDescriptor().findFrameSlot(getIdentifier());
            return Objects.requireNonNull(getGlobalFrame().getObject(slot));
        } catch (NullPointerException | FrameSlotTypeException ex) {
            throw new AMLRuntimeException("could not read value: " + getIdentifier());
        }
    }
}
