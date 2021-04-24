package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.nodes.AMLProgramNode;
import dev.bakku.aml.language.nodes.AMLRootNode;
import dev.bakku.aml.language.nodes.variables.AMLWriteLocalVariableNodeGen;
import dev.bakku.aml.language.runtime.AMLRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class AMLLambda implements AMLCallable {
    protected AMLBaseNode bodyNode;
    protected String[] parameters;

    public AMLLambda(AMLBaseNode bodyNode, String[] parameters) {
        this.bodyNode = bodyNode;
        this.parameters = parameters;
    }

    public Object invoke(Object... arguments) {
        if (arguments.length != arity()) {
            throw new AMLRuntimeException(arity() +
                " arguments expected but got " + arguments.length);
        }

        List<AMLBaseNode> argumentNodes = new ArrayList<>();

        for(int i = 0; i < arity(); i++) {
            final var arg = arguments[i];

            argumentNodes.add(AMLWriteLocalVariableNodeGen.create(
                new AMLBaseNode() {
                    @Override
                    public Object executeGeneric(VirtualFrame frame) {
                        return arg;
                    }
                },
                parameters[i]
            ));
        }

        argumentNodes.add(bodyNode);

        return Truffle.getRuntime().createCallTarget(
            new AMLRootNode(
                new AMLProgramNode(argumentNodes.toArray(AMLBaseNode[]::new))
            )
        ).call();
    }

    public int arity() {
        return parameters.length;
    }
}
