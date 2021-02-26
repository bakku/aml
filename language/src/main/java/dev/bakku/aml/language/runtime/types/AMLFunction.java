package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.nodes.AMLProgramNode;
import dev.bakku.aml.language.nodes.AMLRootNode;
import dev.bakku.aml.language.nodes.AMLWriteFunctionArgumentNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;

import java.util.ArrayList;
import java.util.List;

@ExportLibrary(InteropLibrary.class)
public class AMLFunction implements TruffleObject {
    private final String name;
    private final AMLBaseNode bodyNode;
    private final String[] argumentNames;

    public AMLFunction(String name, AMLBaseNode bodyNode, String[] argumentNames) {
        this.name = name;
        this.bodyNode = bodyNode;
        this.argumentNames = argumentNames;
    }

    public Object invoke(Object... arguments) {
        if (arguments.length != argumentNames.length) {
            throw new AMLRuntimeException("function " + name + " expects " + argumentNames.length + " arguments but only got " + arguments.length);
        }

        List<AMLBaseNode> argumentNodes = new ArrayList<>();
        for(int i = 0; i < arguments.length; i++) {
            argumentNodes.add(new AMLWriteFunctionArgumentNode(argumentNames[i], arguments[i]));
        }
        argumentNodes.add(this.bodyNode);

        return Truffle.getRuntime().createCallTarget(
            new AMLRootNode(
                new AMLProgramNode(
                    argumentNodes.toArray(AMLBaseNode[]::new)
                )
            )
        ).call();
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return "<func:" + this.name + ">";
    }
}
