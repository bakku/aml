package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.runtime.types.AMLError;

public class AMLReturnErrorNode extends AMLBaseNode {
    private final AMLError error;

    public AMLReturnErrorNode(AMLError error) {
        this.error = error;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return this.error;
    }
}
