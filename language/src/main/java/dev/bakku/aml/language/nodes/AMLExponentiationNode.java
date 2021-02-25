package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLExponentiationNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber expNumbers(AMLNumber left, AMLNumber right) {
        return left.power(right);
    }

    @Specialization
    protected AMLNumber expFractions(AMLFraction left, AMLFraction right) {
        return left.toNumber().power(right.toNumber());
    }

    @Specialization
    protected AMLNumber expNumberAndFraction(AMLNumber left, AMLFraction right) {
        return left.power(right.toNumber());
    }

    @Specialization
    protected AMLNumber expFractionAndNumber(AMLFraction left, AMLNumber right) {
        return left.toNumber().power(right);
    }
}
