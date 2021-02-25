package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

@NodeChild("child")
public abstract class AMLFloorNode extends AMLBaseNode {
    @Specialization
    protected AMLNumber floorNumber(AMLNumber number) {
        return number.floor();
    }

    @Specialization
    protected AMLNumber floorFraction(AMLFraction fraction) {
        return fraction.floor();
    }
}
