package dev.bakku.aml.language;

import ch.obermuhlner.math.big.BigDecimalMath;
import dev.bakku.aml.language.runtime.types.AMLError;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import org.junit.Test;

import java.math.MathContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AMLGlobalVariableTests {
    @Test
    public void readGlobalVariable() {
        var code = "π ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(BigDecimalMath.pi(new MathContext(100))), result);
    }

    @Test
    public void writeGlobalVariable() {
        var code = "A ← 1 + 2 ; A + 3 ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(6), result);
    }

    @Test
    public void variablesShouldBeImmutable() {
        var code = "A ← 1 + 2 ; A ← 4 ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }
}
