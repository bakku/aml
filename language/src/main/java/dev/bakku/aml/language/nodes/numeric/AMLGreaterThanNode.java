package dev.bakku.aml.language.nodes.numeric;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLGreaterThanNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean greaterThanNumbers(AMLNumber left, AMLNumber right) {
        return left.isGreater(right);
    }

    @Specialization
    protected AMLBoolean greaterThanFractions(AMLFraction left, AMLFraction right) {
        return left.toNumber().isGreater(right.toNumber());
    }

    @Specialization
    protected AMLBoolean greaterThanNumberFraction(AMLNumber left, AMLFraction right) {
        return left.isGreater(right.toNumber());
    }

    @Specialization
    protected AMLBoolean greaterThanFractionNumber(AMLFraction left, AMLNumber right) {
        return left.toNumber().isGreater(right);
    }
}
