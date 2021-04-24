package dev.bakku.aml.language.nodes.sets;

import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLObject;
import dev.bakku.aml.language.runtime.types.AMLSet;

import java.util.Arrays;

public class AMLSetLiteralNode extends AMLBaseNode {
    @Children
    private AMLBaseNode[] elements;

    public AMLSetLiteralNode(AMLBaseNode[] elements) {
        this.elements = elements;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        var result = Arrays.stream(elements)
            .map(e -> e.executeGeneric(frame))
            .distinct()
            .map(o -> {
                if (!(o instanceof AMLObject)) {
                    throw new AMLRuntimeException("only sets, numbers, fractions, and booleans are allowed as elements of a set");
                }

                return (AMLObject) o;
            })
            .toArray(AMLObject[]::new);

        return AMLSet.of(result);
    }
}
