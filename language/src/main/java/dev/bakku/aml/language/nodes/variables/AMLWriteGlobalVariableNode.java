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
@NodeField(name = "identifier", type = String.class)
public abstract class AMLWriteGlobalVariableNode extends AMLBaseNode {
    public abstract MaterializedFrame getGlobalFrame();
    public abstract String getIdentifier();

    @Specialization
    public Object write(Object value) {
        var slot = getGlobalFrame().getFrameDescriptor().findFrameSlot(getIdentifier());

        if (slot != null) {
            throw new AMLRuntimeException("attempt to rewrite already defined variable " +
                getIdentifier());
        }

        getGlobalFrame().setObject(
            getGlobalFrame().getFrameDescriptor().addFrameSlot(getIdentifier()),
            value
        );

        return value;
    }
}
