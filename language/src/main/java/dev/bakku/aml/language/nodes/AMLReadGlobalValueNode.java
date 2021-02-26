package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import dev.bakku.aml.language.runtime.AMLRuntimeException;

import java.util.Objects;

@NodeField(name = "globalFrame", type = MaterializedFrame.class)
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AMLReadGlobalValueNode extends AMLBaseNode {
    public abstract FrameSlot getSlot();
    public abstract MaterializedFrame getGlobalFrame();

    @Specialization
    protected Object read() {
        try {
            return Objects.requireNonNull(getGlobalFrame().getObject(getSlot()));
        } catch (NullPointerException | FrameSlotTypeException ex) {
            throw new AMLRuntimeException("could not read value: " + getSlot().getIdentifier().toString());
        }
    }
}
