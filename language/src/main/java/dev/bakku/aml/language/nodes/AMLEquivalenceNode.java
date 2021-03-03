package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLBoolean;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLEquivalenceNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean equivalence(AMLBoolean left, AMLBoolean right) {
        return left.equivalence(right);
    }
}
