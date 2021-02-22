package dev.bakku.aml.language.runtime;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ExportLibrary(InteropLibrary.class)
public class AMLSet implements TruffleObject {
    private final Set<Object> set;

    private AMLSet(Set<Object> set) {
        this.set = set;
    }

    public static AMLSet of(Object... values) {
        return new AMLSet(Set.of(values));
    }

    public AMLSet intersect(AMLSet other) {
        var result = new HashSet<>(other.set);
        result.retainAll(this.set);
        return new AMLSet(result);
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return set.stream()
            .map(Object::toString)
            .collect(Collectors.joining(", ", "{", "}"));
    }

    @ExportMessage
    public boolean hasArrayElements() {
        return true;
    }

    @ExportMessage
    public Object readArrayElement(long index) {
        return set.toArray()[(int) index];
    }

    @ExportMessage
    public long getArraySize() {
        return set.size();
    }

    @ExportMessage
    public boolean isArrayElementReadable(long index) {
        return index < this.getArraySize();
    }
}
