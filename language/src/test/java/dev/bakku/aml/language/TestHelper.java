package dev.bakku.aml.language;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.source.Source;
import dev.bakku.aml.language.nodes.AMLRootNode;
import dev.bakku.aml.language.parser.AMLSyntaxTreeParser;
import dev.bakku.aml.language.runtime.types.AMLBoolean;
import dev.bakku.aml.language.runtime.types.AMLFraction;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import dev.bakku.aml.language.runtime.types.AMLSet;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestHelper {
    public static Object evalCode(String code) {
        var source = Source.newBuilder("aml", code, "testcode")
            .build();
        var node = AMLSyntaxTreeParser.parseTree(new AMLContext(), source);
        return Truffle.getRuntime().createCallTarget(new AMLRootNode(node)).call();
    }

    public static void assertAMLNumber(int expected, Object actual) {
        assertTrue(actual instanceof AMLNumber);
        assertEquals(AMLNumber.of(expected), actual);
    }

    public static void assertAMLNumber(BigDecimal expected, Object actual) {
        assertTrue(actual instanceof AMLNumber);
        assertEquals(AMLNumber.of(expected), actual);
    }

    public static void assertAMLFraction(int expectedNumerator, int expectedDenominator, Object actual) {
        assertTrue(actual instanceof AMLFraction);
        assertEquals(
            AMLFraction.of(
                AMLNumber.of(expectedNumerator),
                AMLNumber.of(expectedDenominator)
            ),
            actual
        );
    }

    public static void assertAMLBoolean(boolean expected, Object actual) {
        assertTrue(actual instanceof AMLBoolean);
        assertEquals(((AMLBoolean) actual).isTrue(), expected);
    }

    public static void assertAMLSetWithNumbers(Set<Integer> expected, Object actual) {
        assertTrue(actual instanceof AMLSet);
        expected.forEach(i -> ((AMLSet) actual).contains(AMLNumber.of(i)));
    }

    public static void assertAMLSetWithBooleans(Set<Boolean> expected, Object actual) {
        assertTrue(actual instanceof AMLSet);
        expected.forEach(i -> ((AMLSet) actual).contains(AMLBoolean.of(i)));
    }
}
