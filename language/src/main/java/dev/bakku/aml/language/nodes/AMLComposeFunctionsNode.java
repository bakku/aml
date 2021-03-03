package dev.bakku.aml.language.nodes;


import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLComposedFunction;
import dev.bakku.aml.language.runtime.types.AMLFunction;

@NodeChild("left")
@NodeChild("right")
public abstract class AMLComposeFunctionsNode extends AMLBaseNode {
    @Specialization
    public AMLComposedFunction composeFunctions(AMLFunction left, AMLFunction right) {
        if (left.arity() != 1 && right.arity() != 1) {
            throw new AMLRuntimeException("can only compose functions which take one argument");
        }

        return new AMLComposedFunction(left, right);
    }
}
