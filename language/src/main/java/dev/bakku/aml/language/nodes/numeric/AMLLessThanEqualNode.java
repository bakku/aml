package dev.bakku.aml.language.nodes.numeric;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLLessThanEqualNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean lessThanEqualNumbers(AMLNumber left, AMLNumber right) {
        return AMLBoolean.of(
            left.isSmaller(right).isTrue() || AMLBoolean.of(left.equals(right)).isTrue()
        );
    }

    @Specialization
    protected AMLBoolean lessThanEqualFractions(AMLFraction left, AMLFraction right) {
        return AMLBoolean.of(
            left.toNumber().isSmaller(right.toNumber()).isTrue() || AMLBoolean.of(left.equals(right)).isTrue()
        );
    }

    @Specialization
    protected AMLBoolean lessThanEqualNumberFraction(AMLNumber left, AMLFraction right) {
        return AMLBoolean.of(
            left.isSmaller(right.toNumber()).isTrue() || AMLBoolean.of(left.equals(right.toNumber())).isTrue()
        );
    }

    @Specialization
    protected AMLBoolean lessThanEqualFractionNumber(AMLFraction left, AMLNumber right) {
        return AMLBoolean.of(
            left.toNumber().isSmaller(right).isTrue() || AMLBoolean.of(left.toNumber().equals(right)).isTrue()
        );
    }
}
