package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.runtime.AMLRuntimeException;

import java.util.Objects;

@NodeChild("value")
@NodeField(name = "identifier", type = String.class)
public abstract class AMLWriteLocalValueNode extends AMLBaseNode {
    public abstract String getIdentifier();

    @Specialization
    protected Object write(VirtualFrame frame, Object value) {
        frame.setObject(
            frame.getFrameDescriptor().addFrameSlot(getIdentifier()),
            value
        );

        return value;
    }
}
