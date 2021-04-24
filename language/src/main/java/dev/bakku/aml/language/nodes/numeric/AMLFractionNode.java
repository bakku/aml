package dev.bakku.aml.language.nodes.numeric;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("numerator")
@NodeChild("denominator")
public abstract class AMLFractionNode extends AMLBaseNode {
    @Specialization
    protected AMLFraction fractionOfNumbers(AMLNumber numerator, AMLNumber denominator) {
        return AMLFraction.of(numerator, denominator);
    }

    @Specialization
    protected AMLFraction fractionOfFractionAndNumber(AMLFraction numerator, AMLNumber denominator) {
        return AMLFraction.of(numerator.toNumber(), denominator);
    }

    @Specialization
    protected AMLFraction fractionOfNumberAndFraction(AMLNumber numerator, AMLFraction denominator) {
        return AMLFraction.of(numerator, denominator.toNumber());
    }

    @Specialization
    protected AMLFraction fractionOfFractions(AMLFraction numerator, AMLFraction denominator) {
        return AMLFraction.of(numerator.toNumber(), denominator.toNumber());
    }
}
