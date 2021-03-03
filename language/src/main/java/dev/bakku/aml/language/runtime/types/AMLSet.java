package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ExportLibrary(InteropLibrary.class)
public class AMLSet<T extends AMLObject> implements TruffleObject, AMLObject {
    private final Set<T> set;

    private AMLSet(Set<T> set) {
        this.set = set;
    }

    public static <T extends AMLObject> AMLSet<T> of(T... values) {
        return new AMLSet<>(Set.of(values));
    }

    public AMLSet<T> intersect(AMLSet<T> other) {
        var result = new HashSet<>(other.set);
        result.retainAll(this.set);
        return new AMLSet<>(result);
    }

    public AMLBoolean contains(AMLObject o) {
        return AMLBoolean.of(set.contains(o));
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return set.stream()
            .map(i -> i.toDisplayString(allowSideEffects).toString())
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
