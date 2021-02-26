package dev.bakku.aml.language;

import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AMLNumberTests {
    @Test
    public void testAdditionOfNumbers() {
        var code = "5 + 2 + 4 ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(11), result);
    }

    @Test
    public void testAdditionOfFractions() {
        var code = "1/2 + 2/1 + 1/3 ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLFraction);
        assertEquals(AMLFraction.of(AMLNumber.of(17), AMLNumber.of(6)), result);
    }

    @Test
    public void testAdditionOfMixedNumericObjects() {
        var code = "1/2 + 1 ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLFraction);
        assertEquals(AMLFraction.of(AMLNumber.of(3), AMLNumber.of(2)), result);

        code = "1 + 1/3 ;";
        result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLFraction);
        assertEquals(AMLFraction.of(AMLNumber.of(4), AMLNumber.of(3)), result);
    }

    @Test
    public void testSubtractExpressions() {
        var code = "10 - 2 - 3 ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(5), result);
    }
}
