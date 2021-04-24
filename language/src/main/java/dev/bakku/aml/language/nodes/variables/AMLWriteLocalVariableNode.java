package dev.bakku.aml.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;

@NodeChild("value")
@NodeField(name = "identifier", type = String.class)
public abstract class AMLWriteLocalVariableNode extends AMLBaseNode {
    protected abstract String getIdentifier();

    @Specialization
    protected Object write(VirtualFrame frame, Object value) {
        frame.setObject(
            frame.getFrameDescriptor().addFrameSlot(getIdentifier()),
            value
        );

        return value;
    }
}
