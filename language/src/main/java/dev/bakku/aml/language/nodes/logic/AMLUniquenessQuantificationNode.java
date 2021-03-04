package dev.bakku.aml.language.nodes.logic;

import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;

public class AMLUniquenessQuantificationNode extends AMLQuantificationNode {
    public AMLUniquenessQuantificationNode(String identifier, AMLBaseNode initializer, AMLBaseNode body) {
        super(identifier, initializer, body);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return super.execute(
            frame,
            stream -> stream.filter(o -> invokeBody(o).isTrue()).count() == 1
        );
    }
}
