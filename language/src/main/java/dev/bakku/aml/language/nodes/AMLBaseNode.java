package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class AMLBaseNode extends Node {
    public abstract Object execute(VirtualFrame frame);
}
