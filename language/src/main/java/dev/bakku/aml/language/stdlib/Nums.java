package dev.bakku.aml.language.stdlib;

import dev.bakku.aml.language.AMLContext;
import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLCallable;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;

import java.math.BigDecimal;
import java.math.MathContext;

public class Nums {
    public static void addNumBuiltIns(AMLContext ctx) {
        addSin(ctx);
        addCos(ctx);
        addSqrt(ctx);
        addRound(ctx);
    }

    private static void addSin(AMLContext ctx) {
        ctx.getGlobalFrame().setObject(
            ctx.getGlobalFrameDescriptor().addFrameSlot("sin"),
            new AMLCallable() {
                @Override
                public Object invoke(Object... arguments) {
                    if (arguments.length != arity()) {
                        throw new AMLRuntimeException("expected " + arity() + " arguments but got " + arguments.length);
                    }

                    if (!(arguments[0] instanceof AMLNumber))
                        throw new AMLRuntimeException("expected a number as argument");

                    var doubleVal = ((AMLNumber) arguments[0]).unwrap().doubleValue();

                    return AMLNumber.of(BigDecimal.valueOf(Math.sin(doubleVal)));
                }

                @Override
                public int arity() {
                    return 1;
                }
            }
        );
    }

    private static void addCos(AMLContext ctx) {
        ctx.getGlobalFrame().setObject(
            ctx.getGlobalFrameDescriptor().addFrameSlot("cos"),
            new AMLCallable() {
                @Override
                public Object invoke(Object... arguments) {
                    if (arguments.length != arity()) {
                        throw new AMLRuntimeException("expected " + arity() + " arguments but got " + arguments.length);
                    }

                    if (!(arguments[0] instanceof AMLNumber))
                        throw new AMLRuntimeException("expected a number as argument");

                    var doubleVal = ((AMLNumber) arguments[0]).unwrap().doubleValue();

                    return AMLNumber.of(BigDecimal.valueOf(Math.cos(doubleVal)));
                }

                @Override
                public int arity() {
                    return 1;
                }
            }
        );
    }

    private static void addSqrt(AMLContext ctx) {
        ctx.getGlobalFrame().setObject(
            ctx.getGlobalFrameDescriptor().addFrameSlot("√"),
            new AMLCallable() {
                @Override
                public Object invoke(Object... arguments) {
                    if (arguments.length != arity()) {
                        throw new AMLRuntimeException("expected " + arity() + " arguments but got " + arguments.length);
                    }

                    AMLNumber num;

                    if (arguments[0] instanceof AMLFraction) {
                        num = ((AMLFraction) arguments[0]).toNumber();
                    } else if (!(arguments[0] instanceof AMLNumber)) {
                        throw new AMLRuntimeException("expected a number or fraction as argument");
                    } else {
                        num = (AMLNumber) arguments[0];
                    }

                    return num.sqrt();
                }

                @Override
                public int arity() {
                    return 1;
                }
            }
        );
    }

    private static void addRound(AMLContext ctx) {
        ctx.getGlobalFrame().setObject(
            ctx.getGlobalFrameDescriptor().addFrameSlot("≈"),
            new AMLCallable() {
                @Override
                public Object invoke(Object... arguments) {
                    if (arguments.length != arity()) {
                        throw new AMLRuntimeException("expected " + arity() + " arguments but got " + arguments.length);
                    }

                    AMLNumber num;

                    if (arguments[0] instanceof AMLFraction) {
                        num = ((AMLFraction) arguments[0]).toNumber();
                    } else if (!(arguments[0] instanceof AMLNumber)) {
                        throw new AMLRuntimeException("expected a number or fraction as first argument");
                    } else {
                        num = (AMLNumber) arguments[0];
                    }

                    if (!(arguments[1] instanceof AMLNumber)) {
                        throw new AMLRuntimeException("expected a number as second argument");
                    }

                    int places = ((AMLNumber) arguments[1]).unwrap().intValue();
                    return AMLNumber.of(num.unwrap().round(new MathContext(places)));
                }

                @Override
                public int arity() {
                    return 2;
                }
            }
        );
    }
}
