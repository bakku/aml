package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLSet;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLSubsetEqNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean subsetEq(AMLSet left, AMLSet right) {
        return AMLBoolean.of(left.isSubset(right).isTrue() || left.equals(right));
    }
}
