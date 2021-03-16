package dev.bakku.aml.polyglot;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class PolyglotSeparateFile {
    private static final String AML_ID = "aml";

    public static void main(String[] args) throws IOException {
        var ctx = Context.newBuilder(AML_ID)
                .build();

        var srcFile = Objects.requireNonNull(
            PolyglotSeparateFile.class.getClassLoader().getResource("math.aml")
        );

        var source = Source.newBuilder(AML_ID, srcFile)
                .build();

        ctx.eval(source);

        var scanner = new Scanner(System.in);

        var line = scanner.nextLine().split(" ");
        Value func = null;

        switch (line[0]) {
            case "area":
                func = ctx.getBindings(AML_ID).getMember("area");
                break;
            case "circumference":
                func = ctx.getBindings(AML_ID).getMember("circumference");
                break;
        }

        var result = func.execute(Integer.parseInt(line[1]));
        System.out.println(result);
    }
}
