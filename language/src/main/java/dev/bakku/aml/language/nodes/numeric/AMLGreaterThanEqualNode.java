package dev.bakku.aml.language.nodes.numeric;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLGreaterThanEqualNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean greaterThanEqualNumbers(AMLNumber left, AMLNumber right) {
        return AMLBoolean.of(
            left.isGreater(right).isTrue() || AMLBoolean.of(left.equals(right)).isTrue()
        );
    }

    @Specialization
    protected AMLBoolean greaterThanEqualFractions(AMLFraction left, AMLFraction right) {
        return AMLBoolean.of(
            left.toNumber().isGreater(right.toNumber()).isTrue() || AMLBoolean.of(left.equals(right)).isTrue()
        );
    }

    @Specialization
    protected AMLBoolean greaterThanEqualNumberFraction(AMLNumber left, AMLFraction right) {
        return AMLBoolean.of(
            left.isGreater(right.toNumber()).isTrue() || AMLBoolean.of(left.equals(right.toNumber())).isTrue()
        );
    }

    @Specialization
    protected AMLBoolean greaterThanEqualFractionNumber(AMLFraction left, AMLNumber right) {
        return AMLBoolean.of(
            left.toNumber().isGreater(right).isTrue() || AMLBoolean.of(left.toNumber().equals(right)).isTrue()
        );
    }
}
