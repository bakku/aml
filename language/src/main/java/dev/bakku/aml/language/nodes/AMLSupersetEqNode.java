package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLSet;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLSupersetEqNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean supersetEq(AMLSet left, AMLSet right) {
        return AMLBoolean.of(right.isSubset(left).isTrue() || left.equals(right));
    }
}
