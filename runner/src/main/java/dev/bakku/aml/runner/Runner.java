package dev.bakku.aml.runner;

import org.graalvm.polyglot.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Runner {
    private static final String AML_ID = "aml";

    public static void main(String[] args) {
        if (args.length == 0 || args[0].strip().equals("")) {
            System.out.println("usage: ./runner [script]");
            System.exit(-1);
        }

        var code = readFile(args[0]);
        var ctx = Context.create(AML_ID);

        var retVal = ctx.eval(AML_ID, code);
        System.out.println(retVal);
    }

    private static String readFile(String path) {
        try {
            var lines = Files.readAllLines(Paths.get(path));
            return String.join("\n", lines);
        } catch (IOException ex) {
            System.out.println("File " + path + " cannot be read or does not exist");
            System.exit(-1);

            // will not be reached
            return null;
        }
    }
}
