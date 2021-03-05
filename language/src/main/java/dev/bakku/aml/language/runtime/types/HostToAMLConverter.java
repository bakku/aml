package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import dev.bakku.aml.language.runtime.AMLRuntimeException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostToAMLConverter {
    public static AMLObject convert(Object argument) throws UnsupportedMessageException, UnsupportedTypeException {
        var library = InteropLibrary.getUncached();

        if (library.isBoolean(argument)) {
            return AMLBoolean.of(library.asBoolean(argument));
        } else if (library.isNumber(argument)) {
            return AMLNumber.of(BigDecimal.valueOf(library.asDouble(argument)));
        } else if (library.hasArrayElements(argument)) {
            List<AMLObject> list = new ArrayList<>();

            for (int i = 0; i < library.getArraySize(argument); i++) {
                try {
                    list.add(convert(library.readArrayElement(argument, i)));
                } catch (InvalidArrayIndexException ex) {
                    // Should not happen
                    throw new AMLRuntimeException("array index exceeded while converting host value");
                }
            }

            return AMLSet.of(list.toArray(AMLObject[]::new));
        }

        throw UnsupportedTypeException.create(new Object[] { argument }, "Type " + argument.getClass() + " is not supported");
    }
}
