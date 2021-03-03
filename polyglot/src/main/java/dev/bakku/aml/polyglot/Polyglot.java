package dev.bakku.aml.polyglot;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;

public class Polyglot {
    private static String AML_ID = "aml";

    public static void main(String[] args) {
        var ctx = Context.newBuilder(AML_ID)
            .build();

        var func = ctx.eval(AML_ID, "f: (a) → a + 1;");
        System.out.println(func.canExecute());
        System.out.println(func.execute(new ProxyArray() {
            private int[] arr = new int[] {1, 2, 3};

            @Override
            public Object get(long index) {
                return null;
            }

            @Override
            public void set(long index, Value value) {
            }

            @Override
            public long getSize() {
                return arr.length;
            }
        }));
    }
}
