package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLSet;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLIntersectionNode extends AMLBaseNode {
    @Specialization
    protected AMLSet intersect(AMLSet left, AMLSet right) {
        return left.intersect(right);
    }
}
