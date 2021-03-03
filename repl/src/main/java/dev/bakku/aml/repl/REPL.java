package dev.bakku.aml.repl;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import java.util.Scanner;

public class REPL {
    private static final String AML_ID = "aml";

    public static void main(String[] args) {
        var ctx = Context.create(AML_ID);
        var reader = LineReaderBuilder.builder()
            .build();

        while(true) {
            var code = reader.readLine("> ");

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

            System.out.println("=> " + retVal);
        }
    }

}
