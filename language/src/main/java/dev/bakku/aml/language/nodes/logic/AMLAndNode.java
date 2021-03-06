package dev.bakku.aml.language.nodes.logic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLBoolean;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLAndNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean or(AMLBoolean left, AMLBoolean right) {
        return left.and(right);
    }
}
