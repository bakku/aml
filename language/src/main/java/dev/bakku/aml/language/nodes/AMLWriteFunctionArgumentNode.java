package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class AMLWriteFunctionArgumentNode extends AMLBaseNode {
    private String name;
    private Object value;

    public AMLWriteFunctionArgumentNode(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        frame.setObject(
            frame.getFrameDescriptor()
                .addFrameSlot(name),
            this.value
        );

        return this.value;
    }
}
