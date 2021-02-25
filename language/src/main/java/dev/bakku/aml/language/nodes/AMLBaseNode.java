package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.runtime.types.AMLTypes;

@TypeSystemReference(AMLTypes.class)
public abstract class AMLBaseNode extends Node {
    public abstract Object execute(VirtualFrame frame);
}
