package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLSet;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLDifferenceNode extends AMLBaseNode {
    @Specialization
    protected AMLSet difference(AMLSet left, AMLSet right) {
        return left.difference(right);
    }
}
