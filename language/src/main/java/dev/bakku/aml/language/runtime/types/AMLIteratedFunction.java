package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class AMLIteratedFunction implements TruffleObject, AMLInvokable {
    private AMLFunction func;
    private AMLNumber iterations;

    public AMLIteratedFunction(AMLFunction func, AMLNumber iterations) {
        this.func = func;
        this.iterations = iterations;
    }

    @Override
    public Object invoke(Object... arguments) {
        Object retVal = func.invoke(arguments);

        for (int i = 1; iterations.isGreater(AMLNumber.of(i)).isTrue(); i++) {
            retVal = func.invoke(retVal);
        }

        return retVal;
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return "<iterated_func: " +
            func.toDisplayString(allowSideEffects) +
            " ^ " +
            iterations.toDisplayString(allowSideEffects) + ">";
    }
}
