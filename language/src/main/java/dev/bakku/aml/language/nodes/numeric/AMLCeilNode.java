package dev.bakku.aml.language.nodes.numeric;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("child")
public abstract class AMLCeilNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber ceilNumber(AMLNumber number) {
        return number.ceil();
    }

    @Specialization
    protected AMLNumber ceilFraction(AMLFraction fraction) {
        return fraction.ceil();
    }
}
