package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLSet;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLNotSubsetNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean notSubset(AMLSet left, AMLSet right) {
        return AMLBoolean.of(left.isSubset(right).isFalse());
    }
}
