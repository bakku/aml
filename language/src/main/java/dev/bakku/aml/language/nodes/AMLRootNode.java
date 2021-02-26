package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLError;

public class AMLRootNode extends RootNode {
    @Child
    private AMLBaseNode root;

    public AMLRootNode(AMLBaseNode root) {
        super(null);
        this.root = root;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        try {
            return root.executeGeneric(frame);
        } catch (AMLRuntimeException ex) {
            return new AMLError(ex.getMessage());
        }
    }
}
