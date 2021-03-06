package dev.bakku.aml.language.nodes.numeric;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("child")
public abstract class AMLNumNegationNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber negateNumber(AMLNumber number) {
        return number.multiply(AMLNumber.of(-1));
    }

    @Specialization
    protected AMLFraction negateFraction(AMLFraction fraction) {
        return fraction.multiply(AMLNumber.of(-1).toFraction());
    }
}
