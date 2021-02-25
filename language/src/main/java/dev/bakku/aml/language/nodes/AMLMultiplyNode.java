package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLMultiplyNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber multiplyNumbers(AMLNumber left, AMLNumber right) {
        return left.multiply(right);
    }

    @Specialization
    protected AMLFraction multiplyFractions(AMLFraction left, AMLFraction right) {
        return left.multiply(right);
    }

    @Specialization
    protected AMLFraction divideNumberAndFraction(AMLNumber left, AMLFraction right) {
        return left.toFraction().multiply(right);
    }

    @Specialization
    protected AMLFraction divideFractionAndNumber(AMLFraction left, AMLNumber right) {
        return left.multiply(right.toFraction());
    }
}
