package dev.bakku.aml.language.nodes.generic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.*;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLExponentiationNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber expNumbers(AMLNumber left, AMLNumber right) {
        return left.power(right);
    }

    @Specialization
    protected AMLNumber expFractions(AMLFraction left, AMLFraction right) {
        return left.toNumber().power(right.toNumber());
    }

    @Specialization
    protected AMLNumber expNumberAndFraction(AMLNumber left, AMLFraction right) {
        return left.power(right.toNumber());
    }

    @Specialization
    protected AMLNumber expFractionAndNumber(AMLFraction left, AMLNumber right) {
        return left.toNumber().power(right);
    }

    @Specialization
    protected AMLInvokable iteratedFunction(AMLFunction func, AMLNumber right) {
        if (right.isSmaller(AMLNumber.of(1)).isTrue()) {
            throw new AMLRuntimeException("cannot iterate a function less than 1 times");
        }

        if (!right.ceil().equals(right)) {
            throw new AMLRuntimeException("cannot iterate a function using a decimal number");
        }

        if (func.arity() != 1) {
            throw new AMLRuntimeException("cannot iterate a function with more than one argument");
        }

        return new AMLIteratedFunction(func, right);
    }
}
