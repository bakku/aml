package dev.bakku.aml.repl;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import java.util.Scanner;

public class REPL {
    private static final String AML_ID = "aml";

    public static void main(String[] args) {
        var ctx = Context.create(AML_ID);
        var scanner = new Scanner(System.in);

        while(true) {
            System.out.print("> ");
            var code = scanner.nextLine();

            if (code.toLowerCase().strip().equals("exit")) {
                break;
            }

            if (code.strip().equals("")) {
                continue;
            }

            Value retVal;

            try {
                retVal = ctx.eval(AML_ID, code);
            } catch (PolyglotException ex) {
                ex.printStackTrace();
                continue;
            }

            /*
            Casting:
                var set = new HashSet<>(retVal.as(new TypeLiteral<List<Integer>>() {}));
                set.forEach(System.out::println);
             */

            System.out.println("=> " + retVal);
        }
    }

}
