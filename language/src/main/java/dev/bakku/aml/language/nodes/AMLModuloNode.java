package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLModuloNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber moduloNumbers(AMLNumber left, AMLNumber right) {
        return left.modulo(right);
    }

    @Specialization
    protected AMLNumber moduloFractions(AMLFraction left, AMLFraction right) {
        return left.toNumber().modulo(right.toNumber());
    }

    @Specialization
    protected AMLNumber moduloNumberAndFraction(AMLNumber left, AMLFraction right) {
        return left.modulo(right.toNumber());
    }

    @Specialization
    protected AMLNumber divideFractionAndNumber(AMLFraction left, AMLNumber right) {
        return left.toNumber().modulo(right);
    }
}
