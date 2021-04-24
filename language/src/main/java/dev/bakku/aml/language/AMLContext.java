package dev.bakku.aml.language;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLCallable;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import dev.bakku.aml.language.runtime.types.AMLObject;
import dev.bakku.aml.language.stdlib.Nums;
import dev.bakku.aml.language.stdlib.Sets;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AMLContext {
    private final FrameDescriptor globalFrameDescriptor;
    private final MaterializedFrame globalFrame;

    public AMLContext() {
        this.globalFrameDescriptor = new FrameDescriptor();
        this.globalFrame = Truffle.getRuntime()
            .createVirtualFrame(null, this.globalFrameDescriptor)
            .materialize();
        addBuiltIns();
    }

    private void addBuiltIns() {
        this.globalFrame.setObject(
            this.globalFrameDescriptor.addFrameSlot("π"),
            AMLNumber.PI
        );

        this.globalFrame.setObject(
            this.globalFrameDescriptor.addFrameSlot("e"),
            AMLNumber.E
        );

        this.globalFrame.setObject(
            this.globalFrameDescriptor.addFrameSlot("⊤"),
            AMLBoolean.of(true)
        );

        this.globalFrame.setObject(
            this.globalFrameDescriptor.addFrameSlot("⊥"),
            AMLBoolean.of(false)
        );

        this.globalFrame.setObject(
            this.globalFrameDescriptor.addFrameSlot("print"),
            new AMLCallable() {
                @Override
                public Object invoke(Object... arguments) {
                    var output = Arrays.stream(arguments)
                        .map(arg -> (AMLObject) arg)
                        .map(obj -> obj.toDisplayString(false).toString())
                        .collect(Collectors.joining(" "));

                    System.out.println(output);
                    return output;
                }

                @Override
                public int arity() {
                    return Integer.MAX_VALUE;
                }
            }
        );

        Sets.addSetBuiltIns(this);
        Nums.addNumBuiltIns(this);
    }

    public MaterializedFrame getGlobalFrame() {
        return globalFrame;
    }

    public FrameDescriptor getGlobalFrameDescriptor() {
        return globalFrameDescriptor;
    }
}
