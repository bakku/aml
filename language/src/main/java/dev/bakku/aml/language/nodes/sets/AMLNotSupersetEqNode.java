package dev.bakku.aml.language.nodes.sets;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLSet;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLNotSupersetEqNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean notSupersetEq(AMLSet left, AMLSet right) {
        return AMLBoolean.of(right.isSubset(left).isFalse() && !left.equals(right));
    }
}
