package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExportLibrary(InteropLibrary.class)
public class AMLSet implements TruffleObject, AMLObject {
    private final LinkedHashSet<AMLObject> set;

    private AMLSet(LinkedHashSet<AMLObject> set) {
        this.set = set;
    }

    public static AMLSet of(AMLObject... values) {
        var s = new LinkedHashSet<>(Arrays.asList(values));
        return new AMLSet(s);
    }

    public AMLSet intersect(AMLSet other) {
        var result = new LinkedHashSet<>(other.set);
        result.retainAll(this.set);
        return new AMLSet(result);
    }

    public AMLSet union(AMLSet other) {
        var result = new LinkedHashSet<>(other.set);
        result.addAll(this.set);
        return new AMLSet(result);
    }

    public AMLSet difference(AMLSet other) {
        var result = new LinkedHashSet<>(this.set);
        result.removeAll(other.set);
        return new AMLSet(result);
    }

    public AMLBoolean isSubset(AMLSet right) {
        var rightIncludesAll = set.stream()
            .allMatch(o -> right.contains(o).isTrue());

        return AMLBoolean.of(rightIncludesAll && !this.equals(right));
    }

    public AMLBoolean contains(AMLObject o) {
        return AMLBoolean.of(set.contains(o));
    }

    public AMLNumber cardinality() {
        return AMLNumber.of(set.size());
    }

    public Stream<AMLObject> stream() {
        return this.set.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AMLSet amlSet = (AMLSet) o;
        return Objects.equals(set, amlSet.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(set);
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
