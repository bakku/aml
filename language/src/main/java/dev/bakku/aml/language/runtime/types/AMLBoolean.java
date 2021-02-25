package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class AMLBoolean implements TruffleObject {
    private static final AMLBoolean TRUE = new AMLBoolean(true);
    private static final AMLBoolean FALSE = new AMLBoolean(false);

    private final boolean value;

    private AMLBoolean(boolean value) {
        this.value = value;
    }

    public static AMLBoolean of(boolean value) {
        return value ? TRUE : FALSE;
    }

    public boolean isTrue() {
        return value;
    }

    public boolean isFalse() {
        return !value;
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        if (isTrue()) {
            return "⊤";
        } else {
            return "⊥";
        }
    }
}
