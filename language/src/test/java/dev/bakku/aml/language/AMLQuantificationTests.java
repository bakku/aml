package dev.bakku.aml.language;

import dev.bakku.aml.language.runtime.types.AMLError;
import org.junit.Test;

import static dev.bakku.aml.language.TestHelper.*;
import static org.junit.Assert.*;

public class AMLQuantificationTests {
    @Test
    public void universalShouldReturnTrueIfHoldsForAll() {
        var code = "∀(x ∈ {2, 4, 6}: x mod 2 = 0);";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void universalShouldReturnFalseIfDoesNotHoldForAll() {
        var code = "∀(x ∈ {2, 3, 4, 6}: x mod 2 = 0);";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void existentialQuantificationShouldReturnTrueIfHoldsForAtLeastOne() {
        var code = "∃(x ∈ {2, 3, 6}: x mod 2 = 0);";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void existentialQuantificationShouldReturnFalseIfHoldsForNone() {
        var code = "∃(x ∈ {1, 3, 5}: x mod 2 = 0);";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void uniquenessQuantificationShouldReturnTrueIfHoldsForOne() {
        var code = "∃!(x ∈ {2, 3, 7}: x mod 2 = 0);";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void uniquenessQuantificationShouldReturnFalseIfHoldsForMoreThanOne() {
        var code = "∃!(x ∈ {2, 4, 7}: x mod 2 = 0);";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void uniquenessQuantificationShouldReturnFalseIfHoldsForNone() {
        var code = "∃!(x ∈ {1, 3, 5}: x mod 2 = 0);";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void quantificationShouldReturnAnErrorIfVarInBodyDoesNotExist() {
        var code = "∀(x ∈ {2, 4, 6}: y mod 2 = 0);";
        var result = evalCode(code);
        assertTrue(result instanceof AMLError);
        assertNotEquals("body of quantification must return a boolean", ((AMLError) result).getMessage());
    }

    @Test
    public void quantificationShouldReturnAnErrorIfInitializerIsNotASet() {
        var code = "a ← 2;" +
            "∀(x ∈ a: x mod 2 = 0);";
        var result = evalCode(code);
        assertTrue(result instanceof AMLError);
    }

    @Test
    public void quantificationShouldReturnAnErrorIfBodyDoesNotReturnABool() {
        var code = "∀(x ∈ {2, 3, 4}: x mod 2);";
        var result = evalCode(code);
        assertTrue(result instanceof AMLError);
    }
}
