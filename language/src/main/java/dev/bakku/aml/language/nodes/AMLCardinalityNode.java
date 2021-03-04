package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import dev.bakku.aml.language.runtime.types.AMLSet;

@NodeChild("set")
public abstract class AMLCardinalityNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber cardinality(AMLSet set) {
        return set.cardinality();
    }
}
