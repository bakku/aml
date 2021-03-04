package dev.bakku.aml.language;

import org.junit.Test;

import java.util.Set;

import static dev.bakku.aml.language.TestHelper.*;

public class AMLSetTests {
    @Test
    public void createSetsWithLiteral() {
        var code = "{⊤, ⊥, ⊤};";
        var result = evalCode(code);

        assertAMLSetWithBooleans(Set.of(true, false), result);
    }

    @Test
    public void createSetsWithEllipsis() {
        var code = "{1, ..., 4};";
        var result = evalCode(code);
        assertAMLSetWithNumbers(Set.of(1, 2, 3, 4), result);
    }

    @Test
    public void cardinalityShouldReturnCount() {
        var code = "|{1, ..., 4}|;";
        var result = evalCode(code);
        assertAMLNumber(4, result);
    }

    @Test
    public void intersectionShouldReturnCorrectSet() {
        var code = "{1, ..., 4} ∩ {3, ...,  6};";
        var result = evalCode(code);
        assertAMLSetWithNumbers(Set.of(3, 4), result);
    }

    @Test
    public void unionShouldReturnCorrectSet() {
        var code = "{1, ..., 4} ∪ {3, ...,  6};";
        var result = evalCode(code);
        assertAMLSetWithNumbers(Set.of(1, 2, 3, 4, 5, 6), result);
    }

    @Test
    public void differenceShouldReturnCorrectSet() {
        var code = "{1, ..., 4} \\ {3, ...,  6};";
        var result = evalCode(code);
        assertAMLSetWithNumbers(Set.of(1, 2), result);
    }

    @Test
    public void complexSetOperation() {
        var code = "A ← {1, ..., 10}; " +
            "B ← {3, 4, 5, 6, 7}; " +
            "C ← {1, 2, 7, 8, 11, 12}; " +
            "(A \\ B) ∩ C;";
        var result = evalCode(code);
        assertAMLSetWithNumbers(Set.of(1, 2, 8), result);
    }

    @Test
    public void subsetShouldReturnTrueIfSubset() {
        var code = "{1, 2, 3} ⊂ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void subsetShouldReturnFalseIfNotASubset() {
        var code = "{3, ..., 6} ⊂ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void subsetShouldReturnFalseIfEqual() {
        var code = "{1, ..., 4} ⊂ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void notSubsetShouldReturnFalseIfSubset() {
        var code = "{1, 2, 3} ⊄ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void notSubsetShouldReturnTrueIfNotASubset() {
        var code = "{3, ..., 6} ⊄ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void notSubsetShouldReturnTrueIfEqual() {
        var code = "{1, ..., 4} ⊄ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void subsetEqShouldReturnTrueIfSubset() {
        var code = "{1, 2, 3} ⊆ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void subsetEqShouldReturnFalseIfNotASubset() {
        var code = "{3, ..., 6} ⊆ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void subsetEqShouldReturnTrueIfEqual() {
        var code = "{1, ..., 4} ⊆ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void notSubsetEqShouldReturnFalseIfSubset() {
        var code = "{1, 2, 3} ⊈ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void notSubsetEqShouldReturnTrueIfNotASubset() {
        var code = "{3, ..., 6} ⊈ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void notSubsetEqShouldReturnFalseIfEqual() {
        var code = "{1, ..., 4} ⊈ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void supersetShouldReturnTrueIfSuperset() {
        var code = "{1, ..., 4} ⊃ {1, 2, 3};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void supersetShouldReturnFalseIfNotSuperset() {
        var code = "{3, ..., 6} ⊃ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void supersetShouldReturnFalseIfEqual() {
        var code = "{1, ..., 4} ⊃ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void notSupersetShouldReturnFalseIfSuperset() {
        var code = "{1, ..., 4} ⊅ {1, 2, 3};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void notSupersetShouldReturnTrueIfNotSuperset() {
        var code = "{3, ..., 6} ⊅ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void notSupersetShouldReturnTrueIfEqual() {
        var code = "{1, ..., 4} ⊅ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void supersetEqShouldReturnTrueIfSuperset() {
        var code = "{1, ..., 4} ⊇ {1, 2, 3};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void supersetEqShouldReturnFalseIfNotSuperset() {
        var code = "{3, ..., 6} ⊇ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void supersetEqShouldReturnTrueIfEqual() {
        var code = "{1, ..., 4} ⊇ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void notSupersetEqShouldReturnFalseIfSuperset() {
        var code = "{1, ..., 4} ⊉ {1, 2, 3};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void notSupersetEqShouldReturnTrueIfNotSuperset() {
        var code = "{3, ..., 6} ⊉ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void notSupersetEqShouldReturnFalseIfEqual() {
        var code = "{1, ..., 4} ⊉ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void equalsShouldReturnTrueWhenEqual() {
        var code = "{1, ..., 4} = {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }

    @Test
    public void equalsShouldReturnFalseWhenNotEqual() {
        var code = "{1, ..., 5} = {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void unequalsShouldReturnFalseWhenEqual() {
        var code = "{1, ..., 4} ≠ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(false, result);
    }

    @Test
    public void unequalsShouldReturnTrueWhenNotEqual() {
        var code = "{1, ..., 5} ≠ {1, ..., 4};";
        var result = evalCode(code);
        assertAMLBoolean(true, result);
    }
}
