package dev.bakku.aml.language.runtime;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class AMLSet implements TruffleObject {
    private Set<Integer> set;

    private AMLSet(Set<Integer> set) {
        this.set = set;
    }

    public static AMLSet of(Integer... values) {
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
}
