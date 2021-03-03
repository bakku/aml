package dev.bakku.aml.language;

import dev.bakku.aml.language.runtime.types.AMLError;
import org.junit.Test;

import static dev.bakku.aml.language.TestHelper.assertAMLNumber;
import static dev.bakku.aml.language.TestHelper.evalCode;
import static org.junit.Assert.assertTrue;

public class AMLIfTests {
    @Test
    public void testThenBranch() {
        var code =
            "f: () → if 1 = 1: 3 + 4 " +
                    "otherwise: 5 - 4;" +
            "f();";

        var result = evalCode(code);
        assertAMLNumber(7, result);
    }

    @Test
    public void testElseBranch() {
        var code =
            "f: () → if 1 ≠ 1: 3 + 4 " +
                    "otherwise: 5 - 4;" +
            "f();";

        var result = evalCode(code);
        assertAMLNumber(1, result);
    }

    @Test
    public void testNonBooleanCondition() {
        var code =
            "f: () → if 1: 3 + 4 " +
                    "otherwise: 5 - 4;" +
                    "f();";

        var result = evalCode(code);

        assertTrue(result instanceof AMLError);
    }
}
