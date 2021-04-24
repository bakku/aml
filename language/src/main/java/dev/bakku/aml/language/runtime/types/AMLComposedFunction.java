package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.*;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.util.ArrayList;
import java.util.List;

@ExportLibrary(InteropLibrary.class)
public class AMLComposedFunction implements TruffleObject, AMLCallable {
    private AMLCallable outer, inner;

    public AMLComposedFunction(AMLCallable outer, AMLCallable inner) {
        this.outer = outer;
        this.inner = inner;
    }

    @Override
    public Object invoke(Object... arguments) {
        return outer.invoke(inner.invoke(arguments));
    }

    @Override
    public int arity() {
        return 1;
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

        return invoke(HostToAMLConverter.convert(arguments[0]));
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return "<composed_func>";
    }
}
