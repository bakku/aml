package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.runtime.types.AMLNumber;

public class AMLNumberNode extends AMLBaseNode {
    private AMLNumber number;

    public AMLNumberNode(AMLNumber number) {
        this.number = number;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return this.number;
    }
}
