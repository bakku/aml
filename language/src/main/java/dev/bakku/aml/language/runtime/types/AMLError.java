package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class AMLError implements TruffleObject {
    private final String message;

    public AMLError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return "Runtime error: " + message;
    }

    @Override
    public String toString() {
        return "AMLError{" +
            "message='" + message + '\'' +
            '}';
    }
}
