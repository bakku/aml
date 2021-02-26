package dev.bakku.aml.language;

import dev.bakku.aml.language.runtime.types.AMLNumber;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AMLNumberTests {
    @Test
    public void testAdditionExpressions() {
        var code = "5 + 2 + 4 ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(11), result);
    }

    @Test
    public void testSubtractExpressions() {
        var code = "10 - 2 - 3 ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(5), result);
    }
}
