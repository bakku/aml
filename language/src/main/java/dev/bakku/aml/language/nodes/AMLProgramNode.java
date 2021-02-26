package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class AMLProgramNode extends AMLBaseNode {
    @Children
    private AMLBaseNode[] nodes;

    public AMLProgramNode(AMLBaseNode[] nodes) {
        this.nodes = nodes;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        for (int i = 0; i < (this.nodes.length - 1); i++) {
            this.nodes[i].executeGeneric(frame);
        }

        return this.nodes[this.nodes.length - 1].executeGeneric(frame);
    }
}
