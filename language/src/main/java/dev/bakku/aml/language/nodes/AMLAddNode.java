package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLAddNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber addNumbers(AMLNumber left, AMLNumber right) {
        return left.add(right);
    }

    @Specialization
    protected AMLFraction addFractions(AMLFraction left, AMLFraction right) {
        return left.add(right);
    }

    @Specialization
    protected AMLFraction addNumberAndFraction(AMLNumber left, AMLFraction right) {
        return left.toFraction().add(right);
    }

    @Specialization
    protected AMLFraction addFractionAndNumber(AMLFraction left, AMLNumber right) {
        return left.add(right.toFraction());
    }
}
