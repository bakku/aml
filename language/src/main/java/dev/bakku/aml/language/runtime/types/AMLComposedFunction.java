package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

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
    public Object toDisplayString(boolean allowSideEffects) {
        return "<composed_func>";
    }
}
