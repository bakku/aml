package dev.bakku.aml.language;

import org.junit.Test;

import static dev.bakku.aml.language.TestHelper.*;

public class AMLNumberTests {
    @Test
    public void testAdditionOfNumbers() {
        var code = "5 + 2 + 4 ;";
        var result = evalCode(code);
        assertAMLNumber(11, result);
    }

    @Test
    public void testAdditionOfFractions() {
        var code = "1/2 + 2/1 + 1/3 ;";
        var result = evalCode(code);
        assertAMLFraction(17, 6, result);
    }

    @Test
    public void testAdditionOfMixedNumericObjects() {
        var code = "1/2 + 1 ;";
        var result = evalCode(code);
        assertAMLFraction(3, 2, result);

        code = "1 + 1/3 ;";
        result = evalCode(code);
        assertAMLFraction(4, 3, result);
    }

    @Test
    public void testSubtractExpressions() {
        var code = "10 - 2 - 3 ;";
        var result = evalCode(code);
        assertAMLNumber(5, result);

    }
}
