package dev.bakku.aml.language;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class AMLGlobalScope implements TruffleObject {
    AMLContext ctx;

    public AMLGlobalScope(AMLContext ctx) {
        this.ctx = ctx;
    }

    @ExportMessage
    public boolean hasLanguage() {
        return true;
    }

    @ExportMessage
    public Class<? extends TruffleLanguage<?>> getLanguage() {
        return AMLLanguage.class;
    }

    @ExportMessage
    public boolean hasMembers() {
        return true;
    }

    @ExportMessage
    public Object readMember(String member) throws UnknownIdentifierException {
        try {
            return this.ctx.getGlobalFrame().getObject(
                this.ctx.getGlobalFrame().getFrameDescriptor().findFrameSlot(member)
            );
        } catch (FrameSlotTypeException ex) {
            throw UnknownIdentifierException.create(member);
        }
    }

    @ExportMessage
    public boolean isMemberReadable(String member) {
        return this.ctx.getGlobalFrame().getFrameDescriptor().findFrameSlot(member) != null;
    }

    @ExportMessage
    public Object getMembers(boolean includeInternal) {
        return this.ctx.getGlobalFrame().getFrameDescriptor().getIdentifiers();
    }

    @ExportMessage
    public boolean isScope() {
        return true;
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return "global scope";
    }
}
