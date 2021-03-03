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
}
