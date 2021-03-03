package dev.bakku.aml.language;

import dev.bakku.aml.language.runtime.types.AMLBoolean;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AMLLogicalTests {
    @Test
    public void equivalenceMustBeTrueForBothTrue() {
        var code = "1 = 1 ⇔ 2 = 2;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void equivalenceMustBeTrueForBothFalse() {
        var code = "1 = 2 ⇔ 2 = 3;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void equivalenceMustBeFalseWhenDifferent() {
        var code = "1 = 2 ⇔ 2 = 2;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isFalse());
    }

    @Test
    public void implicationMustBeTrueForBothTrue() {
        var code = "1 = 1 ⇒ 2 = 2;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void implicationMustBeFalseWhenLeftTrueRightFalse() {
        var code = "1 = 1 ⇒ 2 = 3;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isFalse());
    }

    @Test
    public void implicationMustBeTrueWhenLeftFalseAndRightTrue() {
        var code = "1 = 2 ⇒ 2 = 2;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void implicationMustBeTrueWhenLeftFalseAndRightFalse() {
        var code = "1 = 2 ⇒ 2 = 3;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void orMustBeTrueForBothTrue() {
        var code = "1 = 1 ∨ 2 = 2;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void orMustBeTrueWhenLeftTrueRightFalse() {
        var code = "1 = 1 ∨ 2 = 3;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void orMustBeTrueWhenLeftFalseAndRightTrue() {
        var code = "1 = 2 ∨ 2 = 2;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void orMustBeFalseWhenLeftFalseAndRightFalse() {
        var code = "1 = 2 ∨ 2 = 3;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isFalse());
    }

    @Test
    public void xorMustBeFalseForBothTrue() {
        var code = "1 = 1 ⊕ 2 = 2;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isFalse());
    }

    @Test
    public void xorMustBeTrueWhenLeftTrueRightFalse() {
        var code = "1 = 1 ⊕ 2 = 3;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void xorMustBeTrueWhenLeftFalseAndRightTrue() {
        var code = "1 = 2 ⊕ 2 = 2;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void xorMustBeFalseWhenLeftFalseAndRightFalse() {
        var code = "1 = 2 ⊕ 2 = 3;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isFalse());
    }

    @Test
    public void andMustBeTrueForBothTrue() {
        var code = "1 = 1 ∧ 2 = 2;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isTrue());
    }

    @Test
    public void andMustBeFalseWhenLeftTrueRightFalse() {
        var code = "1 = 1 ∧ 2 = 3;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isFalse());
    }

    @Test
    public void andMustBeFalseWhenLeftFalseAndRightTrue() {
        var code = "1 = 2 ∧ 2 = 2;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isFalse());
    }

    @Test
    public void andMustBeFalseWhenLeftFalseAndRightFalse() {
        var code = "1 = 2 ∧ 2 = 3;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLBoolean);
        assertTrue(((AMLBoolean) result).isFalse());
    }
}
