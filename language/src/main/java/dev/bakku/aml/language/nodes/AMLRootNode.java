package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

public class AMLRootNode extends RootNode {
    @Child
    private AMLBaseNode root;

    public AMLRootNode(TruffleLanguage<?> language, AMLBaseNode root) {
        super(language);
        this.root = root;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return root.executeGeneric(frame);
    }
}
