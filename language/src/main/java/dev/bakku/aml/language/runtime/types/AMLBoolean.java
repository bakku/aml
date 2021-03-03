package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
public class AMLBoolean implements TruffleObject, AMLObject {
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

    public AMLBoolean equivalence(AMLBoolean right) {
        return of(this.equals(right));
    }

    public AMLBoolean implies(AMLBoolean right) {
        if (this.isTrue()) {
            return of(right.isTrue());
        }

        return of(true);
    }

    public AMLBoolean or(AMLBoolean right) {
        return of(this.isTrue() || right.isTrue());
    }

    public AMLBoolean xor(AMLBoolean right) {
        return of(!this.equals(right));
    }

    public AMLBoolean and(AMLBoolean right) {
        return of(this.isTrue() && right.isTrue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AMLBoolean that = (AMLBoolean) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
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
