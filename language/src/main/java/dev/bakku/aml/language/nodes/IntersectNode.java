package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.AMLSet;

@NodeChild("left")
@NodeChild("right")
public abstract class IntersectNode extends AMLBaseNode {
    @Specialization
    protected AMLSet intersectSets(AMLSet left, AMLSet right) {
        return left.intersect(right);
    }
}
