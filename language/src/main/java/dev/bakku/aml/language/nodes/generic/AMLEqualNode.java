package dev.bakku.aml.language.nodes.generic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import dev.bakku.aml.language.runtime.types.AMLSet;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLEqualNode extends AMLBaseNode {
    @Specialization
    protected AMLBoolean compareNumbers(AMLNumber left, AMLNumber right) {
        return AMLBoolean.of(left.equals(right));
    }

    @Specialization
    protected AMLBoolean compareFractions(AMLFraction left, AMLFraction right) {
        return AMLBoolean.of(left.equals(right));
    }

    @Specialization
    protected AMLBoolean compareNumberAndFraction(AMLNumber left, AMLFraction right) {
        return AMLBoolean.of(left.equals(right.toNumber()));
    }

    @Specialization
    protected AMLBoolean compareFractionAndNumber(AMLFraction left, AMLNumber right) {
        return AMLBoolean.of(left.toNumber().equals(right));
    }

    @Specialization
    protected AMLBoolean compareSets(AMLSet left, AMLSet right) {
        return AMLBoolean.of(left.equals(right));
    }
}
