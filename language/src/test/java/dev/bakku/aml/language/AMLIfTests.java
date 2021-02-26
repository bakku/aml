package dev.bakku.aml.language;

import dev.bakku.aml.language.runtime.AMLRuntimeException;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AMLIfTests {
    @Test
    public void testThenBranch() {
        var code = "if 1 = 1 then 3 + 4 ; else 5 - 4 ; end";

        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(7), result);
    }

    @Test
    public void testElseBranch() {
        var code = "if 1 ≠ 1 then 3 + 4 ; else 5 - 4 ; end";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(1), result);
    }

    @Test(expected = AMLRuntimeException.class)
    public void testNonBooleanCondition() {
        var code = "if 1 then 3 + 4 ; else 5 - 4 ; end";
        TestHelper.evalCode(code);
    }
}
