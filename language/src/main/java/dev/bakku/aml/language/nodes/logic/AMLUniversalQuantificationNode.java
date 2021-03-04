package dev.bakku.aml.language.nodes.logic;

import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;

public class AMLUniversalQuantificationNode extends AMLQuantificationNode {
    public AMLUniversalQuantificationNode(String identifier, AMLBaseNode initializer, AMLBaseNode body) {
        super(identifier, initializer, body);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return super.execute(
            frame,
            stream -> stream.allMatch(o -> invokeBody(o).isTrue())
        );
    }
}
