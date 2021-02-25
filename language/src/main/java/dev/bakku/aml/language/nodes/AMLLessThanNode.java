package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLLessThanNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean lessThanNumbers(AMLNumber left, AMLNumber right) {
        return left.isSmaller(right);
    }

    @Specialization
    protected AMLBoolean lessThanFractions(AMLFraction left, AMLFraction right) {
        return left.toNumber().isSmaller(right.toNumber());
    }

    @Specialization
    protected AMLBoolean lessThanNumberFraction(AMLNumber left, AMLFraction right) {
        return left.isSmaller(right.toNumber());
    }

    @Specialization
    protected AMLBoolean lessThanFractionNumber(AMLFraction left, AMLNumber right) {
        return left.toNumber().isSmaller(right);
    }
}
