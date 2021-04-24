package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

public abstract class AMLBaseNode extends Node {
    public abstract Object executeGeneric(VirtualFrame frame);
}
