package dev.bakku.aml.language.nodes;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("child")
public abstract class AMLFactorialNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber number(AMLNumber number) {
        return number.factorial();
    }
}
