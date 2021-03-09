package dev.bakku.aml.language.nodes.logic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLBoolean;

@NodeChild("value")
public abstract class AMLNegationNode extends AMLBaseNode {
    @Specialization
    public AMLBoolean negate(AMLBoolean bool) {
        return AMLBoolean.of(!bool.isTrue());
    }
}
