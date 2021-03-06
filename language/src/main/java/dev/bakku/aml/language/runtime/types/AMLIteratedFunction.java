package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.*;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class AMLIteratedFunction implements TruffleObject, AMLCallable {
    private AMLCallable callable;
    private AMLNumber iterations;

    public AMLIteratedFunction(AMLCallable callable, AMLNumber iterations) {
        this.callable = callable;
        this.iterations = iterations;
    }

    @Override
    public int arity() {
        return this.callable.arity();
    }

    @Override
    public Object invoke(Object... arguments) {
        Object retVal = callable.invoke(arguments);

        for (int i = 1; iterations.isGreater(AMLNumber.of(i)).isTrue(); i++) {
            retVal = callable.invoke(retVal);
        }

        return retVal;
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
        return "<iterated_func>";
    }
}
