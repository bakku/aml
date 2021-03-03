package dev.bakku.aml.language;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLNumber;

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
            this.globalFrameDescriptor.addFrameSlot("⊤"),
            AMLBoolean.of(true)
        );

        this.globalFrame.setObject(
            this.globalFrameDescriptor.addFrameSlot("⊥"),
            AMLBoolean.of(false)
        );
    }

    public MaterializedFrame getGlobalFrame() {
        return globalFrame;
    }

    public FrameDescriptor getGlobalFrameDescriptor() {
        return globalFrameDescriptor;
    }
}
