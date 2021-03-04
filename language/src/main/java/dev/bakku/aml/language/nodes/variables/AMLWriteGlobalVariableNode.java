package dev.bakku.aml.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;

import java.util.Objects;

@NodeChild("value")
@NodeField(name = "globalFrame", type = MaterializedFrame.class)
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AMLWriteGlobalVariableNode extends AMLBaseNode {
    public abstract FrameSlot getSlot();
    public abstract MaterializedFrame getGlobalFrame();

    @Specialization
    public Object write(Object value) {
        try {
            Objects.requireNonNull(getGlobalFrame().getObject(getSlot()));
            throw new AMLRuntimeException("rewrite to already defined variable " + getSlot().getIdentifier().toString());
        } catch (FrameSlotTypeException | NullPointerException e) {
            // Should happen
        }

        getGlobalFrame().setObject(getSlot(), value);
        return value;
    }
}
