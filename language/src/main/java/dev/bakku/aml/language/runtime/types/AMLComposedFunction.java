package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class AMLComposedFunction implements TruffleObject, AMLInvokable {
    private AMLFunction outer;
    private AMLFunction inner;

    public AMLComposedFunction(AMLFunction outer, AMLFunction inner) {
        this.outer = outer;
        this.inner = inner;
    }

    @Override
    public Object invoke(Object... arguments) {
        return outer.invoke(inner.invoke(arguments));
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return "<composed_func: " +
            inner.toDisplayString(allowSideEffects) +
            " ∘ " +
            outer.toDisplayString(allowSideEffects) + ">";
    }
}
