package dev.bakku.aml.polyglot;

import org.graalvm.polyglot.Context;

import java.util.List;

public class Polyglot {
    private static String AML_ID = "aml";

    public static void main(String[] args) {
        var ctx = Context.newBuilder(AML_ID)
            .allowAllAccess(true)
            .build();

        ctx.eval(AML_ID, "x ← 1 + 2;");

        var value = ctx.getBindings(AML_ID).getMember("x");
        System.out.println(value.asInt());

        value = ctx.eval(AML_ID, "∀(x ∈ {2, 4, 6}: x mod 2 = 0);");
        System.out.println(value.asBoolean());

        value = ctx.eval(AML_ID, "1/3 · 3;");
        System.out.println(value.asLong());

        value = ctx.eval(AML_ID, "{1, ..., 10};");
        System.out.println(value.getArrayElement(0));
        System.out.println(value.getArrayElement(1));
        System.out.println(value.getArrayElement(2));
        System.out.println(value.getArrayElement(3));

        var func = ctx.eval(AML_ID, "f: (S) → S ⊂ {1, 2, 3};");

        value = func.execute(List.of(1, 2, 3));
        System.out.println(value.asBoolean());
    }
}
