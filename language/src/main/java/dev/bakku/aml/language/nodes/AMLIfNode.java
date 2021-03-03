package dev.bakku.aml.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLBoolean;

public class AMLIfNode extends AMLBaseNode {
    @Child
    private AMLBaseNode conditionNode;
    @Child
    private AMLBaseNode thenNode;
    @Child
    private AMLBaseNode elseNode;

    public AMLIfNode(AMLBaseNode conditionNode, AMLBaseNode thenNode, AMLBaseNode elseNode) {
        this.conditionNode = conditionNode;
        this.thenNode = thenNode;
        this.elseNode = elseNode;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        var result = this.conditionNode.executeGeneric(frame);

        if (!(result instanceof AMLBoolean)) {
            throw new AMLRuntimeException("condition of the if expression does not result in a boolean");
        }

        var resultAsBool = (AMLBoolean) result;

        if (resultAsBool.isTrue()) {
            return this.thenNode.executeGeneric(frame);
        } else {
            return this.elseNode.executeGeneric(frame);
        }
    }
}
