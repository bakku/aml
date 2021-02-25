package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.runtime.types.AMLSet;

public class AMLSetLiteralNode extends AMLBaseNode {
    private AMLSet amlSet;

    public AMLSetLiteralNode(AMLSet amlSet) {
        this.amlSet = amlSet;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return this.amlSet;
    }
}
