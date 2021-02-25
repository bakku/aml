package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLDivideNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber divideNumbers(AMLNumber left, AMLNumber right) {
        return left.divide(right);
    }

    @Specialization
    protected AMLFraction divideFractions(AMLFraction left, AMLFraction right) {
        return left.divide(right);
    }

    @Specialization
    protected AMLFraction divideNumberAndFraction(AMLNumber left, AMLFraction right) {
        return left.toFraction().divide(right);
    }

    @Specialization
    protected AMLFraction divideFractionAndNumber(AMLFraction left, AMLNumber right) {
        return left.divide(right.toFraction());
    }
}
