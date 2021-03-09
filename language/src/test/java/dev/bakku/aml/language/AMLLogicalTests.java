package dev.bakku.aml.language;

import dev.bakku.aml.language.runtime.types.AMLBoolean;
import org.junit.Test;

import static dev.bakku.aml.language.TestHelper.*;
import static org.junit.Assert.assertTrue;

public class AMLLogicalTests {
    @Test
    public void equivalenceMustBeTrueForBothTrue() {
        var code = "1 = 1 ⇔ 2 = 2;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void equivalenceMustBeTrueForBothFalse() {
        var code = "1 = 2 ⇔ 2 = 3;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void equivalenceMustBeFalseWhenDifferent() {
        var code = "1 = 2 ⇔ 2 = 2;";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void implicationMustBeTrueForBothTrue() {
        var code = "1 = 1 ⇒ 2 = 2;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void implicationMustBeFalseWhenLeftTrueRightFalse() {
        var code = "1 = 1 ⇒ 2 = 3;";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void implicationMustBeTrueWhenLeftFalseAndRightTrue() {
        var code = "1 = 2 ⇒ 2 = 2;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void implicationMustBeTrueWhenLeftFalseAndRightFalse() {
        var code = "1 = 2 ⇒ 2 = 3;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void orMustBeTrueForBothTrue() {
        var code = "1 = 1 ∨ 2 = 2;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void orMustBeTrueWhenLeftTrueRightFalse() {
        var code = "1 = 1 ∨ 2 = 3;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void orMustBeTrueWhenLeftFalseAndRightTrue() {
        var code = "1 = 2 ∨ 2 = 2;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void orMustBeFalseWhenLeftFalseAndRightFalse() {
        var code = "1 = 2 ∨ 2 = 3;";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void xorMustBeFalseForBothTrue() {
        var code = "1 = 1 ⊕ 2 = 2;";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void xorMustBeTrueWhenLeftTrueRightFalse() {
        var code = "1 = 1 ⊕ 2 = 3;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void xorMustBeTrueWhenLeftFalseAndRightTrue() {
        var code = "1 = 2 ⊕ 2 = 2;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void xorMustBeFalseWhenLeftFalseAndRightFalse() {
        var code = "1 = 2 ⊕ 2 = 3;";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void andMustBeTrueForBothTrue() {
        var code = "1 = 1 ∧ 2 = 2;";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void andMustBeFalseWhenLeftTrueRightFalse() {
        var code = "1 = 1 ∧ 2 = 3;";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void andMustBeFalseWhenLeftFalseAndRightTrue() {
        var code = "1 = 2 ∧ 2 = 2;";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void andMustBeFalseWhenLeftFalseAndRightFalse() {
        var code = "1 = 2 ∧ 2 = 3;";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void negationMustMakeFalseToTrue() {
        var code = "¬(1 = 2);";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void negationMustMakeTrueToFalse() {
        var code = "¬(1 = 1);";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }
}
