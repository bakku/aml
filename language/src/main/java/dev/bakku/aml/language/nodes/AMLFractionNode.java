package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("numerator")
@NodeChild("denominator")
public abstract class AMLFractionNode extends AMLBaseNode {
    @Specialization
    protected AMLFraction fraction(AMLNumber numerator, AMLNumber denominator) {
        return AMLFraction.of(numerator, denominator);
    }
}
