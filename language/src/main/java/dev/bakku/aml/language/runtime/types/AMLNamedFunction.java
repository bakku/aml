package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.*;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import dev.bakku.aml.language.nodes.AMLBaseNode;

import java.util.ArrayList;
import java.util.List;

@ExportLibrary(InteropLibrary.class)
public class AMLNamedFunction extends AMLLambda implements TruffleObject {
    private final String name;

    public AMLNamedFunction(String name, AMLBaseNode bodyNode, String[] parameters) {
        super(bodyNode, parameters);
        this.name = name;
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
