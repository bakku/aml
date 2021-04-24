package dev.bakku.aml.language.stdlib;

import dev.bakku.aml.language.AMLContext;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLCallable;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import dev.bakku.aml.language.runtime.types.AMLSet;

public class Sets {
    public static void addSetBuiltIns(AMLContext ctx) {
        addGetIndex(ctx);
    }

    private static void addGetIndex(AMLContext ctx) {
        ctx.getGlobalFrame().setObject(
            ctx.getGlobalFrameDescriptor().addFrameSlot("ᵢ"),
            new AMLCallable() {
                @Override
                public Object invoke(Object... arguments) {
                    if (arguments.length != 2 || !(arguments[0] instanceof AMLSet) ||
                        !(arguments[1] instanceof AMLNumber)) {
                        throw new AMLRuntimeException("set and number expected");
                    }

                    var set = (AMLSet) arguments[0];
                    var idx = ((AMLNumber) arguments[1]).unwrap().intValue();
                    return set.readArrayElement(idx - 1);
                }

                @Override
                public int arity() {
                    return 2;
                }
            }
        );
    }
}
