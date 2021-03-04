package dev.bakku.aml.language.nodes.sets;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLSet;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLNotSubsetEqNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean notSubsetEq(AMLSet left, AMLSet right) {
        return AMLBoolean.of(left.isSubset(right).isFalse() && !left.equals(right));
    }
}
