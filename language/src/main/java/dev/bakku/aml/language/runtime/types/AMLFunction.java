package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.interop.*;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import dev.bakku.aml.language.nodes.AMLBaseNode;
import dev.bakku.aml.language.nodes.AMLProgramNode;
import dev.bakku.aml.language.nodes.AMLRootNode;
import dev.bakku.aml.language.nodes.functions.AMLWriteFunctionArgumentNode;
import dev.bakku.aml.language.runtime.AMLRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExportLibrary(InteropLibrary.class)
public class AMLFunction implements TruffleObject, AMLInvokable, AMLObject {
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
            throw new AMLRuntimeException("function " + name + " expects " + argumentNames.length + " arguments but got " + arguments.length);
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

    public int arity() {
        return argumentNames.length;
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return "<func:" + this.name + ">";
    }

    @ExportMessage
    public boolean isExecutable() {
        return true;
    }

    @ExportMessage
    public Object execute(Object[] arguments) throws UnsupportedMessageException, UnsupportedTypeException, ArityException {
        if (arguments.length != arity()) {
            throw ArityException.create(arity(), arguments.length);
        }

        List<AMLObject> args = new ArrayList<>();

        for (var arg : arguments) {
            args.add(HostToAMLConverter.convert(arg));
        }

        return invoke(args.toArray(Object[]::new));
    }
}
