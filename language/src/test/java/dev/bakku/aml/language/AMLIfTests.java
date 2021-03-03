package dev.bakku.aml.language;

import dev.bakku.aml.language.runtime.types.AMLError;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AMLIfTests {
    @Test
    public void testThenBranch() {
        var code =
            "f: () → if 1 = 1: 3 + 4 " +
                    "otherwise: 5 - 4;" +
            "f();";

        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(7), result);
    }

    @Test
    public void testElseBranch() {
        var code =
            "f: () → if 1 ≠ 1: 3 + 4 " +
                    "otherwise: 5 - 4;" +
            "f();";

        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(1), result);
    }

    @Test
    public void testNonBooleanCondition() {
        var code =
            "f: () → if 1: 3 + 4 " +
                    "otherwise: 5 - 4;" +
                    "f();";

        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }
}
