package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import dev.bakku.aml.language.runtime.types.AMLSet;

import java.util.ArrayList;
import java.util.List;

@NodeChild("from")
@NodeChild("to")
public abstract class AMLSetEllipsisNode extends AMLBaseNode {
    @Specialization
    protected AMLSet createSet(AMLNumber from, AMLNumber to) {
        var smaller = from;
        var bigger = to;

        if (from.isGreater(to).isTrue()) {
            smaller = to;
            bigger = from;
        }

        List<AMLNumber> nums = new ArrayList<>();

        while (!smaller.equals(bigger)) {
            nums.add(smaller);
            smaller = smaller.add(AMLNumber.of(1));
        }

        nums.add(bigger);

        return AMLSet.of(nums.toArray(AMLNumber[]::new));
    }
}
