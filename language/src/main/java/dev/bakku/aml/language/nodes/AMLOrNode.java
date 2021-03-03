package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLBoolean;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLOrNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean or(AMLBoolean left, AMLBoolean right) {
        return left.or(right);
    }
}
