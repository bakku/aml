package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLSubNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber subtractNumbers(AMLNumber left, AMLNumber right) {
        return left.subtract(right);
    }

    @Specialization
    protected AMLFraction subtractFractions(AMLFraction left, AMLFraction right) {
        return left.subtract(right);
    }

    @Specialization
    protected AMLFraction subtractNumberAndFraction(AMLNumber left, AMLFraction right) {
        return left.toFraction().subtract(right);
    }

    @Specialization
    protected AMLFraction subtractFractionAndNumber(AMLFraction left, AMLNumber right) {
        return left.subtract(right.toFraction());
    }
}
