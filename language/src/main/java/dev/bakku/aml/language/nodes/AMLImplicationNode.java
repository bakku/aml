package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLBoolean;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLImplicationNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean implication(AMLBoolean left, AMLBoolean right) {
        return left.implies(right);
    }
}
